import java.util.Comparator;
import java.util.NoSuchElementException;

public class BST<T> {
   private class Node {
      private T data;
      private Node left;
      private Node right;

      public Node(T data) {
         this.data = data;
         left = null;
         right = null;
      }
   }

   private Node root;

   /***CONSTRUCTORS***/

   /**
   * Default constructor initializes member variable(s)
   */
   public BST() {
      root = null;
   }
   
   /**
    * Creates a BST of minimal height from an array of values.
    * @param array the list of values to insert.
    * @param cmp the way the tree is organized.
    * @precondition array must be sorted in ascending order
    *@throws IllegalArgumentException when the array is unsorted.
   */
   public BST(T[] array, Comparator<T> cmp) throws IllegalArgumentException {
      this();
      if (!isSorted(array, cmp)) {
         throw new IllegalArgumentException("BST(T[] array): Array must be sorted");
      }
      if (array != null) {
         root = arrayHelper(0, array.length - 1, array); 
      }
   }
   
   /**
   * Copy constructor for the BST
   * @param bst the BST of which to make a copy
   * @param cmp the way the tree is organized
   */
   public BST(BST<T> bst, Comparator<T> cmp) {
      this();
      if (bst == null || bst.root == null) {
         return;
      }
      copyHelper(bst.root, cmp);
   }
   
   /***ACCESSORS***/
   /**
   * Returns the smallest value in the tree
   * @precondition !isEmpty()
   * @return The smallest value
   * @throws NoSuchElementException when precondition is violated
   */
   public T findMin() throws NoSuchElementException{
      if(isEmpty()) {
         throw new NoSuchElementException("The list is empty, the list has no minimum.");
      }
      return findMin(root);
   }
   
   /**
   * Recursive helper for the findMin() method
   * @param node the current node to check if it is the smallest
   * @return the smallest value in the tree
   */
   private T findMin(Node node) {
      if(node.left == null) {
         return node.data;
      }
      return findMin(node.left);
   }
   
   /**
   * Returns the largest value in the tree
   * @precondition !isEmpty()
   * @return The largest value
   * @throws NoSuchElementException when precondition is violated
   */
   public T findMax() throws NoSuchElementException {
      if(isEmpty()) {
         throw new NoSuchElementException("The list is empty, the list has no maximum.");
      }
      return findMax(root);
   }
   
   /**
   * Recursive helper for the findMax() method
   * @param node the current node to check if it is the smallest
   * @return the smallest value in the tree
   */
   private T findMax(Node node) {
      if(node.right == null) {
         return node.data;
      }
      return findMax(node.right);
   }
   
   /**
   * Returns the data stored in the root
   * @precondition !isEmpty()
   * @return the data stored in the root
   * @throws NoSuchElementException when precondition is violated
   */
   public T getRoot() throws NoSuchElementException {
      if(isEmpty()) {
         throw new NoSuchElementException("root is null, can't get it");
      }
      return root.data;
   }
   
   /**
   * Determines whether the tree is empty
   * @return whether the tree is empty
   */
   public boolean isEmpty() {
      return root == null;
   }
   
   /**
   * Returns the height of the tree as an int
   * @return the height of the tree as an int
   */
   public int getHeight() {
      if(isEmpty()) { 
         return -1;
      }
      return getHeight(root);
   }
   
   /**
   * Private helper for the getHeight method
   * @param node the current node to count
   * @returns the height of the tree
   */
   private int getHeight(Node node) {
      if(node == null) {
         return -1;
      }
      int leftHeight = getHeight(node.left);
      int rightHeight = getHeight(node.right);
      return Math.max(leftHeight, rightHeight) + 1;
   }
   
   /**
   * Returns the current size of the tree (number of nodes)
   * @return the number of nodes in the tree
   */
   public int getSize() {
      if(isEmpty()) {
         return 0;
      }
      return getSize(root);
   }
   
