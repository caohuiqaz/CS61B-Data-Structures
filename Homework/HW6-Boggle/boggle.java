import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
public class Boggle {
	public static int DEFAULT_SIZE = 8; 
	public static int MIN_PREFIX_LENGTH = 3;
	
	public static int MIN_WORD_LENGTH = 3; 
	public static int MAX_WORD_LENGTH = 64; 
	public static int INITIAL_WORD_TARGET_LENGTH = 7; 
	private int size; 
	private Cell[][] grid; 

	private Random generator = new Random();

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

	private static class Dictionary {
		public BinarySearchTree index;
		
		public BinarySearchTree[] prefix;

		public Dictionary(File file) throws IOException {
			index = new BinarySearchTree();
			prefix = new BinarySearchTree[MAX_WORD_LENGTH];

			for (int i = 0; i < prefix.length; i++) {
				prefix[i] = new BinarySearchTree();
			}

			String word;

			Scanner scanner = new Scanner(file);
			scanner.useDelimiter("[\\n]"); 

			word: while (scanner.hasNext()) {
				word = scanner.next();

				if (word.length() > MAX_WORD_LENGTH) {
					continue word;
				}

				word = word.toLowerCase();

				index.add(word);

				for (int i = MIN_PREFIX_LENGTH; i < prefix.length && i < word.length(); i++) {
					prefix[i].add(word.substring(0, i));
				}
			}
		}
	}

	
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

	
	public BinarySearchTree findWords(BinarySearchTree index) {
		BinarySearchTree matches = new BinarySearchTree();
		StringBuffer charBuffer = new StringBuffer();

		
		int min_word_length = MIN_WORD_LENGTH;

		for (int max_word_length = INITIAL_WORD_TARGET_LENGTH; max_word_length <= MAX_WORD_LENGTH; max_word_length++) {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					findWords(index, matches, min_word_length, max_word_length,
							i, j, charBuffer);
				}
			}
			if (min_word_length == 0) {
				min_word_length = max_word_length;
			}
			min_word_length++;
		}

		return matches;
	}

	
	private void findWords(Dictionary dictionary, BinarySearchTree matches,
			int i, int j, StringBuffer charBuffer) {

		boolean valid_word_prefix = true;

		charBuffer.append(grid[i][j].c);

		grid[i][j].visited = true;

		String word = new String(charBuffer);

		if (charBuffer.length() >= MIN_WORD_LENGTH
				&& charBuffer.length() <= MAX_WORD_LENGTH) {
			if (dictionary.index.find(word)) {
				if (matches.add(word)) {
					System.out.println(matches.size() + ": " + word);
				}
			}
		}

		
		if (charBuffer.length() >= MIN_PREFIX_LENGTH
				&& charBuffer.length() < MAX_WORD_LENGTH) {
			if (!dictionary.prefix[charBuffer.length()].find(word)) {
				valid_word_prefix = false;
			}
		}

		if (valid_word_prefix && charBuffer.length() < MAX_WORD_LENGTH) {
			checkI: for (int iTarget = i - 1; iTarget <= i + 1; iTarget++) {
				if (iTarget < 0 || iTarget >= size) {
					continue checkI;
				}
				checkJ: for (int jTarget = j - 1; jTarget <= j + 1; jTarget++) {
					if (jTarget < 0 || jTarget >= size) {
						continue checkJ;
					}
					if (grid[iTarget][jTarget].visited == false) {
						findWords(dictionary, matches, iTarget, jTarget, charBuffer);
					}
				}
			}
		}
		grid[i][j].visited = false;
		charBuffer.deleteCharAt(charBuffer.length() - 1);
	}

	
	private void findWords(BinarySearchTree index, BinarySearchTree matches,
			int min_word_length, int max_word_length, int i, int j,
			StringBuffer charBuffer) {

		charBuffer.append(grid[i][j].c);

		grid[i][j].visited = true;

		if (charBuffer.length() >= min_word_length
				&& charBuffer.length() <= max_word_length) {
			String word = new String(charBuffer);

			if (index.find(word)) {
				if (matches.add(word)) {
					System.out.println(matches.size() + ": " + word);
				}
			}
		}

		if (charBuffer.length() < max_word_length) {
			checkI: for (int iTarget = i - 1; iTarget <= i + 1; iTarget++) {
				if (iTarget < 0 || iTarget >= size) {
					continue checkI;
				}
				checkJ: for (int jTarget = j - 1; jTarget <= j + 1; jTarget++) {
					if (jTarget < 0 || jTarget >= size) {
						continue checkJ;
					}
					if (grid[iTarget][jTarget].visited == false) {
						findWords(index, matches, min_word_length,
								max_word_length, iTarget, jTarget, charBuffer);
					}
				}
			}
		}
		grid[i][j].visited = false;
		charBuffer.deleteCharAt(charBuffer.length() - 1);
	}

	
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

	

	public static void main(String[] args) {
		try {
			System.out.println("Done.");
			Boggle board = new Boggle();
			System.out.println(board);
			System.out.print("Building dictionary... ");
			long startIndexTime = System.currentTimeMillis();
			Dictionary dictionary = new Dictionary(new File(args[0]));
			System.out.println("Done.");
			
			long startSearchTime = System.currentTimeMillis();
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

	private static BinarySearchTree buildIndex(File file) throws IOException {
		BinarySearchTree index = new BinarySearchTree();

		
		Scanner scanner = new Scanner(file);
		scanner.useDelimiter("[\\n]"); // Separate tokens using newlines

		while (scanner.hasNext()) {
			index.add(scanner.next().toLowerCase());
		}
		return index;
	}

	
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
			
			return 'e';
		}
	}
}