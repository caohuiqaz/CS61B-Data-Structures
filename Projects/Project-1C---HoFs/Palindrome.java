public class Palindrome
{
	public static Deque<Character> wordToDeque(String word)
	{
		Deque <Character> deque = new LinkedListDequeSolution <Character>();
		int i;
		for(i=0;i<word.length();i++)
		{
			char x = word.charAt(i);
			deque.addLast(x);
		}
		return deque;
	}
	 private static boolean isPalindrome(Deque<Character> x) {
        if (x.size() == 0 || x.size() == 1) {
            return true;
        }

        char first = x.removeFirst();
        char last = x.removeLast();

        boolean isPal = first == last;
        if (isPal) {
            return isPalindrome(x) && isPal;
        } else {
            return false;
        }
    }

    public static boolean isPalindrome(String word) {
        word = word.toLowerCase();      // Ideally this should be here. But not mentioned in spec
        Deque<Character> deque = wordToDeque(word);
        return isPalindrome(deque);
    }	
	public static boolean isPalindrome(Deque <Character> x , CharacterComparator cc)
	{
		if(x.size() == 0 || x.size() == 1)
			return true;
		
		char first = x.removeFirst();
		char last = x.removeLast();
		
		boolean Pal = first == last;
		if(Pal)
		{
			return isPalindrome(x,cc) && Pal;
		}
		else
			return false;
	}
	
	public static boolean isPalindrome(String word, CharacterComparator cc)
	{
		Deque <Character> deque = wordToDeque(word);
		return isPalindrome(deque,cc);
	}
}