   /**
   * Private recursive helper for the getSize() method
   * @param node the current node
   * @return the size of the tree
   */
   private int getSize(Node node) { 
      if(node == null) {
         return 0;
      }
      int leftLen = getSize(node.left);
      int rightLen = getSize(node.right);
      return leftLen + rightLen + 1;
   }
   
   /**
   * Searches for a specified value in the tree
   * @param data the value to search for
   * @param cmp the Comparator that indicates the way the data in the tree is ordered
   * @return the data stored in that Node of the tree, otherwise null
   */
   public T search(T data, Comparator <T> cmp) {
      return search(data, root, cmp);
   }
   
   /**
   * Recursive helper for the search method
   * @param data the value to search for
   * @param node the current node to check
   * @param cmp the Comparator that indicates the way the data in the tree is ordered
   * @return the data stored in that Node of the tree, otherwise null
   */
   private T search(T data, Node node, Comparator <T> cmp) {
      if(node == null) {
         return null;
      }
      if(cmp.compare(node.data, data) == 0) {
         return node.data;
      }
      else if(cmp.compare(node.data, data) > 0) {
         return search(data, node.left, cmp);
      }
      else {
         return search(data, node.right, cmp);
      }
   }

    /**
   * Searches for a specified value in the tree
   * @param data the value to search for
   * @param cmp the Comparator that indicates the way the data in the tree is ordered
   * @return the data stored in that Node of the tree, otherwise null
   */
   public T search2(T data, Comparator <T> cmp) {
      remove(data, cmp);
      return search2(data, root, cmp);
   }
   
   /**
   * Recursive helper for the search method
   * @param data the value to search for
   * @param node the current node to check
   * @param cmp the Comparator that indicates the way the data in the tree is ordered
   * @return the data stored in that Node of the tree, otherwise null
   */
   private T search2(T data, Node node, Comparator <T> cmp) {
      if(node == null) {
         return null;
      }
      if(cmp.compare(node.data, data) == 0) {
         return node.data;
      }
      else if(cmp.compare(node.data, data) > 0) {
         return search2(data, node.left, cmp);
      }
      else {
         return search2(data, node.right, cmp);
      }
   }
   
   /***MUTATORS***/
   
   /**
   * Removes a value from the BST
   * @precondition !isEmpty()
   * @param data the value to remove
   * @param cmp the Comparator indicating how data in the tree is organized
   */
   public void remove(T data, Comparator<T> cmp) {
      if(isEmpty()) { 
         return;
      }
      root = remove(data, root, cmp);
   }
   
   /**
   * Helper method to the remove method
   * @param data the data to remove
   * @param node the current node
   * @param cmp the Comparator indicating how data in the tree is organized
   * @return an update reference variable
   */
   private Node remove(T data, Node node, Comparator<T> cmp) {
      if(node == null) {
         return null;
      }
      if(cmp.compare(data, node.data) < 0) {
         node.left = remove(data, node.left, cmp);
      }
      else if(cmp.compare(data, node.data) > 0) {
         node.right = remove(data, node.right, cmp);
      }
      else {
         if(node.left == null && node.right == null) {
            node = null;
         }
         else if(node.left == null && node.right != null) {
            return node.right;
         }
         else if(node.right == null && node.left != null) {
            return node.left;
         }
         else if(node.left != null && node.right != null) {
            node.data = findMin(node.right);
            node.right = remove(node.data, node.right, cmp);
         }
      }
      return node;
   }
   
   /**
    * Wrapper that inserts a new node in the tree
    * @param data the data to insert
    * @param cmp the Comparator indicating how the data is ordered
    */
   public void insert(T data, Comparator<T> cmp) {
      if (isEmpty()) {
         root = new Node(data);
      }
      else {
         insert(data, root, cmp);
      }
   }

