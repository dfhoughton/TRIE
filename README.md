TRIE
====

David F. Houghton
14 March, 2011

The code in this project will convert a list of phrases into a [trie][] regex for faster pattern matching in Java. For example, it converts

    "kit", "cat", "rat", "rot"

into

    (?i:\b(?>r[ao]|ki|ca)t\b)

To be perfectly accurate, the regex created is not a "trie", but like a trie it requires no backtracking, so like a trie it will match in linear time proportional to the length of the typical words of which it is composed.

Full Documentation
------------------

For complete documentation, including code samples and API, see [my site][docs].

License
-------
This software is distributed under the terms of the FSF Lesser Gnu Public License (see lgpl.txt).

[trie]: http://en.wikipedia.org/wiki/Trie
[docs]: http://dfhoughton.org/trie/
