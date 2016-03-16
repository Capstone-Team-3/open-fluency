package cscie99.team2.lingolearn.server.confuser;

import static org.junit.Assert.assertEquals
import org.junit.Test
import cscie99.team2.lingolearn.shared.error.ConfuserException

class ConfuserToolsTest extends GroovyTestCase {

	/**
	 * Check to make sure the black list is running correctly by using
	 * a phrase from the middle of the list. 
	 */
	@Test
	public void testOnBlackList() throws ConfuserException, IOException {
		// Check the phrase "Stop being stupid" against the blacklist
		assertEquals(true, ConfuserTools.onBlackList("ふざけるな", "jp"));
		// Check to phrase "Hello" against the blacklist
		assertEquals(false, ConfuserTools.onBlackList("こんにちは", "jp"));
	}
	
	/**
	 * Check the detection of Unicode ranges by using the standard ranges from
	 * http://www.unicode.org/charts/
	 */
	@Test
	public void testCheckCharacter() throws ConfuserException {
		final char punctuation = '。';
		
		// Check to make sure unknowns work by checking against basic ASCII and
		// acting accordingly when we are in the number range versus the rest 
		// of the range since Arabic numbers are used in Japanese
		for (char ndx = 0x021; ndx < 0x007E; ndx++) {
			char ch = Character.valueOf(ndx);
			if (Character.isDigit(ch)) {
				assertEquals(CharacterType.Number, ConfuserTools.checkCharacter(ch));
			} else {
				assertEquals(CharacterType.Unknown, ConfuserTools.checkCharacter(ch));
			}
		}
		// Check to make sure the punctuation is detected correctly
		assertEquals(CharacterType.Punctuation, ConfuserTools.checkCharacter(punctuation));
		// Check to make sure Hiragana pass
		for (char ndx = 0x3041; ndx < 0x3096; ndx++) {
			assertEquals(CharacterType.Hiragana, ConfuserTools.checkCharacter(Character.valueOf(ndx)));
		}
		// Check to make sure the katakana pass
		for (char ndx = 0x30A1; ndx < 0x30FC; ndx++) {
			assertEquals(CharacterType.Katakana, ConfuserTools.checkCharacter(Character.valueOf(ndx)));
		}
		// Check to make sure the kanji pass, note that we are checking the 
		// full CJK range so this includes kanji that are not used in Japanese
		for (char ndx = 0x4E00; ndx < 0x9FAF; ndx++) {
			assertEquals(CharacterType.Kanji, ConfuserTools.checkCharacter(Character.valueOf(ndx)));
		}
	}
	
	/**
	 * Check to make sure the phrase types are being detected correctly.
	 */
	@Test
	public void testPhrasetype() {
		// Test to make sure さようなら  (Goodbye) is detected correctly
		assertEquals(PhraseType.Hiragana, ConfuserTools.checkPhrase("さようなら"));
		
		// Test to make sure お元気ですか (How are you?) is detected correctly
		assertEquals(PhraseType.Kanji, ConfuserTools.checkPhrase("お元気ですか"));
		
		//Test to make sure コンピュータ (computer) is detected correctly
		assertEquals(PhraseType.Katakana, ConfuserTools.checkPhrase("コンピュータ"));
		
		// Test to make sure 私の名前はエルビスです (My Name is Elvis) is detected correctly
		assertEquals(PhraseType.Mixed, ConfuserTools.checkPhrase("私の名前はエルビスです"));
	}
}
