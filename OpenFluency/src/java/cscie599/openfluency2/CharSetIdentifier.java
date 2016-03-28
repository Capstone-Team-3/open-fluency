package cscie599.openfluency2;

import java.util.EnumMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * katakana, Hiragana and Hangul from
 From http://www.loc.gov/marc/specifications/specchartables.html
 East Asian ideographs from the East Asian Coded Character set (ANSI/NISO Z39.64, or "EACC", including 10 "Version J" 
   Kanji from http://www.unicode.org/charts/
   Anki uses utf-8 encoding
   This module uses both the fieldname and the field text to identify the language and alphabet of the text
 */
public class CharSetIdentifier {
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
	//public static Pattern hanCharReg = Pattern.compile("[\u4e00-\u9faf]",Pattern.UNICODE_CHARACTER_CLASS);
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
		Matcher m = hiraganaCharReg.matcher(str);
		int ct=0;
		while (m.find()) { ct += m.groupCount(); }
		alpha.put(Charset.Hiragana,ct);
		m = katakanaCharReg.matcher(str);
		ct=0;
		while (m.find()) { ct += m.groupCount(); }
		alpha.put(Charset.Katakana,ct);
		m = hanCharReg.matcher(str);
		ct=0;
		while (m.find()) { ct += m.groupCount(); }
		alpha.put(Charset.Kanji,ct);
		m = latinCharReg.matcher(str);
		ct=0;
		while (m.find()) { ct += m.groupCount(); }
		alpha.put(Charset.Latin,ct);
	}
	
	/**
	 * AFter calling it with enough text, the module should have some stats as to what the alphabet is
	 * @return
	 */
	public Charset getCharSet(){
		int max=0;
		int nextmax=0;
		Charset m=null;
		Charset nextm=null;
		for (Charset e : Charset.values()){
			if (alpha.get(e) > max) {
				nextmax = max;
				max = alpha.get(e);
				nextm=m;
				m=e;
			}
		}
		// If at least half the chars are of one type and it is twice that of the next type
		if (totalchars==0 || max==0) return Charset.Unknown;
		if (nextmax/max > .5 ) return Charset.Mixed;
		if (max/totalchars > .5) // majority 
			return m;
		else
			return Charset.Unknown;
	}
	
	
	public static Charset identifier(String str) {
		EnumMap<Charset,Integer> alpha= new EnumMap<>(Charset.class) ;
		alpha.put(Charset.Hiragana, 0);
		alpha.put(Charset.Katakana, 0);
		alpha.put(Charset.Hangul, 0);
		alpha.put(Charset.Kanji, 0);
		alpha.put(Charset.Latin, 0);
		int len = str.length();
		for (int i =0; i < len; i++) {
			/*from <ucs>3041</ucs><utf-8>E38181</utf-8> 
		  to <ucs>3093</ucs><utf-8>E38293</utf-8>
			 */
			if ( 'ぁ' <= str.charAt(i) && str.charAt(i) <= 'ぁ') {
				alpha.put(Charset.Hiragana,alpha.get(Charset.Hiragana) + 1);
			}
			/*
			 * from <ucs>309C</ucs><utf-8>E3829C</utf-8>
			 * to <ucs>30A1</ucs><utf-8>E382A1</utf-8>
			 */
			else if ( 'ァ' <= str.charAt(i) && str.charAt(i) <= 'ヶ') {
				alpha.put(Charset.Katakana,alpha.get(Charset.Katakana) + 1);
			}
			/* hangul - end
			 *  2,028 mappings of character encodings for
				 Korean hangul from the East Asian Coded Character set (ANSI/NISO Z39.64
			 */
			else if (0xE384B1 <= str.charAt(i) && str.charAt(i) <= 0xECAD8C) {
				alpha.put(Charset.Hangul,alpha.get(Charset.Hangul) + 1);
			}
			// UCS range = 3007 -> ã€‡ 
			// 4E00 to 9FD5
			else if (('一' <= str.charAt(i) && str.charAt(i) <= '䲤') 
					|| ( '〇' == str.charAt(i))) {
				//else if (( 0xE38087 <= str.charAt(0)) && (str.charAt(0) >= 0xE9BBA2))  {
				alpha.put(Charset.Kanji,alpha.get(Charset.Kanji) + 1);
			}
			else if ( 0 < str.charAt(i) && str.charAt(i) > 127)  {
				alpha.put(Charset.Latin,alpha.get(Charset.Latin) + 1);
			}
		}
	Charset pick = Charset.Hiragana;
	for(Charset s: Charset.values()) {
		if (alpha.get(s) > alpha.get(pick)) {
			pick = s;
		}
	}
	return pick;
	}
}
