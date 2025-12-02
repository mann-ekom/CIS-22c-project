import java.util.ArrayList;

public class Graph {
   private int vertices;
   private int edges;
   private ArrayList<LinkedList<Integer>> adj;
   private ArrayList<Character> color;
   private ArrayList<Integer> distance;
   private ArrayList<Integer> parent;
   private ArrayList<Integer> discoverTime;
   private ArrayList<Integer> finishTime;
   private static int time = 0;

   /** Constructors and Destructors */
   
   /**
   * constructs default Graph with given amount of verticies
   * @param amount of verticies in graph
   * @precondition param must be >zero
   * @throws IllegalArgumentException when precondition is violated
   */
   public Graph(int numVtx) throws IllegalArgumentException {
      if (numVtx <= 0)
         throw new IllegalArgumentException("Graph(): Cannot create Graph with less than 1 vertex");
      
      vertices = numVtx;
      edges = 0;
      adj = new ArrayList(numVtx + 1);
      color = new ArrayList(numVtx + 1);
      distance = new ArrayList(numVtx + 1);
      parent = new ArrayList(numVtx + 1);
      discoverTime = new ArrayList(numVtx + 1);
      finishTime = new ArrayList(numVtx + 1);
      
      for(int i = 0; i <= numVtx; i++) {
         adj.add(new LinkedList());
         color.add('W');
         distance.add(-1);
         parent.add(0);
         discoverTime.add(-1);
         finishTime.add(-1);
      }
   }

   /*** Accessors ***/
   
   /**
     * Returns the number of verticies in graph
     * @return number of verticies in graph
     */
   public int getNumVertices() {
      return vertices;
   }
   
   /**
     * Returns the number of verticies in graph
     * @return number of verticies in graph
     */
   public int getNumEdges() {
      return edges;
   }
   
   /**
     * Returns if graph has no edges
     * @return is graph empty
     */
   public boolean isEmpty() {
      return edges == 0;
   }
   
   /**
     * Returns the color of given vertex
     * @param a of vertex in the graph
     * @return color of given vertex
     */
   public Character getColor(Integer v) throws IndexOutOfBoundsException {
      if (v <= 0 || v > vertices)
         throw new IndexOutOfBoundsException("getColor(): vertex is out of bounds of graph");
      return color.get(v);
   }
   
   /**
     * Returns the color of given vertex
     * @param a of vertex in the graph
     * @return color of given vertex
     */
   public Integer getDistance(Integer v) throws IndexOutOfBoundsException {
      if (v <= 0 || v > vertices)
         throw new IndexOutOfBoundsException("getColor(): vertex is out of bounds of graph");
      return distance.get(v);
   }
   
   /**
     * Returns the color of given vertex
     * @param a of vertex in the graph
     * @return color of given vertex
     */
   public Integer getParent(Integer v) throws IndexOutOfBoundsException {
      if (v <= 0 || v > vertices)
         throw new IndexOutOfBoundsException("getColor(): vertex is out of bounds of graph");
      return parent.get(v);
   }
   
   /**
     * Returns the color of given vertex
     * @param a of vertex in the graph
     * @return color of given vertex
     */
   public Integer getDiscoverTime(Integer v) throws IndexOutOfBoundsException {
      if (v <= 0 || v > vertices)
         throw new IndexOutOfBoundsException("getColor(): vertex is out of bounds of graph");
      return discoverTime.get(v);
   }
   
   /**
     * Returns the color of given vertex
     * @param a of vertex in the graph
     * @return color of given vertex
     */
   public Integer getFinishTime(Integer v) throws IndexOutOfBoundsException {
      if (v <= 0 || v > vertices)
         throw new IndexOutOfBoundsException("getColor(): vertex is out of bounds of graph");
      return finishTime.get(v);
   }
   
   /**
     * Returns the color of given vertex
     * @param a of vertex in the graph
     * @return color of given vertex
     */
   public LinkedList getAdjacencyList(Integer v) throws IndexOutOfBoundsException {
      if (v <= 0 || v > vertices)
         throw new IndexOutOfBoundsException("getColor(): vertex is out of bounds of graph");
      return adj.get(v);
   }

   /*** Manipulation Procedures ***/
   
