package cscie599.openfluency2;

import java.util.EnumMap;

import cscie599.openfluency2.CharSetParser.Charset;

/*
 * katakana, Hiragana and Hangul from
 From http://www.loc.gov/marc/specifications/specchartables.html
 East Asian ideographs from the East Asian Coded Character set (ANSI/NISO
 Z39.64, or "EACC", including 10 "Version J" 
   Kanji from http://www.unicode.org/charts/
   Anki uses utf-8 encoding
 */
public class CharSetParser {
	public static enum Charset {Hiragana,Katakana,Hangul,Kanji,Ascii};
	public static Charset identifier(String str) {
		EnumMap<Charset,Integer> alpha= new EnumMap<>(Charset.class) ;
		alpha.put(Charset.Hiragana, 0);
		alpha.put(Charset.Katakana, 0);
		alpha.put(Charset.Hangul, 0);
		alpha.put(Charset.Kanji, 0);
		alpha.put(Charset.Ascii, 0);
		int len = str.length();
		for (int i =0; i < len; i++) {
			/*from <ucs>3041</ucs><utf-8>E38181</utf-8> 
		  to <ucs>3093</ucs><utf-8>E38293</utf-8>
			 */
			if ( 'ぁ' <= str.charAt(i) && str.charAt(i) <= 'ん') {
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
			// UCS range = 3007 -> 〇 
			// 4E00 to 9FD5
			else if (('一' <= str.charAt(i) && str.charAt(i) <= '䲤') 
					|| ( '〇' == str.charAt(i))) {
				//else if (( 0xE38087 <= str.charAt(0)) && (str.charAt(0) >= 0xE9BBA2))  {
				alpha.put(Charset.Kanji,alpha.get(Charset.Kanji) + 1);
			}
			else if ( 0 < str.charAt(i) && str.charAt(i) > 127)  {
				alpha.put(Charset.Ascii,alpha.get(Charset.Ascii) + 1);
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
