package dfh.trie.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dfh.trie.Trie;
import dfh.trie.TrieException;

/**
 * Combs through a huge pile of data looking for problem cases to add to set of
 * Trie unit tests.
 * 
 * @author houghton
 */
public class RegexBugFinder {
	enum states {
		searching, firstHalf, secondHalf, missed,
	};

	private static states state = states.searching;

	/**
	 * Maximum number of items to try to compile into a regex at one time.
	 */
	private static final int MAX_ITEMS = 100000;

	public static void main(String[] args) throws IOException {

		String[] lines = null;
		Map<String, Integer> set = null;
		int chars = -1;
		if (args.length == 0) {
			int size = 1000000;
			int length = 32;
			chars = (32 + 1) * 10000 + 4;
			System.out.println("generating " + size + " lines");
			set = new HashMap<String, Integer>(size);
			for (int i = 0; i < size; i++) {
				StringBuffer b = new StringBuffer(length);
				for (int j = 0; j < length; j++) {
					char c = (char) (Math.random() * 127);
					if (Character.isWhitespace(c) || c < 33)
						c = ' ';
					b.append(c);
				}
				set.put(b.toString().trim(), 0);
			}
			lines = set.keySet().toArray(new String[set.size()]);
			System.out.println(lines.length + " good lines");
		} else {
			System.out.println("counting lines in " + args[0]);
			File f = new File(args[0]);
			Process p = Runtime.getRuntime().exec("wc -l " + args[0]);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = reader.readLine();
			line = line.replaceFirst(" .*", "");
			System.out.println(line + " lines available");
			p = null;
			reader = null;

			System.out.println("reading in " + args[0]
					+ " and eliminating empty lines");
			set = new HashMap<String, Integer>(Integer.parseInt(line));
			reader = new BufferedReader(new FileReader(f));
			Pattern nonWhitespacePattern = Pattern.compile("\\S");
			chars = 4;
			while ((line = reader.readLine()) != null) {
				if (nonWhitespacePattern.matcher(line).find()) {
					line = line.trim().replaceAll("\\p{javaWhitespace}++", " ");
					set.put(line, 0);
					chars += line.length() + 7;
				}
			}
			lines = set.keySet().toArray(new String[set.size()]);
			System.out.println(lines.length + " good lines");
		}
		set = null;

		Arrays.sort(lines, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return -o1.compareTo(o2);
			}
		});

		System.out.println("entering search loop");
		int offset = 0, size = Math.min(2 * MAX_ITEMS, lines.length);
		int errorOffset = -1, errorSize = -1;
		String[] ar = null;
		MAIN: while (true) {
			switch (state) {
			case searching:
				if (size < 3)
					break MAIN;
				offset += size;
				if (size + offset > lines.length)
					size = lines.length - offset;
				if (size == 0)
					break MAIN;
				break;
			case firstHalf:
				if (size < 3)
					break MAIN;
				errorOffset = offset;
				errorSize = size;
				size = (int) Math.ceil(size / 2.0);
				break;
			case secondHalf:
				offset += size;
				break;
			case missed:
				if (size < 3)
					break MAIN;
				offset -= size / 2;
				if (offset < 0)
					break MAIN;
				break;
			}

			System.out.println();
			System.out.println(state + " " + offset + " " + size);

			ar = Arrays.copyOfRange(lines, offset,
					Math.min(lines.length, offset + size));

			boolean foundError = seekErrors(chars, ar);

			// change states based on whether error was found
			switch (state) {
			case searching:
				if (foundError)
					state = states.firstHalf;
				break;
			case firstHalf:
				if (!foundError)
					state = states.secondHalf;
				break;
			case secondHalf:
				if (foundError)
					state = states.firstHalf;
				else
					state = states.missed;
				break;
			case missed:
				if (foundError)
					state = states.firstHalf;
				else
					break MAIN;
			}

		}

		System.out.println();
		if (errorOffset == -1) {
			System.out.println("found no problems");
			return;
		}
		ar = Arrays.copyOfRange(lines, errorOffset, errorOffset + errorSize);
		if (ar.length > 2)
			System.out.println("current list very long; will attempt to cull");
		DELETE: while (ar.length > 2) {
			System.out.println(ar.length + " suspects");
			for (int i = 2; i <= ar.length; i++) {
				int segment = ar.length / i;
				for (int start = 0; start < ar.length; start += segment) {
					List<String> list = new ArrayList<String>(ar.length);
					int end = start + segment;
					for (int j = 0; j < ar.length; j++) {
						if (j < start || j >= end)
							list.add(ar[j]);
					}
					String[] ar2 = list.toArray(new String[list.size()]);
					if (seekErrors(chars, ar2)) {
						System.out.println("remaining array: length: "
								+ ar2.length + "; segment: " + segment
								+ "; start: " + start + "; end: " + end
								+ "; old ar length: " + ar.length);
						ar = ar2;
						continue DELETE;
					}
				}
			}
			break;
		}
		for (String s : ar)
			System.out.println('"' + s + "\",");
	}

	private static boolean seekErrors(int chars, String[] ar) {
		boolean foundError = false;
		Map<String, Integer> keepers = new HashMap<String, Integer>(ar.length);
		for (String s : ar)
			keepers.put(s.trim().replaceAll("\\s+", " "), 0);
		ar = keepers.keySet().toArray(new String[keepers.size()]);
		System.out.println("should find " + ar.length + " matches");
		try {
			// non-backtracking regex
			long characters = 0;
			for (String s : ar)
				characters += s.length();
			System.out.printf("%d characters%n", characters);
			System.out.println("creating compact, non-backtracking regex");
			String regex = makeRegex(ar, false);

			System.out.println("compiling compact, non-backtracking regex");
			Pattern p1 = compileRegex(regex);

			regex = null;
			System.gc();

			System.out.println("creating text to match");
			long time = System.currentTimeMillis();
			StringBuffer b = new StringBuffer(chars);
			for (String s : ar)
				b.append(s).append('\n');
			long currTime = System.currentTimeMillis();
			System.out.printf("%.3f seconds%n", (currTime - time) / 1000.0);

			ar = null;
			System.gc();

			System.out
					.println("counting matches with compact, non-backtracking regex");
			seekErrors(foundError, keepers, p1, b);

			p1 = null;
			System.gc();

		} catch (Throwable t) {
			System.out.println("throwable caught: "
					+ t.getClass().getSimpleName());
			foundError = true;
		}
		return foundError;
	}

	private static String makeRegex(String[] ar, boolean backtrack)
			throws TrieException {
		long time = System.currentTimeMillis();
		int flags = Trie.AUTO_BOUNDARY | Trie.SPACEANDTAB;
		if (backtrack)
			flags |= Trie.BACKTRACKING;
		String regex = Trie.trie(ar, flags);
		long currTime = System.currentTimeMillis();
		System.out.printf("%.3f seconds%n", (currTime - time) / 1000.0);
		return regex;
	}

	private static Pattern compileRegex(String regex) {
		long time;
		long currTime;
		time = System.currentTimeMillis();
		Pattern p1 = Pattern.compile(regex);
		currTime = System.currentTimeMillis();
		System.out.printf("%d characters%n%.3f seconds%n", regex.length(),
				(currTime - time) / 1000.0);

		regex = null;
		System.gc();
		return p1;
	}

	private static void seekErrors(boolean foundError,
			Map<String, Integer> keepers, Pattern p1, StringBuffer b) {
		long time;
		long currTime;
		int count;
		count = 0;
		time = System.currentTimeMillis();
		Matcher m = p1.matcher(b);
		while (m.find()) {
			String s = m.group();
			Integer i = keepers.get(s);
			if (i == null || i == 1) {
				foundError = true;
				break;
			} else
				keepers.put(s, 1);
			count++;
		}
		currTime = System.currentTimeMillis();
		System.out.printf("%.3f seconds%n", (currTime - time) / 1000.0);
		System.out.print(count + " matches");
		System.out.println(foundError ? " before error found"
				: " and no error found");
		for (int i : keepers.values()) {
			if (i == 0) {
				foundError = true;
				break;
			}
		}
		if (foundError)
			System.out.println("failed to match expression");
	}
}
