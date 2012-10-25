package dfh.trie.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import dfh.trie.Trie;
import dfh.trie.TrieException;

/**
 * A bunch of regression tests based on problems found with actual data.
 * <p>
 * <b>Creation date:</b> Oct 27, 2011
 * 
 * @author David Houghton
 * 
 */
public class DictionaryTest {

	@Test
	public void testBasic() {
		String[] ar = { "cat", "dog", "llama" };
		try {
			assertEquals(selfMatch(ar, Trie.AUTO_BOUNDARY), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void reversal() {
		String[] ar = { "cat", "dog", "llama" };
		String[] reversed = { "tac", "god", "amall" };
		try {
			Pattern p = Pattern.compile(Trie.trie(ar, Trie.REVERSE));
			for (String s : reversed)
				assertTrue(p.matcher(s).matches());
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test1() {
		String[] ar = { "a", "a!" };
		try {
			assertEquals(selfMatch(ar, Trie.AUTO_BOUNDARY), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test2() {
		String[] ar = { "a/a" };
		try {
			assertEquals(selfMatch(ar, Trie.AUTO_BOUNDARY), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test3() {
		String[] ar = { "a-a", "a a" };
		try {
			assertEquals(selfMatch(ar, Trie.AUTO_BOUNDARY), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test4() {
		String[] ar = { "a b", "a", "b" };
		try {
			assertEquals(selfMatch(ar, Trie.AUTO_BOUNDARY), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test5() {
		String[] ar = { "abcc", "abc", "a", };
		try {
			assertEquals(selfMatch(ar, Trie.CONDENSE), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test6() {
		String[] ar = { "You're Driving Me Crazy", "You're Dead...", };
		try {
			assertEquals(selfMatch(ar, Trie.AUTO_BOUNDARY | Trie.SPACEANDTAB),
					true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test7() {
		String[] ar = { "abbc", "ab", "a", };
		try {
			assertEquals(selfMatch(ar, 0), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test8() {
		String[] ar = { "aXX", "aX", "a", };
		try {
			assertEquals(selfMatch(ar, Trie.PRESERVE_WHITESPACE), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test9() {
		String[] ar = { "x, x, x!", "x al y III", "x al y II", "x al y",
				"x a z", };
		try {
			assertEquals(
					selfMatch(ar, Trie.PRESERVE_WHITESPACE | Trie.CONDENSE),
					true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test10() {
		String[] ar = { "a....", "b", };
		try {
			assertEquals(
					selfMatch(ar, Trie.PRESERVE_WHITESPACE | Trie.CONDENSE),
					true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test11() {
		String[] ar = { "aild West", "aest", "ac", };
		try {
			assertEquals(
					selfMatch(ar, Trie.PRESERVE_WHITESPACE | Trie.CONDENSE),
					true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test12() {
		String[] ar = { "ax...sssshhhhh", "a", };
		try {
			assertEquals(
					selfMatch(ar, Trie.PRESERVE_WHITESPACE | Trie.CONDENSE),
					true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test13() {
		String[] ar = { "ax...sssshhhhh", };
		try {
			assertEquals(
					selfMatch(ar, Trie.PRESERVE_WHITESPACE | Trie.CONDENSE),
					true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test14() {
		String[] ar = { "Km.0", "Km. 0", };
		try {
			assertEquals(selfMatch(ar, Trie.SPACEANDTAB | Trie.CONDENSE), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test15() {
		String[] ar = { "a a a a", "a b a a", "a a a", };
		try {
			assertEquals(
					selfMatch(ar, Trie.PRESERVE_WHITESPACE | Trie.CONDENSE
							| Trie.AUTO_BOUNDARY), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test16() {
		String[] ar = { "a a a a", "a a a", };
		try {
			assertEquals(
					selfMatch(ar, Trie.SPACEANDTAB | Trie.CONDENSE
							| Trie.AUTO_BOUNDARY), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test17() {
		String[] ar = { "Kill! Kill! Kill! Kill!", "Kill! Kill! Kill!", };
		try {
			assertEquals(selfMatch(ar, Trie.SPACEANDTAB | Trie.CONDENSE), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test18() {
		String[] ar = { "The Master Poets Collection [Video Series]",
				"The Master", };
		try {
			assertEquals(
					selfMatch(ar, Trie.SPACEANDTAB | Trie.CONDENSE
							| Trie.AUTO_BOUNDARY), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test19() {
		String[] ar = { "\\Q" };
		try {
			assertEquals(selfMatch(ar, Trie.SPACEANDTAB | Trie.CONDENSE), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test20() {
		String[] ar = { "a-a", "a$a", "a0a" };
		try {
			assertEquals(selfMatch(ar, Trie.AUTO_BOUNDARY), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test21() {
		String[] ar = { "a", "b", "c" };
		try {
			assertEquals(selfMatch(ar, 0), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test22() {
		String[] ar = { "aa", "ab", "ac" };
		try {
			assertEquals(selfMatch(ar, 0), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test23() {
		String[] ar = { "aa", "ab", "ac" };
		try {
			assertEquals(selfMatch(ar, Trie.AUTO_BOUNDARY), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test24() {
		String[] ar = { "dad", "dbd", "dcd" };
		try {
			assertEquals(selfMatch(ar, Trie.AUTO_BOUNDARY), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test25() {
		String[] ar = { "-", ".", "/" };
		try {
			assertEquals(selfMatch(ar, 0), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test26() {
		String[] ar = { "+", ",", "-" };
		try {
			assertEquals(selfMatch(ar, 0), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test27() {
		String[] ar = { "-", ".", "/", "0" };
		try {
			assertEquals(selfMatch(ar, 0), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test28() {
		String[] ar = { "*", "+", ",", "-" };
		try {
			assertEquals(selfMatch(ar, 0), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test29() {
		String[] ar = { "a", "aa", "aaa", "aaaa", "aaaaaa" };
		try {
			assertEquals(selfMatch(ar, Trie.CONDENSE), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test30() {
		String[] ar = { "a", "aa", "aaa", "aaaa", "aaaaaa" };
		try {
			assertEquals(selfMatch(ar, 225), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test31() {
		String[] ar = { "xaa", "xba", "yab", "ybb" };
		try {
			assertEquals(selfMatch(ar, Trie.CONDENSE | Trie.BACKTRACKING), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test32() {
		String[] ar = { "a a a a", "a b a a", "a a a", };
		try {
			assertEquals(
					selfMatch(ar, Trie.PRESERVE_WHITESPACE | Trie.CONDENSE),
					true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test33() {
		String[] ar = { "a a a a", "a xac a a", "a xbc a a", "a yac a a",
				"a ybc a a", "a a a", };
		try {
			assertEquals(
					selfMatch(ar, Trie.PRESERVE_WHITESPACE | Trie.CONDENSE),
					true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test34() {
		String[] ar = { "a a a a", "a xac a a", "a xbc a a", "a yac a a",
				"a ybc a a", "a a a", };
		try {
			assertEquals(
					selfMatch(ar, Trie.PRESERVE_WHITESPACE | Trie.CONDENSE
							| Trie.BACKTRACKING), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test35() {
		String[] ar = { "a a a a", "a b a a", "a a a", };
		try {
			assertEquals(
					selfMatch(ar, Trie.PRESERVE_WHITESPACE | Trie.CONDENSE
							| Trie.BACKTRACKING), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test36() {
		String[] ar = { "xac", "xbc", "yac", "ybc" };
		try {
			assertEquals(selfMatch(ar, Trie.CONDENSE), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test37() {
		String[] ar = { "James while John had had had had had had had had had had had a better effect on the teacher" };
		try {
			assertEquals(selfMatch(ar, Trie.CONDENSE), true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * This collection was throwing an error at one point.
	 */
	@Test
	public void test38() {
		String[] ar = { "no return", "defensive 12 on-field",
				"roughing the kicker", "extra point", "defensive offside",
				"sacked", "recovered by", "good", "incomplete pass",
				"field goal attempt", "kicks off", "illegal formation",
				"touchdown", "defensive pass interference", "returns",
				"pass to", "no good", "rush", "roughing the passer",
				"false start", "field goal", "punts", "touchback", "return",
				"fumbles", "low block", "fumble",
				"incomplete pass intended for", "tackled by",
				"illegal block above the waist", "illegal touch Pass",
				"pass intended for", "face mask", "offensive holding",
				"illegal contact", "offensive 12 on-field", "chop block",
				"running into the kicker", "intercepted by", "missed",
				"timeout", "encroachment", "kicks", };
		try {
			assertEquals(selfMatch(ar, Trie.CONDENSE | Trie.AUTO_BOUNDARY),
					true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Testing the new no-char-classes feature.
	 */
	@Test
	public void test39() {
		String[] ar = { "bid", "bed", "bad", "bod", "bud" };
		try {
			assertEquals(selfMatch(ar, Trie.CONDENSE | Trie.NO_CHAR_CLASSES),
					true);
		} catch (TrieException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * tests whether a dictionary matches each of its terms once and only once
	 * 
	 * @param ar
	 * @param flags
	 * @return
	 * @throws TrieException
	 */
	private boolean selfMatch(String[] ar, int flags) throws TrieException {
		// clean ar of duplicates
		Map<String, Integer> set = new HashMap<String, Integer>(ar.length * 2);
		for (String s : ar)
			set.put(s, 0);
		ar = set.keySet().toArray(new String[set.size()]);
		Arrays.sort(ar);

		// create text to match
		StringBuffer b = new StringBuffer();
		for (String s : ar)
			b.append(s).append('\n');

		// create pattern
		String regex = Trie.trie(ar, flags);
		// regex = "a(?: X{1,2}+)?+";
		Pattern p = Pattern.compile(regex);
		System.out.println(p);
		Matcher m = p.matcher(b.toString().trim());
		System.out.println();

		// count matches
		while (m.find()) {
			Integer i = set.get(m.group());
			if (i == null || i == 1)
				return false;
			set.put(m.group(), 1);
		}
		for (int i : set.values())
			if (i == 0)
				return false;
		return true;
	}
}
