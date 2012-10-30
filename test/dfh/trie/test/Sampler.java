package dfh.trie.test;

import static dfh.trie.Trie.AUTO_BOUNDARY;
import static dfh.trie.Trie.BACKTRACKING;
import static dfh.trie.Trie.CASEINSENSITIVE;
import static dfh.trie.Trie.CONDENSE;
import static dfh.trie.Trie.NO_CHAR_CLASSES;
import static dfh.trie.Trie.PRESERVE_WHITESPACE;
import static dfh.trie.Trie.trie;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dfh.trie.Trie;

/**
 * Constructing this to illustrate the function of the different configuration
 * constants.
 * <p>
 * 
 * @author David F. Houghton - Oct 30, 2012
 * 
 */
class Sampler {

	static String[] words = { 
		"cat", 
		"cot", 
		"dog", 
		"Dig", 
		"elephant",
		"eeeeeeeeek", 
		"elevator shaft" 
	};

	static String sentence = "The categorical cottager dogged the elephant "
			+ "dig's elevator   shaft while shouting 'eeeeeeeeeeeeeeek!'";

	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException {

		test(0);
		test(AUTO_BOUNDARY);
		test(CONDENSE);
		test(BACKTRACKING);
		test(CASEINSENSITIVE);
		test(PRESERVE_WHITESPACE);
		test(NO_CHAR_CLASSES);
		test(AUTO_BOUNDARY | CONDENSE | CASEINSENSITIVE);
		String re = Trie.trie(new String[] { "c€t", "d€g" },
				Trie.CASEINSENSITIVE).replaceAll("€", "[aeiou]");
		System.out.println(re);
	}

	static void test(int options) throws IllegalArgumentException,
			IllegalAccessException {
		String re = trie(words, options);
		Pattern p = Pattern.compile(re);
		Matcher m = p.matcher(sentence);
		describeOptions(options);
		System.out.println(re);
		while (m.find())
			System.out.printf("  %s%n", m.group());
		System.out.println();
	}

	static void describeOptions(int options) throws IllegalAccessException {
		boolean first = true;
		for (Field f : Trie.class.getFields()) {
			if (f.getType().equals(int.class)) {
				int flag = f.getInt(null);
				if ((options & flag) != flag)
					continue;
				if (first)
					first = false;
				else
					System.out.print(" | ");
				System.out.print(f.getName());
			}
		}
		if (!first)
			System.out.println();
	}

}
