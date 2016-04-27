package com.openfluency.confuser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.openfluency.language.Alphabet;

import cscie99.team2.lingolearn.server.confuser.CharacterType;
import cscie99.team2.lingolearn.server.confuser.ConfuserTools;
import cscie99.team2.lingolearn.shared.error.ConfuserException;

class ChineseConfuser implements ConfuserInterface {

	// The path to the directory of confusers
	private final static String CONFUSER_DIRECTORY = "/confusers/";
	
	// The extension for the files
	private final static String CONFUSER_EXTENSION = ".confusers";
	
	// The language code we are working with
	private final static String CONFUSER_LANGUAGE = "ch";

    private String[] legalSoundList = [
        "a","o","e","er","aiao","ou","an","en","ang","eng","yi","ya",
        "yao","ye","you","yan","yin","yang","ying","yong","wu","wa","wo","wai","wei","wan","wen",
        "wang","weng","yu","yue","yuan","yun",
        "ba","bo","bai","bei","bao","ban","ben","bang","beng","bi","biao","bie","bian","bin","bing","bu",
        "pa","po","pai","pei","pao","pou","pan","pen","pang","peng","pi","piao","pie","pian","pin","ping","pu",
        "ma","mo","me","mai","mei","mao","mou","man","men","mang","meng","mi","miao","mie","miu","mian","min","ming","mu",
        "fa","fo","fei","fou","fan","fen","fang","feng",
        "da","de","dai","dei","dao","dou","dan","den","dang","deng","dong","di","diao","die","diu","dian","ding","du","duo","dui","duan","dun",
        "ta","te","tai","tei","tao","tou","tan","tang","teng","tong","ti","tiao","tie","tian","ting","tu","tuo","tui","tuan","tun",
        "na","ne","nai","nei","nao","nou","nan","nen","nang","neng","nong","ni","niao","nie","niu","nian","nin","niang","ning","nu","nuo","nuan","nü","nüe","nue", //nue is a simplified nüe
        "la","le","lai","lei","lao","lou","lan","lang","leng","long","li","lia","liao","lie","liu","lian","lin","liang","ling","lu","luo","luan","lun","lü","lüe", "lue", //lue to catche lüe
        "ga","ge","gai","gei","gao","gou","gan","gen","gang","geng","gong","gu","gua","guo","guai","gui","guan","gun","guang",
        "ka","ke","kai","kei","kao","kou","kan","ken","kang","keng","kong","ku","kua","kuo","kuai","kui","kuan","kun","kuang",
        "ha","he","hai","hei","hao","hou","han","hen","hang","heng","hong","hu","hua","huo","huai","hui","huan","hun","huang",
        "za","ze","zi","zai","zei","zao","zou","zan","zen","zang","zeng","zong","zu","zuo","zui","zuan","zun",
        "ca","ce","ci","cai","cao","cou","can","cen","cang","ceng","cong","cu","cuo","cui","cuan","cun",
        "sa","se","si","sai","sao","sou","san","sen","sang","seng","song","su","suo","sui","suan","sun",
        "zha","zhe","zhi","zhai","zhei","zhao","zhou","zhan","zhen","zhang","zheng","zhong","zhu","zhua","zhuo","zhuai","zhui","zhuan","zhun","zhuang",
        "cha","che","chi","chai","chao","chou","chan","chen","chang","cheng","chong","chu","chua","chuo","chuai","chui","chuan","chun","chuang",
        "sha","she","shi","shai","shei","shao","shou","shan","shen","shang","sheng","shu","shua","shuo","shuai","shui","shuan","shun","shuang",
        "re","ri","rao","rou","ran","ren","rang","reng","rong","ru","rua","ruo","rui","ruan","run",
        "ji","jia","jiao","jie","jiu","jian","jin","jiang","jing","jiong","ju","jue","juan","jun",
        "qi","qia","qiao","qie","qiu","qian","qin","qiang","qing","qiong","qu","que","quan","qun",
		"xi","xia","xiao","xie","xiu","xian","xin","xiang","xing","xiong","xu","xue","xuan","xun"];

    private Set<String> legalSounds;

    public ChineseConfuser() {
        legalSounds = new HashSet<String>(Arrays.asList(legalSoundList));
    }