   /**
    * Helper that recursively inserts a new node in the tree
    * @param data the data to insert
    * @param node the current node in the search for the correct insert location
    * @param cmp the Comparator indicating how data in the tree is ordered
    */
   private void insert(T data, Node node, Comparator<T> cmp) {
      if (cmp.compare(data, node.data) <= 0) {
         if (node.left == null) {
            node.left = new Node(data);
            return;
         }
         insert(data, node.left, cmp);
      }
      else {
         if (node.right == null) {
            node.right = new Node(data);
            return;
         }
         insert(data, node.right, cmp);
      }
   }

   /***ADDITONAL OPERATIONS***/
   
   /**
   * Private helper method for copy constructor
   * @param node the node containing data to copy
   * @param cmp the way the tree is organized
   */
   private void copyHelper(Node node, Comparator <T> cmp) {
      if (node == null) {
         return;
      }
      insert(node.data, cmp);
      copyHelper(node.left, cmp);
      copyHelper(node.right, cmp);
   }
   
   /**
   * Private helper method for array constructor to check if array is sorted.
   * @param array the array to check
   * @param cmp the way the tree is organized
   * @returns whether the array is sorted or not
   */
   private boolean isSorted(T[] array, Comparator<T> cmp) {
      if (array != null && array[0] != null) {
         for (int i = 0; i < array.length - 1 && array[i + 1] != null; i++) {
            if (cmp.compare(array[i], array[i + 1]) > 0) {
               return false;
            }
         }
      }
      return true;
   }
   
   /**
   * Recursive helper for the array constructor
   * @param begin beginning array index
   * @param end ending array index
   * @param array the array to search
   * @returns the new Node added to the bst
   */
   private Node arrayHelper(int begin, int end, T[] array) {
      Node node = null;
      if (end == begin) {
         node = new Node(array[begin]);
      }
      else if (end == begin + 1) {
         node = new Node(array[begin]);
         node.right = new Node(array[end]);
      }
      else {
         node = new Node(array[(end - begin) / 2 + begin]);
         node.left = arrayHelper(begin, (end - begin) / 2 + begin - 1, array);
         node.right = arrayHelper((end - begin) / 2 + begin + 1, end, array);
      }
      return node;
   }

   /**
    * Creates a String that is a height order
    * traversal of the data in the tree starting at
    * the Node with the largest height (the root)
    * down to Nodes of smallest height - with
    * Nodes of equal height added from left to right.
    * @return the level order traversal as a String
    */
   public String levelOrderString() {
      Queue<Node> que = new Queue<>();
      StringBuilder sb = new StringBuilder();
      que.enqueue(root);
      levelOrderString(que, sb);
      return sb.toString() + "\n";
   }

   /**
    * Helper method to levelOrderString.
    * Inserts the data in level order into a String.
    * @param que the Queue in which to store the data.
    * @param heightTraverse a StringBuilder containing the data.
    */
   private void levelOrderString(Queue<Node> que, StringBuilder heightTraverse) {
      if (!que.isEmpty()) {
         Node nd = que.getFront();
         que.dequeue();
         if (nd != null) {
            que.enqueue(nd.left);
            que.enqueue(nd.right);
            heightTraverse.append(nd.data + " ");
         }
         levelOrderString(que, heightTraverse);
      }
   }
   /**
   * Returns a String of data in pre order
   * @return String in pre order of BST
   */
   public String preOrderString() {
      StringBuilder sb = new StringBuilder();
      preOrderString(root, sb);
      return sb.toString() + "\n";
   }
   
   /**
   * Private helper for preOrderString method
   * @param Node the current node
   * @param preOrder a StringBuilder containing the data
   */
   private void preOrderString(Node node, StringBuilder preOrder) {
      if (node == null) {
         return;
      }
      preOrder.append(node.data + " ");
      preOrderString(node.left, preOrder);
      preOrderString(node.right, preOrder);
   }
   
   /**
   * Returns a String of data in inOrder
   * @return String in inOrder of BST
   */
   public String inOrderString() {
      StringBuilder sb = new StringBuilder();
      inOrderString(root, sb);
      return sb.toString() + "\n";
   }
   
