import java.util.NoSuchElementException;
public class LinkedList<T> {
   
   /**
   * contained class to create the individual nodes in the linked list
   */
   private class Node {
      private T data;
      private Node prev;
      private Node next;
      
      /**
      *  Node constuctor
      *  @param Node's data
      */
      public Node (T data) {
         this.data = data;
         this.prev = null;
         this.next = null;
      }
   }
   
    private Node first;
    private Node last;
    private Node iterator;
    private int length;
    
   /**
   * default constructor of linked list
   */
   @SuppressWarnings("unchecked")
   public LinkedList() {
      first = null;
      last = null;
      length = 0;
   }
   
   /**
   *  constructs linked list from an array
   * @param Array to be made a linked list
   */
   @SuppressWarnings("unchecked")
   public LinkedList(T[] array) {
      this();
      if (array != null && array.length != 0 && array[0] != null) {
         int index = 0;
         while(index < array.length && array[index] != null) {
            addLast(array[index]);
            index++;
         }
         iterator = null;
      }
   }
   
   /**
   *  copy constructor
   * @param linked list to be coppied
   */
   @SuppressWarnings("unchecked")
   public LinkedList(LinkedList<T> original) {
      this();
      if(original != null && original.length != 0) {
         Node temp = original.first;
         while(temp != null) {
            addLast(temp.data);
            temp = temp.next;
         }
         iterator = null;
      }
      
   }
   
   /**
   * converts list to printable string
   * @returns readable string of list
   */
   public String toString() {
      String result = "";
      if (!isEmpty()) {
         result += first.data + " ";
         Node curNode = first.next;
         while (curNode != null) {
            result += curNode.data + " ";
            curNode = curNode.next;
         } 
      }
      return result + "\n";
   }
   
   /**
   * checks if list is empty
   * @returns boolean of if list is empty
   */
   public boolean isEmpty() {
      return length == 0;
   }
   
   /**
   * retrives the length of the list
   * @return length of linked list
   */
   public int getLength() {
      return length;
   }
   
   /**
   * adds element to begining of list
   * @param Data of element to be added
   * @postcondition element added to begining of list 
   */
   public void addFirst(T data) {
      Node dataNode = new Node(data);
      if (length == 0) {
         first = last = dataNode;
      } else {
         dataNode.next = first;
         first.prev = dataNode;
         first = dataNode;
      }
      length++;
   }
   
   /**
   * retrives the first emlement of the list
   * @returns first element in list
   */
   public T getFirst() throws NoSuchElementException {
      if (first == null) {
         throw new NoSuchElementException("getFirst(): LinkedList cannot be empty.");
      }
      return first.data;
   }
   
   /**
   * adds element to end of list
   * @param Data of element to be added
   * @postcondition element added to end of list
   */
   public void addLast(T data) {
      Node dataNode = new Node(data);
      if (length == 0) {
         first = last = dataNode;
      } else {
         dataNode.prev = last;
         last.next = dataNode;
         last = dataNode;
      }
      length++;
   }
   
   /**
   * retrives the last emlement of the list
   * @returns Last element in list
   */
   public T getLast() throws NoSuchElementException {
       if (last == null) {
         throw new NoSuchElementException("getLast(): LinkedList cannot be empty.");
      }
      return last.data;
   }
   
   /**
   * Removes element at the end of the List
   * @precondidtion Linked List must not be empty
   * @postcondition First element of list is removed
   * @throws NoSuchElementException when list is empty
   */
   public void removeFirst() throws NoSuchElementException {
      if (first == null) {
         throw new NoSuchElementException("removeFirst(): LinkedList cannot be empty.");
      } else if (length == 1) {
         first = last = iterator = null;
      } else {
         if (iterator == first) {
            iterator = null;
         }
         first = first.next;
         first.prev = null;
      }
      length--;
   }
   
   /**
   * Removes element at the end of the List
   * @precondidtion Linked List must not be empty
   * @postcondition Last element of list is removed
   * @throws NoSuchElementException when list is empty
   */
   public void removeLast() throws NoSuchElementException {
      if (last == null) {
         throw new NoSuchElementException("removeLast(): LinkedList cannot be empty.");
      } else if (length == 1) {
         first = last = iterator = null;
      } else {
         if (iterator == last) {
            iterator = null;
         }
         last = last.prev;
         last.next = null;
      }
      length--;
   }
   
   /**
   * Sets the Iterator to the first node
   */
   public void positionIterator() {
      iterator = first;
   }
   
