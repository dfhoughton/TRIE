package dfh.trie.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dfh.trie.Trie;
import dfh.trie.TrieException;

public class RegexBenchmark {
	private static long time = System.currentTimeMillis();

	public static void main(String[] args) throws IOException, TrieException {

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
		time();

		System.out.println("reading in " + args[0]
				+ " and eliminating empty lines");
		Set<String> set = new HashSet<String>(Integer.parseInt(line));
		reader = new BufferedReader(new FileReader(f));
		Pattern nonWhitespacePattern = Pattern.compile("\\S");
		int chars = 4;
		while ((line = reader.readLine()) != null) {
			if (nonWhitespacePattern.matcher(line).find()) {
				// one wouldn't think we'd be getting any newlines, but it seems
				// we are
				line = line.trim().replaceAll("\\p{javaWhitespace}++", " ");
				set.add(line);
				chars += line.length() + 7;
			}
		}
		reader.close();
		String[] lines = set.toArray(new String[set.size()]);
		set = null;
		System.out.println(lines.length + " good lines");
		time();

		System.out.println("sorting array");
		Arrays.sort(lines, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return -o1.compareTo(o2);
			}
		});
		time();

		System.out.println("creating compact regex");
		String regex = Trie.trie(lines, Trie.AUTO_BOUNDARY | Trie.SPACEANDTAB);
		System.out.println(regex.length() + " characters in regex");
		time();

		System.out.println("compiling compact regex");
		Pattern p1 = Pattern.compile(regex);
		time();

		regex = null;
		System.gc();

		System.out.println("creating crude regex");
		StringBuffer b = new StringBuffer(chars);
		b.append("(?:");
		for (String s : lines)
			b.append("\\Q").append(s).append("\\E|");
		b.setCharAt(b.length() - 1, ')');
		System.out.println(b.length() + " characters in regex");
		time();

		System.out.println("compiling crude regex");
		Pattern p2 = Pattern.compile(b.toString());
		time();

		System.out.println("creating text to match");
		b = new StringBuffer(b.length());
		int count = 0;
		for (String s : lines) {
			b.append(s).append('\n');
			// if (++count == 10000)
			// break;
		}
		lines = null;
		time();

		System.out.println("counting matches with compact regex");
		count = 0;
		Matcher m = p1.matcher(b);
		while (m.find())
			count++;
		System.out.println(count + " matches");
		time();

		p1 = null;
		m = null;
		System.gc();

		System.out.println("counting matches with crude regex");
		count = 0;
		m = p2.matcher(b);
		while (m.find())
			count++;
		System.out.println(count + " matches");
		time();
	}

	private static void time() {
		System.out.printf("time elapsed: %.2f seconds%n%n",
				(System.currentTimeMillis() - time) / 1000.0);
		System.gc();
		time = System.currentTimeMillis();
	}
}
