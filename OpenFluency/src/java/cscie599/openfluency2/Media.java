/**
 * Code to determine data type
 * patterns copied from: https://github.com/ankidroid/Anki-Android
 * 
 * All the media are numbered, the index to the media is a hashmap
 * named media that maps an integer to a file name. They can be images or sounds
 * 
 * The cards refer to the media by name
 * 
 */
package cscie599.openfluency2;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Code to identify different media types
 * @author phoebemiller
 * Copied code liberally from Anki-Android project on github
 *
 */
public class Media {
	private static final Pattern fIllegalCharReg = Pattern.compile("[><:\"/?*^\\\\|\\x00\\r\\n]");
    private static final Pattern fRemotePattern  = Pattern.compile("(https?|ftp)://");
    private static final Pattern fSoundRegexps = Pattern.compile("(?i)(\\[sound:([^]]+)\\])");
    private static final Pattern fImgRegExpQ = Pattern.compile("(?i)(<img[^>]* src=([\\\"'])([^>]+?)(\\2)[^>]*>)");
    private static final Pattern fImgRegExpU = Pattern.compile("(?i)(<img[^>]* src=(?!['\\\"])([^ >]+)[^>]*?>)");
    /**
     * Pattern used to parse URI (according to http://tools.ietf.org/html/rfc3986#page-50)
     */
    private static Pattern sUriPattern = Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$");
    public static List<Pattern> mRegexps =  Arrays.asList(fSoundRegexps, fImgRegExpQ, fImgRegExpU);

    public Media(String mediaDir) {
        //create media directory
        File fd = new File(mediaDir);
        if (!fd.exists()) {
            if (!fd.mkdir()) {
                //logger.error("Cannot create media directory: " + mediaDir);
            }
        }
    }
    private static final String mark = "-_.!~*'()\"";
    private static final char[] hex = "0123456789ABCDEF".toCharArray();

