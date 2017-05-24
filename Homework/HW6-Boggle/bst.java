
import java.io.*;

public class BinarySearchTree implements Serializable {
	private static final long serialVersionUID = 2L;

	protected Node root; 
	private int size; 
boolean add_success; 
	boolean add_height_increase;

	private class Node implements Serializable {
		private static final long serialVersionUID = 2L;

		public String term;
		public int balance;
		
		public Node left;
		public Node right;

		public Node(String term) {
			this.term = term;
			balance = 0;
			left = null;
			right = null;
		}

		public String toString() {
			String str = term + "(" + balance + ")";
			if (left != null || right != null) {
				str = str + "[";
				if (left != null) {
					str = str + left.toString();
				}
				str = str + ", ";
				if (right != null) {
					str = str + right.toString();
				}
				str = str + ']';
			}
		
			return str;
		}
	}


	public BinarySearchTree() {
		root = null;
		size = 0;
	}
	public boolean add(String term) {
		add_success = false;
		add_height_increase = false;
		root = add(term, root);
		if (add_success) {
			size += 1;
		}
		return add_success;
	}
	private Node add(String term, Node node) {
		if (node == null) {
			add_success = true;
			return new Node(term);
		}
		int bias = term.compareTo(node.term);
		if (bias == 0) {
			return node;
		}

		if (node.left == null && node.right == null) {
			add_height_increase = true;
		}
		if (bias < 0) {
			node.left = add(term, node.left);
			if (add_height_increase) {
				node.balance -= 1;
				if (node.balance <= -2) {
					if (node.left.balance >= 1) {
						if (node.left.right.balance <= -1) {
							node.left.balance = 0;
							node.left.right.balance = 0;
							node.balance = 1;
						} else {
							node.left.balance = -1;
							node.left.right.balance = 0;
							node.balance = 0;
						}
						node.left = rotateLeft(node.left);
					} else {
						node.left.balance = 0;
						node.balance = 0;
					}
					if (node.left.balance == 0) {
						add_height_increase = false;
					}
					return rotateRight(node);
				}
			}
		} else {
			node.right = add(term, node.right);
			if (add_height_increase) {
				node.balance += 1;
				if (node.balance >= 2) {
					if (node.right.balance <= -1) {
						if (node.right.left.balance >= 1) {
							node.right.balance = 0;
							node.right.left.balance = 0;
							node.balance = -1;
						} else {
							node.right.balance = 1;
							node.right.left.balance = 0;
							node.balance = 0;
						}
						node.right = rotateRight(node.right);
					} else {
						node.right.balance = 0;
						node.balance = 0;
					}
					if (node.right.balance == 0) {
						add_height_increase = false;
					}
					return rotateLeft(node);
				}
			}
		}

		return node;
	}
	public boolean find(String term) {
		if (root == null) {
			return false;
		}
		return findNode(term, root);
	}

	private boolean findNode(String term, Node node) {
		if (term.compareTo(node.term) < 0) {
			if (node.left == null) {
				return false;
			} else if (node.left.term.equals(term)) {
				return true;
			} else {
				return findNode(term, node.left);
			}
		} else {
			if (node.right == null) {
				return false;
			} else if (node.right.term.equals(term)) {
				return true;
			} else {
				return findNode(term, node.right);
			}
		}
	}

	public int size() {
		return size;
	}
	
	public String toString() {
		if (root == null) {
			return "[]";
		}
		return root.toString();
	}
	
	
	private Node rotateRight(Node node) {
		Node temp = node.left;
		node.left = temp.right;
		temp.right = node;
		return temp;
	}

	private Node rotateLeft(Node node) {
		Node temp = node.right;
		node.right = temp.left;
		temp.left = node;
		return temp;
	}
}