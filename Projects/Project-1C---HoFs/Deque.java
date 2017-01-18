public interface Deque <Item>
{
	public void printDeque();
	public Item getRecursive(int i);
	public Item get(int i);
	public Item removeFirst();
	public Item removeLast();
	public void addFirst(Item x);
    public void addLast(Item x);
    public boolean isEmpty();
    public int size();	
}