   /**
   * Private helper for inOrderString method
   * @param Node the current node
   * @param inOrder a StringBuilder containing the data
   */
   private void inOrderString(Node node, StringBuilder inOrder) {
      if (node == null) {
         return;
      }
      inOrderString(node.left, inOrder);
      inOrder.append(node.data + " ");
      inOrderString(node.right, inOrder);
   }
   
   /**
   * Returns a String of data in post order
   * @return String in post order of BST
   */
   public String postOrderString() {
      StringBuilder sb = new StringBuilder();
      postOrderString(root, sb);
      return sb.toString() + "\n";
   }
   
   /**
   * Private helper for postOrderString method
   * @param Node the current node
   * @param postOrder a StringBuilder containing the data
   */
   private void postOrderString(Node node, StringBuilder postOrder) {
      if (node == null) {
         return;
      }
      postOrderString(node.left, postOrder);
      postOrderString(node.right, postOrder);
      postOrder.append(node.data + " ");
   }
   
   /***Challenge Methods***/
   /**
   * Returns the data of the Node who is the shared precursor to the two Nodes containing the given data
   * If either data1 or data2 is a duplicate value, the method will find the precursor of the duplicate with
   * greatest height
   * @param data1 the data contained in one node of the tree
   * @param data2 the data contained in another node of the tree
   * @param cmp the way the tree is organized
   * @precondition data1 and data2 must exist in the BST
   * @throws IllegalArgumentException when one or both values do not exist in the BST
   */
   public T sharedPrecursor(T data1, T data2, Comparator <T> cmp) throws IllegalArgumentException {
      if (isEmpty() || search(data1, cmp) == null || search(data2, cmp) == null) {
         throw new IllegalArgumentException("sharedPrecursor: one or both values do not exist in the BST");
      }
      return sharedPrecursor(data1, data2, root, cmp);
   }
   
   /**
   * Private helper method to sharedPrecursor which recursively locates the shared precursor
   * @param data1 the data contained in one node of the tree
   * @param data2 the data contained in another node of the tree
   * @param currLevel the current node of the tree branch
   * @param cmp the way the tree is organized
   * @return the data stored by the shared precursor
   */
   private T sharedPrecursor(T data1, T data2, Node currLevel, Comparator <T> cmp) {
      if (currLevel == null) {
         return null;
      }
      if (cmp.compare(currLevel.data, data1) == 0 || cmp.compare(currLevel.data, data2) == 0) {
         return currLevel.data;
      }
      T left = sharedPrecursor(data1, data2, currLevel.left, cmp);
      T right = sharedPrecursor(data1, data2, currLevel.right, cmp);
      if (left != null && right != null) {
         return currLevel.data;
      }
      if (left != null) {
         return left;
      }
      else {
         return right;
      }
   }
	//----------------------------------------------------------------------------------------------------------
    /**
     * Returns all elements in the tree as an ArrayList (in-order) Using for ViewFriendSorted() in main
     * @return ArrayList of all elements
     */
    public ArrayList<T> toArrayList() {
        ArrayList<T> list = new ArrayList<>();
        toArrayList(root, list);
        return list;
    }

   public void toArrayList(Node node, ArrayList<T> list) {
	   if (node == null) {
	         return;
	   }
	   toArrayList(node.left, list);
	   list.add(node.data);
	   toArrayList(node.right, list);
   }

   /**
     * Searches for all users with a specific name,  created for searchFriendsByName() in main
     * @param name the name to search for
     * @return ArrayList of all matching users
     */
    public ArrayList<T> searchByName(String name) {
        ArrayList<T> results = new ArrayList<>();
        searchByName(name, root, results);
        return results;
    }

	/*
	 *	Helper for searchByName
	 */
    private void searchByName(String name, Node node, ArrayList<T> results) {
       if (node == null) {
           return;
       }
       
       User user = (User) node.data;
       if (user.getName().equalsIgnoreCase(name)) {
           results.add(node.data);
       }
       
       // Search both subtrees since multiple users can have same name
       searchByName(name, node.left, results);
       searchByName(name, node.right, results);
   }
}
