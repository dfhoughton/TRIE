package dfh.trie.test;

import dfh.trie.Trie;
import dfh.trie.TrieException;

/**
 * For profiling {@link Trie}.
 * 
 * @author David Houghton
 * 
 */
public class RegexSingleCompilation {

	private static final char BASE = ' ';
	private static final char CEILING = '~';
	private static final int MAX = CEILING - BASE + 1;

	/**
	 * @param args
	 * @throws TrieException
	 */
	public static void main(String[] args) throws TrieException {
		final int configuration = Trie.AUTO_BOUNDARY | Trie.CASEINSENSITIVE
				| Trie.CONDENSE;
		final int lines = 1000;
		final int range = 26;
		final int length = 16;
		final int variation = 5;
		System.out
				.printf("test:%n\t%d lines%n\tcharacter range: %d characters%n\taverage word length: %d%n\tvariation in length: +/-%d (uniform distribution)%n",
						lines, range, length, variation);
		test(configuration, lines, range, length, variation);
		System.out.println("done");
	}

	public static void test(final int configuration, final int lines,
			final int range, final int length, final int variation)
			throws TrieException {
		String[] ar = new String[lines];
		int max = range < MAX ? range : MAX;
		for (int i = 0; i < lines; i++)
			ar[i] = generateWord(max, length, variation);
		Trie.trie(ar, configuration);
	}

	private static String generateWord(final int max, final int length,
			final int variation) {
		final int lim = (int) (length + (Math.random() * variation * 2 - variation));
		char[] chars = new char[lim];
		for (int i = 0; i < lim; i++)
			chars[i] = getChar(max);
		return new String(chars);
	}

	private static char getChar(final int max) {
		int rand = (int) (Math.random() * max);
		return (char) (BASE + rand);
	}
}