    public static String removeTone(String s)
    {
		StringBuilder sb=new StringBuilder();
		final String [] tone = ["ɑ̄ɑ́ɑ̀ǎ","ēéěè","īíǐì","ōóǒò","ūǖúǘǔǚùǜ","1234"];
		for (int ndx = 0; ndx < s.length(); ndx++) {
			char ch = s.charAt(ndx);
			if ( tone[0].indexOf((int)ch) >= 0)	sb.append('a');
			else if (tone[1].indexOf((int)ch) >= 0)	sb.append('e');
			else if (tone[2].indexOf((int)ch) >= 0)	sb.append('i');
			else if (tone[3].indexOf((int)ch) >= 0)	sb.append('o');
			else if (tone[4].indexOf((int)ch) >= 0)	sb.append('u');
			else if ("1234".indexOf((int)ch) < 0)	sb.append(ch);
        }
		return sb.toString();
    }

    public boolean isLegal(String s){
		String t=removeTone(s.toLowerCase());
        if (legalSounds.contains(removeTone(s.toLowerCase()))) {
            return true;
        }
    }

// TODO:
// This will only work if the user put spaces between each syllable, 
// We may need a better tokenizer if they mush them together like "rénmínbì"
    public String[] tokenize(String str) {
        return str.split("\\s+");    
    }

    public List<String> getAlteredString(String str) {
		String option = ""; // no syllables
        String[] syllables = tokenize(str); 
        List<String> results=new ArrayList<String>();
        for (String s : syllables) {
            List<String> tr= getPinyinSubstitution(s);
			boolean filled=false;
			for (String altered : tr) {
				// If altered syllable is legal, use it, else use the original
				if (isLegal(altered)) {
					if (!filled) {
						for (int i=0; i < results.size(); i++) {
							results.set(i, results.get(i) + " " + altered);   
						}
						filled = true;
					}
					results.add(option + (option.isEmpty()?"":" ") + altered);  // Add the new alternative as an option  
				}
			}
			if (!filled) // Add the syllable to the existing options
				for (int i=0; i < results.size(); i++) {
					results.set(i, results.get(i) + " " + s);   
				}
			option += (option.isEmpty()?"":" ") +  s;
        }
		return results;
    }

	/**
	 * Get a random list of confusers of given type limited to the count 
	 * provided, these results are checked against the black list to 
	 * ensure that nothing inappropriate is returned. 
	 * 
	 * @param word The word (in Chinese) that you want to generate confusers for
	 * @param alphabet The alphabet (Hanzi) that the word is encoded in
	 * @param count The number that should be returned, if -1 is provided then
	 * all results are returned without processing.
	 * @return List of strings containing the confuser results, The length of the list will be between 0 and the requested count.
	 */
	List<String> getConfusers(String word, Alphabet alphabet, int count) {
		
		try {
			List<String> results = [];
		
			if (alphabet.code == "hanzi") {
				results.addAll(getHanziSubsitution(word));
			}
			else if (alphabet.code == "pinyin") {
				Set<String> phrases = new HashSet<String>();
				Set<String> tempResults = new HashSet<String>();
				
				phrases.add(word);				
				
				tempResults.clear();
				for (String phrase : phrases) {
					//tempResults.addAll(getPinyinSubstitution(phrase));
					tempResults.addAll(getAlteredString(phrase));
				}

				boolean updates = true;
				phrases.addAll(tempResults);
				while(updates) {
					updates = false;
					
					int length = phrases.size();

					tempResults.clear();
					for (String phrase : phrases) {
						tempResults.addAll(getPinyinToneSubstitution(phrase))
					}
					phrases.addAll(tempResults);
						
					if (phrases.size() > length) {
						updates = true;
						length = phrases.size();
					}
				}
				
				phrases.remove(word);
				results.addAll(phrases);
			}
			else {
				throw new ConfuserException("Invalid alphabet (" + alphabet.getCode() +" ).");
			}
			
			// Should we return everything?
			if (count == -1) {
				return results;
			}
			
			// Check to make sure all of the results are appropriate
			for (int ndx = 0; ndx < results.size(); ndx++) {
				if (ConfuserTools.onBlackList(results.get(ndx), "jp")) {
					results.remove(ndx);
					ndx--;
				}
			}
			
			// Trim the results down to the count and return them
			Random random = new Random();
			while (results.size() > count) {
				int ndx = random.nextInt(results.size());
				results.remove(ndx);
			}
			return results;
		}
		catch (IOException ex) {
			throw new ConfuserException("There was an error while reading the blacklist.", ex);
		}
	}
	
