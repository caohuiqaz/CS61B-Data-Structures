public class ArrayDeque<Item> {
	private Item[] items;
	private int size = 8;
	private int memreq = 8;
	private static int RFACTOR = 2;
	private static double UFACTOR = 0.25;
    public ArrayDeque() {
    	size = 0;
    	items = (Item[]) new Object[8];
    }
    private void resize(int capacity) {
    	Item[] a = (Item[]) new Object[capacity];
    	System.arraycopy(items, 0, a, 0, size);
    	items = a;    	
    }
	private void decsize(int capacity)
	{
		Item[] a = (Item[]) new Object[capacity];
    	System.arraycopy(items, 0, a, 0, size);
    	items = a;
		memreq = size;
	}
	public void addFirst(Item x)
	{
		if (size == items.length) {
    		resize(size * RFACTOR);
    	}
		Item[] a = (Item[]) new Object[size];
    	System.arraycopy(items, 1, a, 0, size-1);
    	items = a;
		items[0] = x;
		memreq++;
		
	}
    public void addLast(Item x) {    	
    	if (size == items.length) {
    		resize(size * RFACTOR);
    	}

    	items[size] = x;
    	size = size + 1;
		memreq++;
    }
    public Item getBack() {
    	int lastActualItemIndex = size - 1;
    	return items[lastActualItemIndex];
    }
    public Item get(int i) {
        return items[i];
    }
    public int size() {
        return size;        
    }
    public Item removeLast() {
		Item itemToReturn = getBack();
		items[size - 1] = null;
		size = size - 1;
		if(size/memreq<UFACTOR)
		{
			decsize(size);
		}
		return itemToReturn;
    }
	public Item removeFirst()
	{
		Item itemToReturn = items[0];
		Item[] a = (Item[]) new Object[size];
    	System.arraycopy(items, 1, a, 0, size-1);
    	items = a;
		size--;
		if(size/memreq<UFACTOR)
		{
			decsize(size);
		}
		return itemToReturn;
	}
	public boolean isEmpty()
	{
		if(size==0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public void printDeque()
	{
		int i;
		for(i=0;i<size;i++)
		{
			System.out.println(items[i]);
		}
	}
} 