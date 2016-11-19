public class LinkedListDeque<Item> 
{
	public class Node 
	{
		public Item item;
		public Node prev;
		public Node next;
		public Node(Item i, Node j, Node k) 
		{
			item = i;
			prev = j;
			next = k;
		}
	}
	private Node sentinel;
	private int size;
	public LinkedListDeque() 
	{
		size = 0;
		sentinel = new Node(null, null, null);
		sentinel.next = sentinel;
		sentinel.prev = sentinel;
	}
	public LinkedListDeque(Item p) 
	{
		size = 1;
		sentinel = new Node(null, null, null);
		sentinel.next = new Node(p, sentinel, sentinel);
		sentinel.prev = sentinel.next;
	}
	public void addFirst(Item x) 
	{
		size += 1;
		Node newNode = new Node(x, sentinel, sentinel.next);
		sentinel.next.prev = newNode;
		sentinel.next = newNode;
	}
	public void addLast(Item x) 
	{
		size += 1;
		Node newNode = new Node(x, sentinel.prev, sentinel);
		sentinel.prev.next = newNode;
		sentinel.prev = newNode;
	}
	public boolean isEmpty() 
	{
		if(sentinel.next.equals(sentinel)) 
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	public int size() 
	{
		return size;
	}
	public void printDeque() 
	{
		Node p = sentinel.next;
		while (!p.equals(sentinel)) 
		{
			System.out.println(p.item + " ");
			p = p.next;
		}
	}
	public Item removeFirst() 
	{
		size -= 1;
		if(isEmpty()) 
		{
			return null;
		}
		else
		{
			Node del = sentinel.next;
		    Item x = del.item;
		    sentinel.next = del.next;
		    del.next.prev =sentinel;
		    del.next = del.prev = null;
		    return x;
		}		
	}
	public Item removeLast() 
	{
		size -= 1;
		if(isEmpty()) 
		{
			return null;
		}
		else
		{
			Node del = sentinel.prev;
		    Item x = del.item;
		    sentinel.prev = del.prev;
		    del.prev.next =sentinel;
		    del.next = del.prev = null;
		    return x;
		}
	}
	public Item get(int index) 
	{
		if (isEmpty()) 
		{
			return null;
		}
		else 
		{
			Node p = sentinel.next;
			for (int i = 0; i < index; i++) 
			{
				p = p.next;
			}
			return p.item;
		}
	}
	private Item getRecursive(Node p, int index) 
	{
		if (index == 0) 
		{
			return p.item;
		}
		else 
		{
			return getRecursive(p.next, index-1);
		}
	}
	public Item getRecursive(int index) 
	{
	    if (isEmpty()) 
		{
			return null;
		}
		else 
		{	
			Node p = sentinel.next;
			return getRecursive(p, index);
		}
	}
}
