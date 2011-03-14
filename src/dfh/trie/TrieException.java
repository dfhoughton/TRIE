package dfh.trie;

/**
 * Marks predictable exceptions in Trie package.
 * 
 * @author David Houghton
 * 
 */
public class TrieException extends Exception {
	private static final long serialVersionUID = 1L;

	public TrieException(String message) {
		super(message);
	}
}
