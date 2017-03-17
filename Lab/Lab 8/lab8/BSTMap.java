package lab8;

import java.security.key;
import java.util.Iterator;
import java.util.Set;

public class BSTMap <K extends Comparable <K>, V> implements Map61B<K,V>
{
	private Node root;
	private int number_of_items;
	public BSTMap()
	{
		root = null;
		number_of_items = 0;
	}
	public void clear()
	{
		root = null;
		number_of_items = 0;
	}
	public boolean containsKey(K key)
	{
		return get(key) != null;
	}
	public V get(K key)
	{
		return get(root, key);
	}
	private V get(Node n, K key)
	{
		if(n==null)
		{
			return null;
		}
		else if(key.compareTo(n.key) < 0)
		{
			return get(n.left,key);
		}
		else if(key.compareTo(n.key) > 0)
 		{
			return get(n.right,key);
		}
		else
		{
			return n.value;
		}
	}
	public int size()
	{
		return number_of_items;
	}
	public void put(K key, V value)
	{
		root = put(root, key, value);
		number_of_items++;
	}
	private Node put(Node n, K key, V value)
	{
		if(n == null)
		{
			return new Node(key,value);
		}
		if(key.compareTo(n.key) < 0)
		{
			n.left = put(n.left, key, value);
		}
		else if(key.compareTo(n.key) > 0)
		{
			n.right = put(n.right, key, value);
		}
		else
		{
			n.value  value;
		}
		return n;
	}
	public Set<K> keySet()
	{
		throw new UnsupportedOperationException("keySet is not supported");
	}
	public V remove(K key)
	{
		throw new UnsupportedOperationException("remove is not supported");
	}
	public V remove(K key, V value)
	{
		throw new UnsupportedOperationException("remove is not supported");
	}
	public Iterator<K> iterator()
	{
		throw new UnsupportedOperationException("iterator is not supported");
	}
	private class Node
	{
		private K key;
		private V value;
		private Node left;
		private Node right;
		public Node(K key, V value)
		{
			this.key = key;
			this.value = value;
		}
	}
}