	Set<String> getPinyinToneSubstitution(String phrase) {
		Set<String> phraseSet = new HashSet<String>();
		
		for (int ndx = 0; ndx < phrase.length(); ndx++) {
			char ch = phrase.charAt(ndx);
			
			Set<String> tempPhraseSet = new HashSet<String>();
			
			if (ch == '1' || ch == '2' || ch == '3' || ch == '4') {
				
				// check the preceding character to ensure that valid tone number
				if (ndx != 0) {
					char charBefore = phrase.charAt(ndx - 1);
					
					if (!Character.isLetter(charBefore)) {
						continue;
					}
				}
				
				// check the next character to ensure that valid tone number
				if ((ndx + 1) < phrase.length()) {
					char charAfter = phrase.charAt(ndx+1);
					
					if (Character.isDigit(charAfter)) {
						continue;
					}
				}
							
				String beginString = phrase.substring(0,ndx)
				String endString = ""
				
				if (ndx < phrase.length()) {
					endString = phrase.substring(ndx+1)
				}
				
				tempPhraseSet.add(beginString+'1'+endString);
				tempPhraseSet.add(beginString+'2'+endString);
				tempPhraseSet.add(beginString+'3'+endString);
				tempPhraseSet.add(beginString+'4'+endString);
				tempPhraseSet.add(beginString+endString);
			}
			else if (ch == 'ā' || ch == 'á' || ch == 'ǎ' || ch == 'à') {
				String beginString = phrase.substring(0,ndx)
				String endString = ""
				
				if (ndx < phrase.length()) {
					endString = phrase.substring(ndx+1)
				}
				
				tempPhraseSet.add(beginString+'ā'+endString);
				tempPhraseSet.add(beginString+'á'+endString);
				tempPhraseSet.add(beginString+'ǎ'+endString);
				tempPhraseSet.add(beginString+'à'+endString);
				tempPhraseSet.add(beginString+'a'+endString);
			}
			else if (ch == 'ɑ̄' || ch == 'ɑ́' || ch == 'ɑ̌' || ch == 'ɑ̀') {
				String beginString = phrase.substring(0,ndx)
				String endString = ""
				
				if (ndx < phrase.length()) {
					endString = phrase.substring(ndx+1)
				}
				
				tempPhraseSet.add(beginString+'ɑ̄'+endString);
				tempPhraseSet.add(beginString+'ɑ́'+endString);
				tempPhraseSet.add(beginString+'ɑ̌'+endString);
				tempPhraseSet.add(beginString+'ɑ̀'+endString);
				tempPhraseSet.add(beginString+'ɑ'+endString);
			}
			else if (ch == 'ē' || ch == 'é' || ch == 'ě' || ch == 'è') {
				String beginString = phrase.substring(0,ndx)
				String endString = ""
				
				if (ndx < phrase.length()) {
					endString = phrase.substring(ndx+1)
				}
				
				tempPhraseSet.add(beginString+'ē'+endString);
				tempPhraseSet.add(beginString+'é'+endString);
				tempPhraseSet.add(beginString+'ě'+endString);
				tempPhraseSet.add(beginString+'è'+endString);
				tempPhraseSet.add(beginString+'e'+endString);
			}
			else if (ch == 'ī' || ch == 'í' || ch == 'ǐ' || ch == 'ì') {
				String beginString = phrase.substring(0,ndx)
				String endString = ""
				
				if (ndx < phrase.length()) {
					endString = phrase.substring(ndx+1)
				}
				
				tempPhraseSet.add(beginString+'ī'+endString);
				tempPhraseSet.add(beginString+'í'+endString);
				tempPhraseSet.add(beginString+'ǐ'+endString);
				tempPhraseSet.add(beginString+'ì'+endString);
				tempPhraseSet.add(beginString+'i'+endString);
			}
			else if (ch == 'ō' || ch == 'ó' || ch == 'ǒ' || ch == 'ò') {
				String beginString = phrase.substring(0,ndx)
				String endString = ""
				
				if (ndx < phrase.length()) {
					endString = phrase.substring(ndx+1)
				}
				
				tempPhraseSet.add(beginString+'ō'+endString);
				tempPhraseSet.add(beginString+'ó'+endString);
				tempPhraseSet.add(beginString+'ǒ'+endString);
				tempPhraseSet.add(beginString+'ò'+endString);
				tempPhraseSet.add(beginString+'o'+endString);
			}
			else if (ch == 'ū' || ch == 'ú' || ch == 'ǔ' || ch == 'ù') {
				String beginString = phrase.substring(0,ndx)
				String endString = ""
				
				if (ndx < phrase.length()) {
					endString = phrase.substring(ndx+1)
				}
				
				tempPhraseSet.add(beginString+'ū'+endString);
				tempPhraseSet.add(beginString+'ú'+endString);
				tempPhraseSet.add(beginString+'ǔ'+endString);
				tempPhraseSet.add(beginString+'ù'+endString);
				tempPhraseSet.add(beginString+'u'+endString);
			}
			else if (ch == 'ǖ' || ch == 'ǘ' || ch == 'ǚ' || ch == 'ǜ') {
				String beginString = phrase.substring(0,ndx)
				String endString = ""
				
				if (ndx < phrase.length()) {
					endString = phrase.substring(ndx+1)
				}
				
				tempPhraseSet.add(beginString+'ǖ'+endString);
				tempPhraseSet.add(beginString+'ǘ'+endString);
				tempPhraseSet.add(beginString+'ǚ'+endString);
				tempPhraseSet.add(beginString+'ǜ'+endString);
				tempPhraseSet.add(beginString+'ü'+endString);
			}
			else if (ch == 'Ā' || ch == 'Á' || ch == 'Ǎ' || ch == 'À') {
				String beginString = phrase.substring(0,ndx)
				String endString = ""
				
				if (ndx < phrase.length()) {
					endString = phrase.substring(ndx+1)
				}
				
				tempPhraseSet.add(beginString+'Ā'+endString);
				tempPhraseSet.add(beginString+'Á'+endString);
				tempPhraseSet.add(beginString+'Ǎ'+endString);
				tempPhraseSet.add(beginString+'À'+endString);
				tempPhraseSet.add(beginString+'A'+endString);
			}
			else if (ch == 'Ē' || ch == 'É' || ch == 'Ě' || ch == 'È') {
				String beginString = phrase.substring(0,ndx)
				String endString = ""
				
				if (ndx < phrase.length()) {
					endString = phrase.substring(ndx+1)
				}
				
				tempPhraseSet.add(beginString+'Ē'+endString);
				tempPhraseSet.add(beginString+'É'+endString);
				tempPhraseSet.add(beginString+'Ě'+endString);
				tempPhraseSet.add(beginString+'È'+endString);
				tempPhraseSet.add(beginString+'E'+endString);
			}
			else if (ch == 'Ī' || ch == 'Í' || ch == 'Ǐ' || ch == 'Ì') {
				String beginString = phrase.substring(0,ndx)
				String endString = ""
				
				if (ndx < phrase.length()) {
					endString = phrase.substring(ndx+1)
				}
				
				tempPhraseSet.add(beginString+'Ī'+endString);
				tempPhraseSet.add(beginString+'Í'+endString);
				tempPhraseSet.add(beginString+'Ǐ'+endString);
				tempPhraseSet.add(beginString+'Ì'+endString);
				tempPhraseSet.add(beginString+'I'+endString);
			}
			else if (ch == 'Ō' || ch == 'Ó' || ch == 'Ǒ' || ch == 'Ò') {
				String beginString = phrase.substring(0,ndx)
				String endString = ""
				
				if (ndx < phrase.length()) {
					endString = phrase.substring(ndx+1)
				}
				
				tempPhraseSet.add(beginString+'Ō'+endString);
				tempPhraseSet.add(beginString+'Ó'+endString);
				tempPhraseSet.add(beginString+'Ǒ'+endString);
				tempPhraseSet.add(beginString+'Ò'+endString);
				tempPhraseSet.add(beginString+'O'+endString);
			}
			else if (ch == 'Ū' || ch == 'Ú' || ch == 'Ǔ' || ch == 'Ù') {
				String beginString = phrase.substring(0,ndx)
				String endString = ""
				
				if (ndx < phrase.length()) {
					endString = phrase.substring(ndx+1)
				}
				
				tempPhraseSet.add(beginString+'Ū'+endString);
				tempPhraseSet.add(beginString+'Ú'+endString);
				tempPhraseSet.add(beginString+'Ǔ'+endString);
				tempPhraseSet.add(beginString+'Ù'+endString);
				tempPhraseSet.add(beginString+'U'+endString);
			}
			else if (ch == 'Ǖ' || ch == 'Ǘ' || ch == 'Ǚ' || ch == 'Ǜ') {
				String beginString = phrase.substring(0,ndx)
				String endString = ""
				
				if (ndx < phrase.length()) {
					endString = phrase.substring(ndx+1)
				}
				
				tempPhraseSet.add(beginString+'Ǖ'+endString);
				tempPhraseSet.add(beginString+'Ǘ'+endString);
				tempPhraseSet.add(beginString+'Ǚ'+endString);
				tempPhraseSet.add(beginString+'Ǜ'+endString);
				tempPhraseSet.add(beginString+'Ü'+endString);
			}
			
			phraseSet.addAll(tempPhraseSet)
		}
		
		return phraseSet
	}
	
