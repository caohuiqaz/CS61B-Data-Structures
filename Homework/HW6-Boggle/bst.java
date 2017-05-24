/**
 * Binary search tree for string terms
 * Implements AVL balancing algorithm as described in 
 * Koffman and Wolfgang: Objects, Abstraction, Data Structures, and Design
 * 
 * @author Kyle Kingsbury
 */

import java.io.*;

public class BinarySearchTree implements Serializable {
	private static final long serialVersionUID = 2L;

	protected Node root; // Root of the tree
	private int size; // Number of elements in the tree;

	// Recursive method persistance variables
	// This is why methods should have multiple return values.
	boolean add_success; // A node was successfully added to the tree
	boolean add_height_increase; // The height of the tree has increased.

	private class Node implements Serializable {
		private static final long serialVersionUID = 2L;

		public String term;
		public int balance;
		/*
		 * The balance of a node is the difference of the height of its
		 * sub-trees. If the left sub-tree is larger, balance is negative. If
		 * the right is larger, the balance is zero.
		 */
		public Node left;
		public Node right;

		/**
		 * Node constructor
		 * 
		 * @param term Term this node represents
		 */
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


	/**
	 * BinarySearchTree Constructor
	 * 
	 */
	public BinarySearchTree() {
		root = null;
		size = 0;
	}

	/**
	 * Add term and index to tree. If term exists, adds the index to the
	 * existing term. If term does not exist, creates a new node.
	 * 
	 * @param term The term
	 */
	public boolean add(String term) {
		// Perform recursive search
		add_success = false;
		add_height_increase = false;
		root = add(term, root);
		if (add_success) {
			size += 1;
		}
		return add_success;
	}

	/**
	 * Recursive helper for add
	 * 
	 * @param term Term
	 * @param node Tree to add in.
	 * @return Root node of the updated subtree.
	 */
	private Node add(String term, Node node) {
		// Base case: This node is undefined
		if (node == null) {
			// Add node
			add_success = true;
			return new Node(term);
		}

		// Compute relative location of term to this node
		int bias = term.compareTo(node.term);

		// Base case: This is the correct term.
		if (bias == 0) {
			// Node is already defined.
			return node;
		}

		if (node.left == null && node.right == null) {
			// The next recursion will create a new child and increase the
			// height of this tree. Since there are no children, and we haven't
			// returned already, the term does not exist in this tree and will
			// be added.
			add_height_increase = true;
		}

		// Recursive case: What subtree does the term belong in?
		if (bias < 0) {
			// Term is in the left subtree
			// Add term to left subtree
			node.left = add(term, node.left);
			if (add_height_increase) {
				// The left subtree has increased in height
				node.balance -= 1;
				if (node.balance <= -2) {
					// Rebalance
					if (node.left.balance >= 1) {
						// CASE: left-right imbalance
						// Adjust balances
						if (node.left.right.balance <= -1) {
							// CASE: left-right-left insertion
							node.left.balance = 0;
							node.left.right.balance = 0;
							node.balance = 1;
						} else {
							// CASE: left-right-right insertion
							node.left.balance = -1;
							node.left.right.balance = 0;
							node.balance = 0;
						}
						// Perform rotation
						node.left = rotateLeft(node.left);
						// Right rotation is done for both cases
					} else {
						// CASE: left-left imbalance
						// Adjust balances
						node.left.balance = 0;
						node.balance = 0;
						// Right rotation is done for both trees
					}
					// Check to see if this operation balanced the tree
					if (node.left.balance == 0) {
						// No further rebalancing is necessary.
						// The addition resulted in a more balanced tree. Since
						// balancing operations always result in a tree that is
						// equal to or less than the height of the original,
						// there is no need to rebalance further.
						add_height_increase = false;
					}
					return rotateRight(node);
				}
			}
		} else {
			// Term is in the right subtree
			// Add term to right subtree
			node.right = add(term, node.right);
			if (add_height_increase) {
				// The right subtree has increased in height
				node.balance += 1;
				if (node.balance >= 2) {
					// Rebalance
					if (node.right.balance <= -1) {
						// CASE: right-left imbalance
						// Adjust balances
						if (node.right.left.balance >= 1) {
							// CASE: right-left-right insertion
							node.right.balance = 0;
							node.right.left.balance = 0;
							node.balance = -1;
						} else {
							// CASE: right-left-left insertion
							node.right.balance = 1;
							node.right.left.balance = 0;
							node.balance = 0;
						}
						// Perform rotation
						node.right = rotateRight(node.right);
						// Left rotation is done for both cases
					} else {
						// CASE: right-right imbalance
						// Adjust balances
						node.right.balance = 0;
						node.balance = 0;
						// Left rotation is done for both trees
					}
					// Check to see if this operation balanced the tree
					if (node.right.balance == 0) {
						// No further rebalancing is necessary.
						// The addition resulted in a more balanced tree. Since
						// balancing operations always result in a tree that is
						// equal to or less than the height of the original,
						// there is no need to rebalance further.
						add_height_increase = false;
					}
					return rotateLeft(node);
				}
			}
		}

		// No changes
		return node;
	}

	/**
	 * Finds the given term in the tree.
	 * 
	 * @param term The term to search for
	 * @return True if term is defined. Otherwise, returns false.
	 */
	public boolean find(String term) {
		if (root == null) {
			// Empty tree
			return false;
		}

		// Call recursive helper
		return findNode(term, root);
	}

	/**
	 * Finds the given term in the tree.
	 * 
	 * @param term The term to search for
	 * @param node The current node we are checking
	 * @return True if node exists, otherwise false.
	 */
	private boolean findNode(String term, Node node) {
		// Recursive case: Check the appropriate subtree
		if (term.compareTo(node.term) < 0) {
			// Term is in the left subtree
			if (node.left == null) {
				// Left subtree is null
				return false;
			} else if (node.left.term.equals(term)) {
				// Left node contains term
				return true;
			} else {
				// Left subtree is non-null, recursively find
				return findNode(term, node.left);
			}
		} else {
			// Term is in the right subtree
			if (node.right == null) {
				// Right subtree is null
				return false;
			} else if (node.right.term.equals(term)) {
				// Right node contains term
				return true;
			} else {
				// Right subtree is non-null, recursively find
				return findNode(term, node.right);
			}
		}
	}

	/**
	 * @return Number of elements in the tree.
	 */
	public int size() {
		return size;
	}
	
	public String toString() {
		if (root == null) {
			// Base case: empty tree
			return "[]";
		}
		return root.toString();
	}
	
	// Private helper methods
	
	/**
	 * Rotates a given root node of a tree right.
	 * 
	 * @param node Root node of the tree to rotate.
	 * @return The root of the altered tree.
	 */
	private Node rotateRight(Node node) {
		Node temp = node.left;
		node.left = temp.right;
		temp.right = node;
		return temp;
	}

	/**
	 * Rotates a given root node of a tree left;
	 * 
	 * @param node Root node of the tree to rotate.
	 * @return The root of the altered tree.
	 */
	private Node rotateLeft(Node node) {
		Node temp = node.right;
		node.right = temp.left;
		temp.left = node;
		return temp;
	}
}