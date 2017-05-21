import java.util.ArrayList;


public class ArrayHeap<T> {
	private ArrayList<Node> contents = new ArrayList<Node>();

    private int size = 0;
	public void insert(T item, double priority) {
        setNode(size + 1, new Node(item, priority));  
        bubbleUp(size + 1);      
        size += 1;               
    }


	public Node peek() {
		return getNode(1);   
	}

	public Node removeMin() {
	    Node smallestNode = peek();
        setNode(1, getNode(size));
        setNode(size, null);
        size -= 1;
        bubbleDown(1);
		return smallestNode;
	}

	public void changePriority(T item, double priority) {
	    for (int i = 1; i <= size; i++) {
            if (item.equals(getNode(i).item())) {
                getNode(i).myPriority = priority;
                if (min(i, getParentOf(i)) == i) {
                    bubbleUp(i);
                } else {
                    bubbleDown(i);
                }
                return;
            }
        }
	}

	@Override
	public String toString() {
		return toStringHelper(1, "");
	}

	private String toStringHelper(int index, String soFar) {
		if (getNode(index) == null) {
			return "";
		} else {
			String toReturn = "";
			int rightChild = getRightOf(index);
			toReturn += toStringHelper(rightChild, "        " + soFar);
			if (getNode(rightChild) != null) {
				toReturn += soFar + "    /";
			}
			toReturn += "\n" + soFar + getNode(index) + "\n";
			int leftChild = getLeftOf(index);
			if (getNode(leftChild) != null) {
				toReturn += soFar + "    \\";
			}
			toReturn += toStringHelper(leftChild, "        " + soFar);
			return toReturn;
		}
	}

	private Node getNode(int index) {
		if (index >= contents.size()) {
			return null;
		} else {
			return contents.get(index);
		}
	}

	private void setNode(int index, Node n) {
		while (index + 1 >= contents.size()) {
			contents.add(null);
		}
		contents.set(index, n);
	}

	private void swap(int index1, int index2) {
		Node node1 = getNode(index1);
		Node node2 = getNode(index2);
		this.contents.set(index1, node2);
		this.contents.set(index2, node1);
	}

	private int getLeftOf(int i) {
		return 2 * i;
	}

	private int getRightOf(int i) {
		return 2 * i + 1;
	}

	private int getParentOf(int i) {
		return i / 2;
	}

	private void setLeft(int index, Node n) {
	    setNode(getLeftOf(index), n);
	}

	private void setRight(int index, Node n) {
	    setNode(getRightOf(index), n);
	}

	private void bubbleUp(int index) {
	    while ((min(index, getParentOf(index)) == index) && (index != 1)) {
            swap(index, getParentOf(index));
            index = getParentOf(index);
        }
	}

	private void bubbleDown(int index) {
	
        if (getNode(getLeftOf(index)) == null && getNode(getRightOf(index)) == null) {
            return;
        }

        int smallestChilren = min(getLeftOf(index), getRightOf(index));

        if (min(index, getLeftOf(index)) != index || min(index, getRightOf(index)) != index) {
            swap(index, smallestChilren);
            bubbleDown(smallestChilren);
        }
	}

	private int min(int index1, int index2) {
		Node node1 = getNode(index1);
		Node node2 = getNode(index2);
		if (node1 == null) {
			return index2;
		} else if (node2 == null) {
			return index1;
		} else if (node1.myPriority < node2.myPriority) {
			return index1;
		} else {
			return index2;
		}
	}

	public class Node {
		private T myItem;
		private double myPriority;

		private Node(T item, double priority) {
			myItem = item;
			myPriority = priority;
		}

		public T item() {
			return myItem;
		}

		public double priority() {
			return myPriority;
		}

		@Override
		public String toString() {
			return item().toString() + ", " + priority();
		}
	}

	public static void main(String[] args) {
		ArrayHeap<String> heap = new ArrayHeap<String>();
		heap.insert("c", 3);
		heap.insert("i", 9);
		heap.insert("g", 7);
		heap.insert("d", 4);
		heap.insert("a", 1);
		heap.insert("h", 8);
		heap.insert("e", 5);
		heap.insert("b", 2);
		heap.insert("c", 3);
		heap.insert("d", 4);
		System.out.println(heap);
	}

}