	List<String> getPinyinSubstitution(String phrase) {
		List<String> results = new ArrayList<String>();
		
		String vowels = "āɑ̄ēīōūǖĀĒĪŌŪǕáɑ́éíóúǘÁÉÍÓÚǗǎɑ̌ěǐǒǔǚǍĚǏǑǓǙàɑ̀èìòùǜÀÈÌÒÙǛaɑeiouüAEIOUÜ"

		for (int ndx = 0; ndx < phrase.length(); ndx++) {
			char ch = phrase.charAt(ndx);
			
			if (vowels.indexOf((int)ch) >= 0) {
				// swap vowel+n with vowel + ng
				if ((ndx + 1) < phrase.length()) {
					char charAfterN = phrase.charAt(ndx+1);
					char charAfterG = '\0' as char;
					
					if ((ndx + 2) < phrase.length()) {
						charAfterG = phrase.charAt(ndx+2)
					}
					
					if (charAfterN == 'n' && charAfterG == 'g') {
						String beginString = phrase.substring(0,ndx+1)
						String endString = ""
						
						if ((ndx+3) <= phrase.length()) {
							endString = phrase.substring(ndx+3)
						}
						
						results.add(beginString+'n'+endString);
					}
					else if (charAfterN == 'n') {
						String beginString = phrase.substring(0,ndx+1)
						String endString = ""
						
						if ((ndx+2) < phrase.length()) {
							endString = phrase.substring(ndx+2)
						}
						
						results.add(beginString+'ng'+endString)
					}
				}
			}
			else if (ch == 'j') {
				results.add(addCharacterToStringAtPosition(phrase, ndx, (char)'q'))
			}
			else if (ch == 'J') {
				results.add(addCharacterToStringAtPosition(phrase, ndx, (char)'Q'))
			}
			else if (ch == 'q') {
				results.add(addCharacterToStringAtPosition(phrase, ndx, (char)'j'))
			}
			else if (ch == 'Q') {
				results.add(addCharacterToStringAtPosition(phrase, ndx, (char)'J'))
			}
			else if (ch == 'z') {
				results.add(addCharacterToStringAtPosition(phrase, ndx, (char)'c'))
			}
			else if (ch == 'Z') {
				results.add(addCharacterToStringAtPosition(phrase, ndx, (char)'C'))
			}
			else if (ch == 'c') {
				results.add(addCharacterToStringAtPosition(phrase, ndx, (char)'z'))
			}
			else if (ch == 'C') {
				results.add(addCharacterToStringAtPosition(phrase, ndx, (char)'Z'))
			}
			else if (ch == 't') {
				results.add(addCharacterToStringAtPosition(phrase, ndx, (char)'d'))
			}
			else if (ch == 'T') {
				results.add(addCharacterToStringAtPosition(phrase, ndx, (char)'D'))
			}
			else if (ch == 'd') {
				results.add(addCharacterToStringAtPosition(phrase, ndx, (char)'t'))
			}
			else if (ch == 'D') {
				results.add(addCharacterToStringAtPosition(phrase, ndx, (char)'T'))
			}
			else if (ch == 'r') {
				String beginString = phrase.substring(0,ndx)
				String endString = ""
				
				if ((ndx+2) <= phrase.length()) {
					endString = phrase.substring(ndx+2)
				}
		
				results.add(beginString+"zh"+endString)
			}
			else if (ch == 'R') {
				String beginString = phrase.substring(0,ndx)
				String endString = ""
		
				if ((ndx+2) <= phrase.length()) {
					endString = phrase.substring(ndx+2)
				}
				
				results.add(beginString+"Zh"+endString)
			}
			else if (ch == 'h') {
				// swap zh with r
				if (ndx != 0) {
					char charBefore = phrase.charAt(ndx - 1);
					
					if (charBefore == (char)'z') {
						String beginString = phrase.substring(0,ndx-1)
						String endString = ""
				
						if ((ndx+2) <= phrase.length()) {
							endString = phrase.substring(ndx+1)
						}
						
						results.add(beginString+"r"+endString)
					}
					else if (charBefore == (char)'Z') {
						String beginString = phrase.substring(0,ndx-1)
						String endString = ""
				
						if ((ndx+1) <= phrase.length()) {
							endString = phrase.substring(ndx+1)
						}
						
						results.add(beginString+"R"+endString)
					}
				}
			}
		}

		
		
		return results;
	}
	
