package cscie599.openfluency2;

import java.util.EnumMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * katakana, Hiragana and Hangul from
 From http://www.loc.gov/marc/specifications/specchartables.html
 East Asian ideographs from the East Asian Coded Character set (ANSI/NISO Z39.64, or "EACC", including 10 "Version J" 
   Kanji from http://www.unicode.org/charts/
   Anki uses utf-8 encoding
   This module uses both the fieldname and the field text to identify the language and alphabet of the text
 */
public class CharSetIdentifier {

	private static final Logger logger =	
			LoggerFactory.getLogger(CharSetIdentifier.class);
	public static enum Charset {Hiragana,Katakana,Hangul,Kanji,Latin,Romaji,English,Unknown,Mixed}; // English is not really a charset
	public static Pattern hiraganaReg = Pattern.compile("hiragana",Pattern.CASE_INSENSITIVE);
	public static Pattern katakanaReg = Pattern.compile("katakana",Pattern.CASE_INSENSITIVE);
	public static Pattern kanjiReg = Pattern.compile("kanji|hanji|kangxi|hanga",Pattern.CASE_INSENSITIVE);
	public static Pattern hangulReg = Pattern.compile("hangul|korean",Pattern.CASE_INSENSITIVE);
	public static Pattern englishReg = Pattern.compile("english",Pattern.CASE_INSENSITIVE);
	public static Pattern romajiReg = Pattern.compile("romanji|romaji|roumaji",Pattern.CASE_INSENSITIVE);
	public static Pattern kanaReg = Pattern.compile("kana",Pattern.CASE_INSENSITIVE);

	public static Pattern hangulCharReg = Pattern.compile("[\\p{IsHangul}]",Pattern.UNICODE_CHARACTER_CLASS);

	public static Pattern hiraganaCharReg = Pattern.compile("[\\p{InHiragana}]",Pattern.UNICODE_CHARACTER_CLASS);
	public static Pattern katakanaCharReg = Pattern.compile("[\\p{InKatakana}]",Pattern.UNICODE_CHARACTER_CLASS);
	public static Pattern hanCharReg = Pattern.compile("[\\p{IsHan}]",Pattern.UNICODE_CHARACTER_CLASS);
	public static Pattern latinCharReg = Pattern.compile("[\\p{Alpha}]"); 
	//public static Pattern latinCharReg = Pattern.compile("[\\p{InLatin}]",Pattern.UNICODE_CHARACTER_CLASS); 
	
	EnumMap<Charset,Integer> alpha;
	EnumMap<Charset,Pattern> regex;
	int totalchars = 0;
	public CharSetIdentifier(){
		alpha= new EnumMap<>(Charset.class) ;
		for(Charset c: Charset.values()){
			alpha.put(c, 0);
		}
		//alpha.put(Charset.Hiragana, 0); alpha.put(Charset.Katakana, 0); alpha.put(Charset.Hangul, 0); alpha.put(Charset.Kanji, 0); alpha.put(Charset.Latin, 0);
	}

	public static Charset testField(String field) {
		Matcher m = hiraganaReg.matcher(field);
		if (m.find()) return Charset.Hiragana;
		m = katakanaReg.matcher(field);
		if (m.find()) return Charset.Katakana;
		m = kanaReg.matcher(field); // probably hirakana
		if (m.find()) return Charset.Hiragana;
		m = kanjiReg.matcher(field);
		if (m.find()) return Charset.Kanji;
		m = romajiReg.matcher(field);
		if (m.find()) return Charset.Romaji;
		m = englishReg.matcher(field);
		if (m.find()) return Charset.English;
		m = hangulReg.matcher(field);
		if (m.find()) return Charset.Hangul;
		return Charset.Unknown;
	}
	
	/**
	 * Add text from the same source so that this module can guess at what the alphabet is
	 * @param str
	 */
	public void addText(String str) {
		totalchars+= str.length();
		int ct=0;
		Matcher m = hiraganaCharReg.matcher(str);
		while (m.find()) { ct++; }
		alpha.put(Charset.Hiragana,ct + alpha.get(Charset.Hiragana));

		ct=0;
		m = katakanaCharReg.matcher(str);
		while (m.find()) { ct++; }
		alpha.put(Charset.Katakana,ct + alpha.get(Charset.Katakana));

		ct=0;
		m = hanCharReg.matcher(str);
		while (m.find()) { ct++;}
		logger.debug(str + "kanji "+ ct);
		alpha.put(Charset.Kanji,ct + alpha.get(Charset.Kanji));

		ct=0;
		m = latinCharReg.matcher(str);
		while (m.find()) { ct++; }
		alpha.put(Charset.Latin,ct + alpha.get(Charset.Latin));
	}
	
	/**
	 * AFter calling it with enough text, the module should have some stats as to what the alphabet is
	 * @return
	 */
	public Charset getCharSet(){
		float max=0;
		float nextmax=0;
		Charset m=null;
		Charset nextm=null;
		for (Charset e : Charset.values()){
			if (alpha.get(e) > max) {
				nextmax = max;
				max = alpha.get(e);
				nextm=m;
				m=e;
			}
			else if (alpha.get(e) > nextmax) {
				 nextmax = alpha.get(e);
		    }
		}
		// If at least half the chars are of one type and it is twice that of the next type
		if (totalchars==0 || max < 1) return Charset.Unknown;
		if ((nextmax/max) > .5 ) return Charset.Mixed;
		if ((max/totalchars) > .5) // majority 
			return m;
		else
			return Charset.Unknown;
	}
}
