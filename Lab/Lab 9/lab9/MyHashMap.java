package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
public class MyHashMap<K, V> implements Map61B<K, V> 
{
    private LinkedList<MapNode<K, V>>[] hashArray; 
    private double loadFactor;
    private int M;   
    private int N;   
    private final double REFACTOR = 2.0;  
    private class MapNode<K, V> 
	{
        private K key;
        private V value;
        public MapNode(K key, V value) 
		{
            this.key = key;
            this.value = value;
        }
    }
    public MyHashMap() 
	{
        this(5, 10.0);
    }
    public MyHashMap(int initialSize) 
	{
        this(initialSize, 10.0);
    }
    public MyHashMap(int initialSize, double loadFactor) 
	{
        this.M = initialSize;
        this.N = 0;
        this.loadFactor = loadFactor;
        hashArray = (LinkedList<MapNode<K, V>>[]) new LinkedList[initialSize];
        for (int i = 0; i < initialSize; i++) {
            hashArray[i] = new LinkedList<>();
        }
    }
    @Override
    public void clear()
	{
        int defaultSize = 5;
        hashArray = (LinkedList<MapNode<K, V>>[]) new LinkedList[defaultSize];
        for (int i = 0; i < defaultSize; i++) 
		{
            hashArray[i] = new LinkedList<>();
        }
        N = 0;
        M = defaultSize;
    }
    @Override
    public boolean containsKey(K key) 
	{
        if (key == null) 
		{
            throw new NullPointerException("Key is Null");
        }
        return get(key) != null;
    }
    private int hash(K key) 
	{
        return (key.hashCode() & 0x7fffffff) % M; 
    }
    @Override
    public V get(K key) 
	{
        int hashEntry = hash(key);
        MapNode<K, V> node = getNodeLinkedList(hashArray[hashEntry], key);
        if (node != null) 
		{
            return node.value;
        }
        return null;
    }
    private MapNode<K, V> getNodeLinkedList(LinkedList<MapNode<K, V>> chain, K key) 
	{
        for (MapNode<K, V> node : chain) 
		{
            if (node.key.equals(key)) 
			{
                return node;
            }
        }
        return null;
    }
    @Override
    public int size() 
	{
        return N;
    }
    @Override
    public void put(K key, V value) 
	{
        if (key == null) 
		{
            throw new NullPointerException("Key is Null.");
        } 
		else if (value == null) 
		{
            remove(key);
            return;
        }
        if ((double) N / (double) M > loadFactor) 
		{
            resize((int) (M * REFACTOR));
        }
        int hashEntry = hash(key);
        putLinkedList(hashArray[hashEntry], key, value);
    }
    private void putLinkedList(LinkedList<MapNode<K, V>> list, K key, V value) 
	{
        MapNode<K, V> node = getNodeLinkedList(list, key);
        if (node != null) 
		{
            node.value = value;
        } 
		else 
		{
            list.add(new MapNode<>(key, value));
            this.N += 1;
        }
    }
    private void resize(int newSize) 
	{
        MyHashMap<K, V> newHashMap = new MyHashMap<>(newSize);
        for (int i = 0; i < M; i++) 
		{
            for (MapNode<K, V> node : hashArray[i]) 
			{
                newHashMap.put(node.key, node.value);
            }
        }
        this.M = newHashMap.M;
        this.hashArray = newHashMap.hashArray;
    }
    @Override
    public Set<K> keySet() 
	{
        Set<K> keys = new HashSet<>();
        for (int i = 0; i < M; i++) 
		{
            for (MapNode<K, V> node : hashArray[i]) 
			{
                keys.add(node.key);
            }
        }
        return keys;
    }
    @Override
    public V remove(K key) 
	{
        if (key == null) throw new NullPointerException("Key is Null.");
        int hashEntry = hash(key);
        MapNode<K, V> node = getNodeLinkedList(hashArray[hashEntry], key);
        if (node != null)
		{
            hashArray[hashEntry].remove(node);
            N -= 1;
            return node.value;
        } 
		else 
		{
            return null;
        }
    }
    @Override
    public V remove(K key, V value) 
	{
        if (key == null)
		{
			throw new NullPointerException("Key is Null.");
		}	
        int hashEntry = hash(key);
        MapNode<K, V> node = getNodeLinkedList(hashArray[hashEntry], key);
        if (node != null && node.value.equals(value)) 
		{
            hashArray[hashEntry].remove(node);
            N -= 1;
            return node.value;
        } 
		else
		{
            return null;
        }
    }
    @Override
    public Iterator<K> iterator() 
	{
        return keySet().iterator();
    }

}