	private String addCharacterToStringAtPosition(String phrase, int index, char character) {
		String beginString = phrase.substring(0,index)
		String endString = ""
		
		if (index < phrase.length()) {
			endString = phrase.substring(index+1)
		}
		
		return beginString+character+endString
	}
	
	/**
	 * Get a list of hanzi phrases that are similar to the one that has been
	 * provided. In the event that no hanzi confusers for the given card then
	 * null will be returned, otherwise, confusers up to the provided count will
	 * be provided.
	 * 
	 * @param card
	 * @return
	 */
	List<String> getHanziSubsitution(String phrase) {
		// Read the confusers from the resource file
		List<String> confusers = readConfusers();
		
		// Iterate through the characters in this phrase
		List<String> phrases = new ArrayList<String>();
		for (int ndx = 0; ndx < phrase.length(); ndx++) {
			char ch = phrase.charAt(ndx);
			if (ConfuserTools.checkCharacter(ch) != CharacterType.Kanji) {
				continue;
			}
			// Iterate through each of the lines for confusers
			for (String confuser : confusers) {
				if (confuser.contains(String.valueOf(ch))) {
					phrases.addAll(getReplacements(phrase, ch, ndx, confuser));
					break;
				}
			}
		}
		// Return the confusers
		return phrases;		
	}
	
