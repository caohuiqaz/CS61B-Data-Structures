public class OffByN implements CharacterComparator
{
	int d;
	public OffByN(int n)
	{
		this.d = n;
	}
	@Override
	public boolean equalChars(char x, char y)
	{
		int difference = Math.abs(x-y);
		if(difference == this.d)
			return true;
		else
			return false;
	}
}