   /**
   *  returns the element of the current iterator
   *  @precondition iterator must not be null
   *  @return the iterator data 
   */
   public T getIterator() throws NullPointerException {
      if (iterator == null) {
         throw new NullPointerException("getIterator(): cannot get a null iterator.");
      }
      return iterator.data;
   }
   
   /**
   *  return whether the iterator is on the list or not
   *  @return boolean of if iterator is null
   */
   public boolean offEnd() {
      return (iterator == null); 
   }
   
   /**
   *  moves the iterator to the next item in the list
   *  @precondition iterator must not be null
   */
   public void advanceIterator() throws NullPointerException {
      if (iterator == null) {
         throw new NullPointerException("advanceIterator(): cannot advance a null iterator.");
      }
      iterator = iterator.next;
   }
   
   /**
   *  moves the iterator to the previous item in the list
   *  @precondition iterator must not be null
   */
   public void reverseIterator() throws NullPointerException {
      if (iterator == null) {
         throw new NullPointerException("reverseIterator(): cannot reverse a null iterator.");
      }
      iterator = iterator.prev;
   }
   
   /**
   *  inserts an element after the iterator 
   *  @precondition iterator must not be null
   *  @param data to be added to list
   */
   public void addIterator(T data) throws NullPointerException {
      if (iterator == null) {
         throw new NullPointerException("addIterator(): cannot insert after a null iterator.");
      }
      if (iterator == last) {
         addLast(data);
      } else {
         Node dataNode = new Node(data);
         dataNode.prev = iterator;
         dataNode.next = iterator.next;
         iterator.next.prev = dataNode;
         iterator.next = dataNode;
         length++;
      }
   }
   
   /**
   *  removes the iterator
   *  @precondition iterator must not be null
   *  @postcondition iterator is null
   */
   public void removeIterator() throws NullPointerException {
      if (iterator == null) {
         throw new NullPointerException("removeIterator(): cannot remove a null iterator.");
      }
      if (iterator == first) {
         removeFirst();
      } else if (iterator == last) {
         removeLast();
      } else {
         iterator.next.prev = iterator.prev;
         iterator.prev.next = iterator.next;
         iterator = null;
         length--;
      }
   }
   
   /**
   * Tests if linked list is equal to given object
   * @param object to be compared
   * @return boolean of wether object is equal to linked list
   */
   @SuppressWarnings("unchecked")
   @Override public boolean equals(Object obj) {
      
      if (obj == this) {
         return true;
      } else if (!(obj instanceof LinkedList)) {
         return false;
      } else {
         LinkedList<T> objList = (LinkedList<T>) obj;
         if (length != objList.length) {
            return false;
         } else {
            Node temp1 = this.first;
            Node temp2 = objList.first;
            while(temp1 != null) {
               if (!temp1.data.equals(temp2.data)) {
                  return false;
               }
               temp1 = temp1.next;
               temp2 = temp2.next;
            }
         }
         return true;
      }     
   }
   
   /**
   * Clears all nodes from linked list
   */
   public void clear() {
      while (last != null) {
         removeLast();
      }
   }
   
   /**** ADDITIONAL OPPERATIONS ****/
   
   /**
   * converts list to printable string with each value numbered on its own line
   * @returns readable numbered string of list
   */
   public String numberedListString() {
      String result = "";
      if (!isEmpty()) {
         Node curNode = first;
         for (int i = 1; curNode != null; i++) {
            result += i + ". " + curNode.data + "\n";
            curNode = curNode.next;
         } 
      }
      return result + "\n";
   }
   
   
   /** MORE METHODS */
   
   /**
   * finds some given data within the LL and gives it's index
   * @param the data we wish to find in LL
   * @returns index of data or -1 if data not in LL
   */
   public int findIndex(T data) {
      if (!isEmpty()) {
         Node curNode = first;
         for (int i = 0; curNode != null; i++) {
            if(curNode.data.equals(data)) {
               return i;
            }
            curNode = curNode.next;
         }
      }
      return -1;
   }
   
   /**
   * finds some given data within the LL and gives it's index
   * @precondition 0 =< index < length
   * @param Index we wish to place iterator at
   * @throws NoSuchElementException precondition is not met
   */
   public void advanceIteratorToIndex(int index) throws IndexOutOfBoundsException {
      if (index < 0 || index >= length) {
         throw new IndexOutOfBoundsException("advanceItereatorToIndex(): Index out of bounds of list");
      }
      positionIterator();
      for(int i = 1; i <= index; i++) {
         advanceIterator();
      }
   }
   
}
