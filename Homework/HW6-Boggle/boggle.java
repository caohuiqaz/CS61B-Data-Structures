import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * Boggle.
 * 
 * @author Kyle Kingsbury
 * 
 */
public class Boggle {
	public static int DEFAULT_SIZE = 8; // Default dimensions of grid
	public static int MIN_PREFIX_LENGTH = 3; // Smallest number of characters
	// in a prefix tree
	public static int MIN_WORD_LENGTH = 3; // Lowest number of letters in a
	// word.
	public static int MAX_WORD_LENGTH = 64; // Highest number of letters in a
	// match.
	public static int INITIAL_WORD_TARGET_LENGTH = 7; // Reasonable standard
	// for
	// exhaustive search

	private int size; // Dimensions of grid
	private Cell[][] grid; // Boggle grid

	private Random generator = new Random();

	/**
	 * Class Cell represents a character Cell in the grid.
	 */
	private class Cell {
		boolean visited;
		char c;

		public Cell(char c) {
			this.c = c;
			visited = false;
		}

		public String toString() {
			return Character.toString(c);
		}
	}

	/**
	 * Class Dictionary contains references for checking the word status of a
	 * given target
	 */
	private static class Dictionary {
		public BinarySearchTree index;
		/**
		 * Prefix is an array of binary search trees where the tree located at
		 * element i in the array contains all substrings of length i at index 0
		 * in the dictionary file. Hence, the word 'catch' is stored in [1] as
		 * 'c', [2] as 'ca', [3] as 'cat', etc. This allows the search function
		 * to abort search paths which can never result in words.
		 */
		public BinarySearchTree[] prefix;

		/**
		 * Constructs a dictionary
		 * 
		 * @param file File to parse
		 * @throws IOException If file can't be read.
		 */
		public Dictionary(File file) throws IOException {
			index = new BinarySearchTree();
			prefix = new BinarySearchTree[MAX_WORD_LENGTH];

			// Initialize prefix BSTs
			for (int i = 0; i < prefix.length; i++) {
				prefix[i] = new BinarySearchTree();
			}

			String word;

			Scanner scanner = new Scanner(file);
			scanner.useDelimiter("[\\n]"); // Separate tokens using newlines

			// Add words to indexes
			word: while (scanner.hasNext()) {
				// Get word
				word = scanner.next();

				if (word.length() > MAX_WORD_LENGTH) {
					// Word is too long, skip
					continue word;
				}

				// Lowercase
				word = word.toLowerCase();

				// Add word to primary dictionary
				index.add(word);

				// Add word prefixes to prefix dictionaries
				for (int i = MIN_PREFIX_LENGTH; i < prefix.length && i < word.length(); i++) {
					prefix[i].add(word.substring(0, i));
				}
			}
		}
	}