    public static String encodeURI(String argString) {
      StringBuilder uri = new StringBuilder();

      char[] chars = argString.toCharArray();
      for (int i = 0; i < chars.length; i++) {
        char c = chars[i];
        if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') ||
            (c >= 'A' && c <= 'Z') || mark.indexOf(c) != -1) {
          uri.append(c);
        } else {
          appendEscaped(uri, c);
        }
      }
      return uri.toString();
    }

    private static void appendEscaped(StringBuilder uri, char c) {
      if (c <= (char) 0xF) {
        uri.append("%");
        uri.append('0');
        uri.append(hex[ c]);
      } else if (c <= (char) 0xFF) {
        uri.append("%");
        uri.append(hex[ c >> 8]);
        uri.append(hex[ c & 0xF]);
      } else {
        // unicode
        uri.append('\\');
        uri.append('u');
        uri.append(hex[ c >> 24]);
        uri.append(hex[(c >> 16) & 0xF]);
        uri.append(hex[(c >> 8) & 0xF]);
        uri.append(hex[ c & 0xF]);
      }
    }
    
    // Return path of sound file
    public static Boolean isSound(String str) {
	//String mimeTYpe = Files.probeContentType(path);
      Matcher uriMatcher = fSoundRegexps.matcher(str.trim());
        if (uriMatcher.matches() && uriMatcher.group(1) != null)
        	return true;
        return false;
    }
    
    public static Boolean isImage(String str) {
    	for (Pattern p : Arrays.asList(fImgRegExpQ, fImgRegExpU)) {
            Matcher m = p.matcher(str);
            if (m.find()) {
            	return true;
            }
    	}
		return false;
    }

    // return Path - URI or local file - prepend path if local
    public static String getImage(String str, String path) {
    	for (Pattern p : Arrays.asList(fImgRegExpQ, fImgRegExpU)) {
            Matcher m = p.matcher(str);
            int fnameIdx = p == fImgRegExpU ? 2 : 3;
            while (m.find()) {
                //String tag = m.group(0);
                String fname = m.group(fnameIdx);
                if (fRemotePattern.matcher(fname).find()) {
                    //dont't do any escaping if remote image
                } else {
                	if (path != null)
						fname = path + File.separator + encodeURI(fname);
                }
        	   return fname;
            }
    	}
       return null;
    }
    
    // replace all relative image path with new directory name
    public static String addImagePath(String str, String path) {
    	String newStr = "";
    	for (Pattern p : Arrays.asList(fImgRegExpQ, fImgRegExpU)) {
            Matcher m = p.matcher(str);
            int fnameIdx = p == fImgRegExpU ? 2 : 3;
            int lastpos=0;
            while (m.find()) {
                //String tag = m.group(0);
                String fname = m.group(fnameIdx);
				int startpos = m.start(fnameIdx);
				int endpos = m.end(fnameIdx);
                if (fRemotePattern.matcher(fname).find() || path == null) {
                    //dont't do anything if remote image
					newStr = newStr.concat(str.substring(lastpos, endpos));
                } else {
					newStr = newStr.concat(str.substring(lastpos, startpos) + path + File.separator + str.substring(startpos,m.end()));
                }
				lastpos= endpos;
            }
    	}
    	return newStr;
    }


    private static boolean hasURIScheme(String path) {
        Matcher uriMatcher = sUriPattern.matcher(path.trim());
        return uriMatcher.matches() && uriMatcher.group(2) != null;
    }

    
    // Need it to determine Audio vs Video
    public static String getMime(File file) throws IOException {
    	Path path = file.toPath();
		String mimeType = Files.probeContentType(path);
		return mimeType;
    }
    	
    public static String getSuffix(File file) {
    	String name = file.getName();
    	int extIndex = name.lastIndexOf(".");
    	if (extIndex == -1) { return ""; }
    	else { return name.substring(extIndex + 1); }
    }

    // Return first sound file name
    public static String getSound(String content,String soundDir) {
        Matcher matcher = fSoundRegexps.matcher(content);
        if (matcher.find()) {
            String sound = matcher.group(1).trim();
			if (hasURIScheme(sound)) {
				return sound;
			}
			else
				return soundDir + File.separator + encodeURI(sound);
        }
		return null;
    }

    // TODO: what to do with multiple audio
    public static String addSoundPath(String content, String path) { //, int qa) {
        Matcher matcher = fSoundRegexps.matcher(content);
        // While there is matches of the pattern for sound markers
        String newStr = "";
        int lastpos=0;
        while (matcher.find()) {
            // Create appropriate list if needed; list must not be empty so long as code does no check
        	/*
            if (!mSoundPaths.containsKey(qa)) {
                mSoundPaths.put(qa, new ArrayList<String>());
            }
            */
            // Get the sound file name
            String sound = matcher.group(2).trim();
            // Construct the sound path and store it
           // mSoundPaths.get(qa).add(getSoundPath(soundDir, sound));
                    //String tag = m.group(0);
			int startpos = matcher.start(2);
			int endpos = matcher.end(1);
            if (fRemotePattern.matcher(sound).find() || path == null) {
				newStr = newStr.concat(content.substring(lastpos, endpos));
            } else {
				newStr = newStr.concat(content.substring(lastpos, startpos) + path + File.separator + content.substring(startpos,matcher.end()));
            }
            lastpos = endpos;
        }
       	return newStr;
    }
    
    public String escapeImages(String string, boolean unescape) {
        for (Pattern p : Arrays.asList(fImgRegExpQ, fImgRegExpU)) {
            Matcher m = p.matcher(string);
            int fnameIdx = p == fImgRegExpU ? 2 : 3;
            while (m.find()) {
                String tag = m.group(0);
                String fname = m.group(fnameIdx);
                if (fRemotePattern.matcher(fname).find()) {
                    //dont't do any escaping if remote image
                } else {
                	/*
                    if (unescape) {
                        string = string.replace(tag,tag.replace(fname, decode(fname)));
                    } else {
                    */
					string = string.replace(tag,tag.replace(fname, encodeURI(fname)));
                   // }
                }
            }
        }
        return string;
    }
}
