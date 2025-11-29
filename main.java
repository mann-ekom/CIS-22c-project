import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class main{
	BST Users;
	ArrayList<User> UserByIndex;
  	Hashtable UserNames;
  	HashTable Passwords;
  	Graph UserConnections;
  	ArrayList BST SharedInterest;
  	int numUsers;
  	
  	
  	
  	
  	
  	
  	
	private void makeGraph() {
		userConnections = new Graph(UserByIndex.size() - 1);
		User current;
		for (int i = 1; i <= userConnections.getNumVertices(); i++) {
			current = UserByIndex.get(i);
			for(int j = 0; j < current.getFriendIds().getLength(); j++) {
				current.getFriendIds().advanceIteratorToIndex(j);
				userConnections.addDirectedEdge(i, current.getFriendIds().getIterator());
			}
		}
	}
  
  
  	private void MakeFile() {
		File file = new File("UserData.txt");
		try (PrintWriter writer = new PrintWriter(file)) {
	        for (int i = 0; i < numUsers; i++) {
	        	writer.println(User.toString());
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
		
	}
}