	/**
	 * Creates a new boggle board of the default dimensions, filled with random
	 * characters.
	 * 
	 */
	public Boggle() {
		generator = new Random();
		size = DEFAULT_SIZE;
		grid = new Cell[size][size];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = new Cell(randomChar());
			}
		}
	}

	/**
	 * Searches the board for matches in the given Dictionary. Prints matches as
	 * they are found.
	 * 
	 * @param dictionary The dictionary to check against.
	 * @return A tree of found words.
	 */
	public BinarySearchTree findWords(Dictionary dictionary) {
		BinarySearchTree matches = new BinarySearchTree();
		StringBuffer charBuffer = new StringBuffer();

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				findWords(dictionary, matches, i, j, charBuffer);
			}
		}

		return matches;
	}

	/**
	 * Searches the board for matches in the given BinarySearchTree. Prints
	 * matches as they are found.
	 * 
	 * Note that using class Dictionary is preferred instead.
	 * 
	 * @param index The dictionary to check against.
	 * @return A tree of found words.
	 */
	public BinarySearchTree findWords(BinarySearchTree index) {
		BinarySearchTree matches = new BinarySearchTree();
		StringBuffer charBuffer = new StringBuffer();

		/*
		 * It's possible to perform an exhaustive search for all words in the
		 * board of a certain length or below. Searches for candidate words at
		 * greater depths take exponentially larger numbers of recursive calls.
		 * Hence, we search exhaustively below a certain target depth. When that
		 * search has completed, we incrementally search for longer words.
		 */

		int min_word_length = MIN_WORD_LENGTH;

		// Start with lower-length words, and then work upwards
		for (int max_word_length = INITIAL_WORD_TARGET_LENGTH; max_word_length <= MAX_WORD_LENGTH; max_word_length++) {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					findWords(index, matches, min_word_length, max_word_length,
							i, j, charBuffer);
				}
			}
			// Having completed the exhaustive pass, we set min_target_depth to
			// max_target_depth to avoid repeat word checks
			if (min_word_length == 0) {
				min_word_length = max_word_length;
			}
			min_word_length++;
		}

		return matches;
	}

	/**
	 * Recursively searches a Cell for words
	 * 
	 * @param dictionary Dictionary of valid words
	 * @param matches Words found in the board
	 * @param i Vertical index of word in board
	 * @param j Horizontal index of word in board
	 * @param charBuffer Characters this method has traversed
	 */
	private void findWords(Dictionary dictionary, BinarySearchTree matches,
			int i, int j, StringBuffer charBuffer) {

		// Whether or not this is a valid beginning to a word
		boolean valid_word_prefix = true;

		// Add this cell to character buffer
		charBuffer.append(grid[i][j].c);

		// Mark cell as visited
		grid[i][j].visited = true;

		// Set word
		String word = new String(charBuffer);

		// Word candidate
		if (charBuffer.length() >= MIN_WORD_LENGTH
				&& charBuffer.length() <= MAX_WORD_LENGTH) {
			// The number of characters we have is within the acceptable range
			// for a word.

			// Check word for word-i-ness.
			if (dictionary.index.find(word)) {
				// This is a word
				if (matches.add(word)) {
					// We haven't seen this word before
					System.out.println(matches.size() + ": " + word);
				}
			}
		}

		/*
		 * Check word for word-ability Essentially, we look to see if this word
		 * *might* be a word later by checking if it exists in a prefix tree,
		 * built from progressively larger initial substrings of words.
		 */
		if (charBuffer.length() >= MIN_PREFIX_LENGTH
				&& charBuffer.length() < MAX_WORD_LENGTH) {
			// System.out.println("Testing word " + word + " of length "
			// + charBuffer.length() + " for prefix.");
			// We can test this word to see if it's a prefix
			if (!dictionary.prefix[charBuffer.length()].find(word)) {
				// This is not a valid word prefix
				// System.out.println("Not a potential word");
				valid_word_prefix = false;
			}
		}

		if (valid_word_prefix && charBuffer.length() < MAX_WORD_LENGTH) {
			// This is a valid word prefix (will be a word later), and adding
			// another letter will not exceed the target maximum word length.
			// Check recursively.
			checkI: for (int iTarget = i - 1; iTarget <= i + 1; iTarget++) {
				if (iTarget < 0 || iTarget >= size) {
					// index iTarget is out of bounds
					continue checkI;
				}
				checkJ: for (int jTarget = j - 1; jTarget <= j + 1; jTarget++) {
					if (jTarget < 0 || jTarget >= size) {
						// index jTarget is out of bounds
						continue checkJ;
					}
					if (grid[iTarget][jTarget].visited == false) {
						// Target Cell has not been visited
						findWords(dictionary, matches, iTarget, jTarget, charBuffer);
					}
				}
			}
		}
		// Since we're returning control, this Cell is no longer visited
		grid[i][j].visited = false;
		// Remove Cell from character stack
		charBuffer.deleteCharAt(charBuffer.length() - 1);
	}

	/**
	 * Recursively searches a Cell for words
	 * 
	 * Note that using class Dictionary is preferred instead.
	 * 
	 * @param index BinarySearchTree of valid words
	 * @param matches Words found in the board
	 * @param min_word_length Minimum size of a word candidate
	 * @param max_word_length Maximum size of a word candidate
	 * @param i Vertical index of word in board
	 * @param j Horizontal index of word in board
	 * @param charBuffer Characters this method has traversed
	 */
	private void findWords(BinarySearchTree index, BinarySearchTree matches,
			int min_word_length, int max_word_length, int i, int j,
			StringBuffer charBuffer) {

		// Add this Cell to character buffer
		charBuffer.append(grid[i][j].c);

		// Mark Cell as visited
		grid[i][j].visited = true;

		// Word candidate
		if (charBuffer.length() >= min_word_length
				&& charBuffer.length() <= max_word_length) {
			// The number of characters we have is within the acceptable range
			// for a word.

			// Get current word
			String word = new String(charBuffer);

			// Check word for word-i-ness.
			if (index.find(word)) {
				// This is a word
				if (matches.add(word)) {
					System.out.println(matches.size() + ": " + word);
				}
			}
		}

		if (charBuffer.length() < max_word_length) {
			// Adding another letter will not exceed the target maximum word
			// length. Check recursively.
			checkI: for (int iTarget = i - 1; iTarget <= i + 1; iTarget++) {
				if (iTarget < 0 || iTarget >= size) {
					// index iTarget is out of bounds
					continue checkI;
				}
				checkJ: for (int jTarget = j - 1; jTarget <= j + 1; jTarget++) {
					if (jTarget < 0 || jTarget >= size) {
						// index jTarget is out of bounds
						continue checkJ;
					}
					if (grid[iTarget][jTarget].visited == false) {
						// Target Cell has not been visited
						findWords(index, matches, min_word_length,
								max_word_length, iTarget, jTarget, charBuffer);
					}
				}
			}
		}
		// Since we're returning control, this Cell is no longer visited
		grid[i][j].visited = false;
		// Remove Cell from character stack
		charBuffer.deleteCharAt(charBuffer.length() - 1);
	}

	/**
	 * Returns a human-readable representation of the board.
	 */
	public String toString() {
		String str = "|";
		for (int j = 0; j < size * 4 - 1; j++) {
			str = str + '-';
		}
		str = str + "|\n";

		for (int i = 0; i < size; i++) {
			str = str + "| ";
			for (int j = 0; j < size; j++) {
				str = str + grid[i][j].c;
				str = str + " | ";
			}
			str = str + "\n|";
			for (int j = 0; j < size * 4 - 1; j++) {
				str = str + '-';
			}
			str = str + "|\n";
		}
		return str;
	}

	// Main methods

	public static void main(String[] args) {
		try {
			System.out.println("Done.");
			// Generate boggle board
			Boggle board = new Boggle();
			// Display board
			System.out.println(board);

			// Get dictionary
			System.out.print("Building dictionary... ");
			long startIndexTime = System.currentTimeMillis();
			//BinarySearchTree index = buildIndex(new File(args[0]));
			Dictionary dictionary = new Dictionary(new File(args[0]));
			System.out.println("Done.");
			
			// Search board with dictionary
			long startSearchTime = System.currentTimeMillis();
			//board.findWords(index);
			board.findWords(dictionary);
			long finishTime = System.currentTimeMillis();
			
			System.out.println("Indexed dictionary in " + ((startSearchTime - startIndexTime) / 1000.0) + " seconds.");
			System.out.println("Searched board in " + ((finishTime - startSearchTime) / 1000.0) + " seconds.");
			System.out.println("Total running time: " + ((finishTime - startIndexTime) / 1000.0) + " seconds.");
			
		} catch (IOException e) {
			System.out.println("Not a dictionary file: " + args[0]);
			return;
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Usage: Boggle [dictionary]");
		}
	}

	// Helper methods

	/**
	 * Builds an index file from disk Note that using class Dictionary is
	 * preferred istead.
	 */
	private static BinarySearchTree buildIndex(File file) throws IOException {
		BinarySearchTree index = new BinarySearchTree();

		/*
		 * With help from Professor David Liben-Nowell, CS127
		 */
		Scanner scanner = new Scanner(file);
		scanner.useDelimiter("[\\n]"); // Separate tokens using newlines

		while (scanner.hasNext()) {
			index.add(scanner.next().toLowerCase());
		}
		return index;
	}

	/**
	 * Returns a random lowercase character from a to z roughly matching the
	 * frequency distribution of english text.
	 */
	private char randomChar() {
		float i = generator.nextInt(1000000);
		i = i / 1000000;
		if (i < .08167) {
			return 'a';
		}
		if (i < .09659) {
			return 'b';
		}
		if (i < .12441) {
			return 'c';
		}
		if (i < .16694) {
			return 'd';
		}
		if (i < .29396) {
			return 'e';
		}
		if (i < .31624) {
			return 'f';
		}
		if (i < .33639) {
			return 'g';
		}
		if (i < .39733) {
			return 'h';
		}
		if (i < .46699) {
			return 'i';
		}
		if (i < .46852) {
			return 'j';
		}
		if (i < .47624) {
			return 'k';
		}
		if (i < .51649) {
			return 'l';
		}
		if (i < .54055) {
			return 'm';
		}
		if (i < .60804) {
			return 'n';
		}
		if (i < .68311) {
			return 'o';
		}
		if (i < .70240) {
			return 'p';
		}
		if (i < .70335) {
			return 'q';
		}
		if (i < .76322) {
			return 'r';
		}
		if (i < .82649) {
			return 's';
		}
		if (i < .91705) {
			return 't';
		}
		if (i < .94463) {
			return 'u';
		}
		if (i < .95441) {
			return 'v';
		}
		if (i < .97801) {
			return 'w';
		}
		if (i < .97951) {
			return 'x';
		}
		if (i < .99925) {
			return 'y';
		}
		if (i < 1) {
			return 'z';
		} else {
			// Failsafe
			return 'e';
		}
	}
}