   /**
   * adds an edge between to vertices
   * @param a of vertex in the graph
   * @param a of vertex in the graph
   * @precondition vertices must be within graph bounds (0 <= u,v < vertices)
   * @throws IllegalArgumentException when precondition is violated
   */
   public void addUndirectedEdge(Integer u, Integer v) throws IndexOutOfBoundsException {
      if (u <= 0 || v <= 0 || u > vertices || v > vertices)
         throw new IndexOutOfBoundsException("addUndirectedEdge(): a vertex was out of bounds of graph");
      adj.get(u).addLast(v);
      adj.get(v).addLast(u);
      edges++;
   }
   
   /**
   * adds an edge from one vertex to another
   * @param a of vertex in the graph
   * @param a of vertex in the graph
   * @precondition vertices must be within graph bounds (0 <= u,v < vertices)
   * @throws IllegalArgumentException when precondition is violated
   */
   public void addDirectedEdge(Integer u, Integer v) throws IndexOutOfBoundsException {
      if (u <= 0 || v <= 0 || u > vertices || v > vertices)
         throw new IndexOutOfBoundsException("addDirectedEdge(): a vertex was out of bounds of graph");
      adj.get(u).addLast(v);
      edges++;
   }

   /**
     * Removes an edge between two users
     * @param userId1 first user's ID
     * @param userId2 second user's ID
     */
    public void removeEdge(int userId1, int userId2) {
        if (userId1 >= 0 && userId1 < Vertices && 
            userId2 >= 0 && userId2 < Vertices) {
            
            adj.get(userId1).remove(Integer.valueOf(userId2));
            adj.get(userId2).remove(Integer.valueOf(userId1));
        }
    }

   /*** Additional Operations ***/

   /**
   * creats a string of each vertex and their adjacency data
   * @return the graph as a readable string
   */
   @Override public String toString() {
      String result = "";
      for (int i = 1; i <= vertices; i++) {
         result += i + ": " + adj.get(i).toString();
      }
      return result;
   }
   
   /**
   * uses breath first search to update all the distances from vertex at given index 
   * @param Integer Index of source
   * @precondition vertices must be within graph bounds (0 <= u,v < vertices)
   * @throws IndexOutOfBoundsException when precondition is violated
   */
   public void BFS(Integer source) throws IndexOutOfBoundsException {
      if(source <= 0 || source > vertices)
         throw new IndexOutOfBoundsException("BFS(): source is out of bounds of graph");
      LinkedList<Integer> queue = new LinkedList();
      for(int i = 1; i <= vertices; i++) {
         color.set(i, 'W');
         distance.set(i, -1);
         parent.set(i, 0);
      }
      color.set(source, 'G');
      distance.set(source, 0);
      queue.addLast(source);
      Integer currVert;
      LinkedList<Integer> currAdj;
      while(!queue.isEmpty()) {
         currVert = queue.getFirst();
         queue.removeFirst();
         currAdj = adj.get(currVert);
         currAdj.positionIterator();
         while(!currAdj.offEnd()) {
            Integer index = currAdj.getIterator();
            if(color.get(index) == 'W') {
               color.set(index, 'G');
               distance.set(index, distance.get(currVert) + 1);
               parent.set(index, currVert);
               queue.addLast(index);
            }
            currAdj.advanceIterator();
         }
         color.set(currVert, 'B'); 
      }
      
   }
   /**
   * Depth-First Search over the whole graph.
   * Initializes metadata and calls recursive Visit on each undiscovered vertex.
   */
   public void DFS() {
      for (int i = 0; i < vertices; i++) {
         color.set(i, 'W');
         parent.set(i, 0);          // NIL represented as 0
         discoverTime.set(i, -1);
         finishTime.set(i, -1);
      }
      time = 0;
         
      for (int i = 0; i < vertices; i++) {
         if (color.get(i) == 'W') {
            visit(i); // pass 0-based index
         }
      }
   }

   /**
    * Recursive visit routine for DFS.
    * @param x 0-based index of vertex to visit
    */
   private void visit(int x) {
      color.set(x, 'G');
      discoverTime.set(x, ++time);
      
      LinkedList<Integer> currAdj = adj.get(x);
      currAdj.positionIterator();
      while (!currAdj.offEnd()) {
         Integer yVertexNumber = currAdj.getIterator(); // y is 1..N (vertex number)
         int yIndex = yVertexNumber - 1;                // convert to 0-based index
         
         if (color.get(yIndex) == 'W') {
            // store parent as a 1-based vertex number to match BFS convention
            parent.set(yIndex, x + 1);
            visit(yIndex);
         }
         
         currAdj.advanceIterator();
      }
      color.set(x, 'B');
      finishTime.set(x, ++time);
   }
}
