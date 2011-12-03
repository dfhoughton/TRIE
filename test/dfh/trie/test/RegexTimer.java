package dfh.trie.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dfh.trie.Trie;
import dfh.trie.TrieException;

/**
 * For testing the effect of prospective regex optimizations.
 * 
 * @author David Houghton
 * 
 */
public class RegexTimer {

	private static final char BASE = ' ';
	private static final char CEILING = '~';
	private static final int MAX = CEILING - BASE + 1;

	/**
	 * @param args
	 * @throws TrieException
	 */
	public static void main(String[] args) throws TrieException {
		// must be greater than 1 or you get no stats
		int iterations = 3;
		final int configuration = Trie.AUTO_BOUNDARY | Trie.CASEINSENSITIVE
				| Trie.CONDENSE;
		final int lines = 500;
		final int range = 26;
		final int length = 16;
		final int variation = 5;
		System.out
				.printf("test:%n\t%d iterations%n\t%d lines%n\tcharacter range: %d characters%n\taverage word length: %d%n\tvariation in length: +/-%d (uniform distribution)%n",
						iterations, lines, range, length, variation);
		List<Double> results = new ArrayList<Double>();
		for (int i = 0; i < iterations; i++)
			results.add(test(configuration, lines, range, length, variation));
		System.out.println("discarding first result");
		results.remove(0);
		double average = 0;
		for (double d : results)
			average += d;
		average /= iterations;
		double stddev = 0;
		for (double d : results) {
			double delta = d - average;
			stddev += delta * delta;
		}
		stddev = Math.sqrt(stddev / iterations);
		Collections.sort(results);
		double median = 0;
		if (iterations % 2 == 1)
			median = results.get((iterations - 1) / 2);
		else {
			int m2 = iterations / 2;
			int m1 = m2 - 1;
			median = (results.get(m1) + results.get(m2)) / 2;
		}
		System.out.printf("avg: %.2f; med: %.2f; stddev: %.2f%n", average,
				median, stddev);
	}

	public static double test(final int configuration, final int lines,
			final int range, final int length, final int variation)
			throws TrieException {
		String[] ar = new String[lines];
		int max = range < MAX ? range : MAX;
		for (int i = 0; i < lines; i++)
			ar[i] = generateWord(max, length, variation);
		long time = System.currentTimeMillis();
		Trie.trie(ar, configuration);
		double seconds = (System.currentTimeMillis() - time) / 1000.0;
		System.out.printf("%.2f seconds%n", seconds);
		return seconds;
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