	/**
	 * Generate all of the valid replacements for the parameters provided.
	 * 
	 * @param phrase The Hanzi phrase have the substitutions applied to.
	 * @param kanji The Hanzi that is to be replaced.
	 * @param index The index that the Hanzi exists at.
	 * @param replacements The Hanzi family as read from the confusers list.
	 * @return The list of updated Hanzi phrases.
	 */
	private List<String> getReplacements(String phrase, char kanji, int index, String replacements) {
		// Make sure the Hanzi provided does not appear in the list
		replacements = replacements.replace(String.valueOf(kanji), "");
		// Iterate through the replacements and generate new strings with 
		// the character at the index being replaced
		List<String> phrases = new ArrayList<String>();
		for (int ndx = 0; ndx < replacements.length(); ndx++) { 
			char replacement = replacements.charAt(ndx);
			// Make sure the phrase is built correctly
			if (index == 0) {
				phrases.add(String.valueOf(replacement) + phrase.substring(1));
				continue;
			}
			int offset = (index == 1) ? 1 : index - 1;
			if (index == (phrase.length() - 1)) {
					phrases.add(phrase.substring(0, offset) + String.valueOf(replacement));
			} else {
				String bah = phrase.substring(0, offset) + replacement + phrase.substring(index + 1);

				phrases.add(bah);
			}
		}
		return phrases;
	}
	
	/**
	 * Read the Hanzi families for confusers from the resource file.
	 * 
	 * @return The confuser families as a list of strings.
	 */
	private List<String> readConfusers() throws ConfuserException, IOException {
		// Open the black list for this language and check to see if it exists
		String path = CONFUSER_DIRECTORY + CONFUSER_LANGUAGE + CONFUSER_EXTENSION;
		InputStream confusers = ConfuserTools.class.getResourceAsStream(path);
		if (confusers == null) {
			throw new ConfuserException("Unable to open the confusers for, " + CONFUSER_LANGUAGE);
		}
		// Prepare the stream reader
		BufferedReader reader = null;
		InputStreamReader stream = null;
		try {
			// Open up the stream to be read
			stream = new InputStreamReader(confusers);
			reader = new BufferedReader(stream);
			// Read and process the contents
			List<String> results = new ArrayList<String>();
			String data;
			while ((data = reader.readLine()) != null) {
				if (data.isEmpty() || data.charAt(0) == '#') {
					continue;
				}
				results.add(data.replace(" ", ""));
			}
			return results;
		} catch (FileNotFoundException ex) {
			throw new ConfuserException("Unable to open the confusers for, " + CONFUSER_LANGUAGE);
		} catch (IOException ex) {
			throw new ConfuserException("An error occured while reading the next line", ex);
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (stream != null) {
				stream.close();
			}
		}
	}
}
