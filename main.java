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
  	
  	
  	public static void main(String[] args) {
		setup();
		login();
		//ask user for options
			//1: view my friends 
				// all friends (sort by name)
				// search for friend (by name, edge case friends with same name)
					//view profile
					//remove friend
			//2: make friends
				//search by name
				//search by interest
				//get recomended friends (mason is working on this)
			//3: exit
				//write users data back to file
				MakeFile()
	}
  	
  	private void setup() {
		//read file
		//make Users
		//add Users to BST and ArrayList
		//add usernames and passwords to hashtables
		//make graph
		makeGraph();
		//add intersts to hash table
		//creats an arraylist where each index is an BST of all people who share 1 interest
	}

	private void login() {
		//scans for username and password
		//search for username in hash table
		
		//if not exist create new user
		newUser( userName, password);
		//set new user to user
		
		//if exists check for password match
		//if password not match re ask password?
		//if password match set current user to user
	}

	private void newUser(String userName, String password) {
		//ask for name
		//ask for interests
		//check if interests exist and add stuff acordingly
		//create new user
		//add to BST, ArrayLists and Hash tables
		//redo graph
		makeGraph();
	}
  	
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
  


	private LinkedList<User> getFriendRecomendations(User curr) {
		int loc = curr.getId();
		userConnections.BFS(loc);
		
		
		return null;
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
