import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class main{
	BST<User> users;
	ArrayList<User> userByIndex;
  	Hashtable<String> userNames;
  	HashTable<String> passwords; //key for password is still the username
  	Graph userConnections;
  	ArrayList<BST<User>> sharedInterest;
	User currUser;
  	
  	
  	public static void main(String[] args) {
		setup();
		login();
		//ask user for options 1, 2, or 3 (1 and 2 will open sub options)
			//1: view my friends 
				// all friends (sort by name)
				// search for friend (by name, edge case friends with same name)
					//view profile (name, username, friends?)
					//remove friend
			//2: make friends
				//search by name
				//search by interest
				//get recomended friends (mason is working on this)
					getFriendRecomendations(User curr)
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
		//creats an arraylist where each index is an interest, and in each bucket is BST of all people who share that interest
	}

	private void login() {
		//scans for username and password
		//search for username in hash table
		
		//if not exist create new user
		newUser( userName, password);
		//set current user to new user
		
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
		userConnections = new Graph(userByIndex.size() - 1);
		User current;
		for (int i = 1; i <= userConnections.getNumVertices(); i++) {
			current = userByIndex.get(i);
			for(int j = 0; j < current.getFriendIds().getLength(); j++) {
				current.getFriendIds().advanceIteratorToIndex(j);
				userConnections.addDirectedEdge(i, current.getFriendIds().getIterator());
			}
		}
	}

	
	private ArrayList<User> getFriendRecomendations(User curr) {
		//breath first search to find distances of all friends
		userConnections.BFS(curr.getId());
		
		//starts recommendations based on BFS distances
		//the user, their direct friends, and any unconnected people are not recommended
		ArrayList<Double> recValues = new ArrayList(userByIndex.size());
		recValues.add(-1.0);
		for (int i = 1; i < userByIndex.size(); i++) {
			if(userConnections.getDistance(i) > 1)
				recValues.add((double) userConnections.getDistance(i));
			else 
				recValues.add(-1.0);
		}
		
		//checks user's interests and makes people who share those interests recommended more
		ID_COMPARATOR idCmp = new ID_COMPARATOR();
		curr.getInterests().positionIterator();
		while (!curr.getInterests().offEnd()) {
			int currId = curr.getInterests().getIterator().getId();
			 for (int i = 1; i < userByIndex.size(); i++) {
				if(sharedInterest.get(currId).search(userByIndex.get(i), idCmp) != null) {
					recValues.set(i, recValues.get(i) / 2);
				}
			 }
		}
		
		//places recommended people in order
		int recSize = 0;
		for (int i = 1; i < recValues.size(); i++) {
			if (recValues.get(i) > 0) recSize++;
		}
		ArrayList<User> recommend = new ArrayList(recSize);
		for (int i = 0; i < recSize; i++) {
			int tempIndex = 0;
			for (int j = 1; j < recValues.size(); j++) {
				if (recValues.get(j) > 0 && recValues.get(j) < recValues.get(tempIndex)) {
					tempIndex = j;
				}
			}
			recommend.add(userByIndex.get(tempIndex));
			recValues.set(tempIndex, 0.0);
		}
		return recommend;
	}
	
  	private void MakeFile() {
		File file = new File("UserData.txt");
		try (PrintWriter writer = new PrintWriter(file)) {
	        for (int i = 0; i < users.getSize(); i++) {
	        	writer.println(User.toString());
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
		
	}
}
