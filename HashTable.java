import java.util.ArrayList;

public class HashTable<T> {
   private int numElements;
   private ArrayList<LinkedList<T> > table;
   
   /** Constructors */
   
   /**
   * One arg constructor that initializes numElements and table
   * @precondition size is greater than 0
   * @param size the size of the ArrayList table
   * @throws IllegalArgumentException when size is out of bounds
   */
   public HashTable(int size) throws IllegalArgumentException {
      if(size <= 0) {
         throw new IllegalArgumentException("can't make size <= 0");
      }
      numElements = 0;
      table = new ArrayList<LinkedList<T>>(size);
      for(int i = 0; i < size; i ++) {
         LinkedList<T> eachList = new LinkedList<>();
         table.add(eachList);
      }
   }
   
   /**
   * Constructor for the HashTable class
   * Inserts the contents of the given array into the Table at the appropriate indices
   * @param array: an array of elements to insert
   * @param size: the size of the Table
   * @precondition size > 0
   * @throws IllegalArgumentException size <= 0
   */
   public HashTable(T [] array, int size) throws IllegalArgumentException {
      if(size <= 0) {
         throw new IllegalArgumentException("Size cannot be less than or equal to 0");
      }
      table = new ArrayList<LinkedList<T>>(size);
      numElements = 0;
      for (int i = 0; i < size; i++) {
         table.add(new LinkedList<T>());
      }
      if(array != null) {
         for (int i = 0; i < array.length; i++) {
            add(array[i]);
         }
      }
   }
   
   /** Accessors */
   
   /**
   * Returms the index in the hash value for given object
   * @param obj the object to search for
   * @return value in the hash value in the table for the given object
   */
   private int hash(T obj) {
      return obj.hashCode() % table.size();
   }
   
   /**
   * returns the number of elements in table
   * @return number of elements in table
   */
   public int getNumElements() {
      return numElements;
   }
   
   /**
   * returns bucket location of a given element 
   * @param elemnt to find location of
   * @return index in hash table for given object (-1 if object not in table)
   * @precondition elemnt is not null
   */
   public int find(T elem) throws NullPointerException {
      if (elem == null) {
         throw new NullPointerException("find(): Element cannot be null");
      }
      int index = hash(elem);
      if (table.get(index).findIndex(elem) == -1) {
         return -1;
      }
      return index;
   }
   
   /**
   * returns element if elemtn exist in table
   * @param elem to find 
   * @return element if exists in table (null if not)
   * @precondition elem is not null
   */
   public T get(T elem) throws NullPointerException {
      if (elem == null) {
         throw new NullPointerException("get(): Element cannot be null");
      }
      int index = hash(elem);
      LinkedList<T> bucket = table.get(index);
      int nodeNum = bucket.findIndex(elem);
      if (nodeNum == -1) {
         return null;
      }
      bucket.advanceIteratorToIndex(nodeNum);
      return bucket.getIterator();
   }
   
   /**
   * Counts the number of elements at this index
   * @param index the index in the table
   * @precondition index >= 0 and index < table.size()
   * @return the count of elements at this index
   * @throws IndexOutOfBoundsException when precondition is violated
   */
   public int countBucket(int index) throws IndexOutOfBoundsException{
      if(index < 0 || index >= table.size()) {
         throw new IndexOutOfBoundsException("Index must be in the bounds of the table");
      }
      LinkedList <T> tempList = table.get(index);
      return tempList.getLength();
   }
   
   /**
   * Determines whether a specified element is in the table
   * @param elmt the element to locate
   * @return whether the element is in the table
   * @precondition elmt is not null
   * @throws NullPointerException when the precondition is violated
   */
   public boolean contains(T elmt) throws NullPointerException {
      if(elmt == null) {
         throw new NullPointerException("elmt cannot be null");
      }
      return find(elmt) != -1;
   }
   
   /** Mutators */
   
   /**
   * adds an given element to the hash table
   * @param element to add to table
   * @precondition elemnt is not null
   */
   public void add(T elem) throws NullPointerException {
      if (elem == null) {
         throw new NullPointerException("add(): Element cannot be null");
      }
      table.get(hash(elem)).addLast(elem);
      numElements++;
   }
   
   /**
   * Removes the given element from the table
   * @param elmt the element to delete
   * @return whether the element was removed from the table
   * @precondition elmt is not null
   * @throws NullPointerException when precondition is violated
   */
   public boolean delete(T elmt) throws NullPointerException {
      if (elmt == null) {
         throw new NullPointerException("delete(): Element cannot be null");
      }
      if(!contains(elmt)) {
         return false;
      }
      int index = hash(elmt);
      LinkedList<T> bucket = table.get(index);
      bucket.advanceIteratorToIndex(bucket.findIndex(elmt));
      bucket.removeIterator();
      numElements --;
      return true;
   }
   
   /**
   * Resets the hash table back to the empty state, as if the one argument constructor has just been called
   */
   public void clear() {
      int length = table.size();
      table = new ArrayList<>(length);
      for (int i = 0; i < length; i ++) {
         table.add(new LinkedList<T>());
      }
      numElements = 0;
   }
   
   /** Additional Methods */
   
   /**
   * Gets the ratio of items in table to the size of the table
   * @return ratio of items in table to the size of the table
   */
   public double getLoadFactor() {
      return (double)numElements / (double)table.size();
   }
   
   /**
   * Creates a String of all elements at a given bucket
   * @param bucket the index in the table
   * @return a String of elements separated by a space with a new line character at the end
   * @precondition bucket is >= 0 and < numElements
   * @throws IndexOutOfBoundsException when bucket is out of bounds
   */
   public String bucketToString(int bucket) throws IndexOutOfBoundsException {
      if(bucket < 0 || bucket >= table.size()) {
         throw new IndexOutOfBoundsException("bucket is out of bounds");
      }
      return table.get(bucket).toString();
   }
   
   /**
   * Creates a String of the bucket number followed by a colon, the first element at each bucket, and a
   * new line. For empty buckets, the String is the bucket number followed by a colon followed by empty
   * @return a String of all first elements at each bucket
   */
   public String rowToString() {
      StringBuilder sb = new StringBuilder();
      for(int i = 0; i < table.size(); i ++) {
         sb.append("Bucket " + i + ": ");
         if(table.get(i).isEmpty()) {
            sb.append("empty");
         }
         else {
            sb.append(table.get(i).getFirst());
         }
         sb.append("\n");
      }
      return sb.toString();
   }
   
   /**
   * Starting at the 0th bucket, and continuing in order until the last bucket, concatenates all elements at all
   * buckets into one String, with a new line between buckets and one more new line at the end of the entire String
   * @return a String of all elements in this HashTable
   */
   @Override public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < table.size(); i++) {
         LinkedList<T> bucket = table.get(i);
         if (!bucket.isEmpty()) {
            bucket.positionIterator();
            while (!bucket.offEnd()) {
               sb.append(bucket.getIterator()).append(" ");
               bucket.advanceIterator();
            }
            sb.append("\n");
         }
      }
      
      sb.append("\n"); // final newline at the end
      return sb.toString();
   }
}
