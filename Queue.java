import java.util.NoSuchElementException;

public class Queue<T> implements Q<T> {
   private class Node {
      private T data;
      private Node next;
   
      public Node(T data) {
         this.data = data;
         this.next = null;
      }
   }
   
   private int size;
   private Node front;
   private Node end;
   
   /**
   * Instantiates a new Queue with default values
   * @postcondition The list is empty (front == null, end == null, size == 0)
   */
   public Queue() {
      size = 0;
      front = null;
      end = null;
   }
   
   /**
   * Converts the given array into a Queue
   * @param array the array of values to insert into this Queue
   * @postcondition a new Queue containing array elements
   */
   public Queue(T [] array) {
      this();
      if(array != null && array.length != 0) {
         for(int i = 0; i < array.length; i ++) {
            enqueue(array[i]);
         }
      }
   }
   
   /**
   * Instantiates a new Queue by by copying another Queue
   * @param original the Queue to copy
   * @postcondition a new Queue object which is an identical but
   * separate copy of the Queue original
   */
   public Queue(Queue <T> original) {
      this();
      if(original != null && original.size != 0) {
         Node temp = original.front;
         while(temp != null) {
            enqueue(temp.data);
            temp = temp.next;
         }
      }
   }
   
   /**
   * Returns the value stored at the front of the Queue.
   * @return the value at the front of the queue
   * @precondition !isEmpty()
   * @throws NoSuchElementException when the
   * precondition is violated
   */
   public T getFront() throws NoSuchElementException {
      if(isEmpty()) {
         throw new NoSuchElementException("No front bro");
      }
      return front.data;
   }
   
   /**
   * Returns the size of the Queue
   * @return the size from 0 to n
   */
   public int getSize() {
      return size;
   }
   
   /**
   * Determines whether a Queue is empty
   * @return whether the Queue contains no elements
   */
   public boolean isEmpty() {
      return front == null;
   }
   
   /**
   * Inserts a new value at the end of the Queue
   * @param data the new data to insert
   * @postcondition a new node at the end of the Queue
   */
   public void enqueue(T data) {
      Node newNode = new Node(data);
      if(isEmpty()) {
         front = end = newNode;
      }
      else {
         end.next = newNode;
         end = newNode;
      }
      size ++;
   }
   
   /**
   * Removes the front element in the Queue
   * @precondition !isEmpty()
   * @throws NoSuchElementException when
   * the precondition is violated
   * @postcondition the front element has
   * been removed
   */
   public void dequeue() throws NoSuchElementException {
      if(isEmpty()) {
         throw new NoSuchElementException("can't deque dawg");
      }
      else if(front == end) {
         front = end = null;
      }
      else {
         front = front.next;
      }
      size --;
   }
   
   /**
   * Determines whether the given Object is another Queue, containing
   * the same data in the same order as this Queue
   * @param obj another Object
   * @return whether the parameter is equal to this Queue
   */
   @SuppressWarnings("unchecked")
   @Override public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }
      else if(!(obj instanceof Queue)) {
         return false;
      }
      else {
         Queue <T> L = (Queue<T>) obj;
         if(L.size != size) {
            return false;
         }
         Node temp1 = this.front;
         Node temp2 = L.front;
         while(temp1 != null) {
            if(temp1.data == null || temp2.data == null) {
               if(temp1.data != temp2.data) {
                  return false;
               }
            }
            else if(!(temp1.data.equals(temp2.data))) {
               return false;
            }
            temp1 = temp1.next;
            temp2 = temp2.next;
         }
      }
      return true;
   }
   
   /**
   * Converts the Queue to a String, with each value separated by a blank space.
   * At the end of a String, place a new line character.
   * @return the Queue as a String
   */
   @Override
   public String toString() {
      String result = "";
      Node temp = front;
      while(temp != null) {
         result += temp.data + " ";
         temp = temp.next;
      }
      return result + "\n";
   }
}
