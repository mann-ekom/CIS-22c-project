import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import BufferedReader; 
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class main{
	static BST<User> users;
	static ArrayList<User> userByIndex;
	static HashTable<String> userNames;
	static HashTable<String> passwords; //key for password is still the username
	static Graph userConnections;
	static ArrayList<BST<User>> usersByInterest;
	static HashTable<Interest> interestMap;
	static User currUser;
  	
  	
  	public static void main(String[] args) {
		
		runApp();
		
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
					//getFriendRecomendations(currUser)
			//3: exit
				//write users data back to file
				//MakeFile()
	}
  	
	private static void makeGraph() {
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
	
	private static ArrayList<User> getFriendRecomendations(User curr) {
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
				if(usersByInterest.get(currId).search(userByIndex.get(i), idCmp) != null) {
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

	private static void MakeFile() {
		File file = new File("UserData.txt");
		try (PrintWriter writer = new PrintWriter(file)) {
	        for (int i = 1; i < userByIndex.size(); i++) {
	        	writer.println(userByIndex.get(i).toString());
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
		
	}

	// --------===========================================================================
	public void runApp() {
    	loadData();
		Scanner sc = new Scanner(System.in);
    	System.out.println("\n╔════════════════════════════════════════╗");
    	System.out.println("║   Welcome to the Social Network App    ║");
   		 System.out.println("╚════════════════════════════════════════╝");

    	while (true) {   
        	System.out.println("\n--- Main Menu ---");
        	System.out.println("1. Login");
        	System.out.println("2. Create New Account");
        	System.out.println("3. Quit");
        	System.out.print("Enter choice: ");

        	String choice = sc.nextLine().trim();

        	switch (choice) {
            	case "1":
                	login();
                	continue; 
            	case "2":
                	createAccount();
                	continue;
            	case "3":
                	saveData();
                	System.out.println("Goodbye!");
                	return;   
            	default:
                	System.out.println("Invalid choice. Please try again.");
        	}
    	}
		sc.close();
	}



   ublic static void loadData() {
 		try (BufferedReader reader = new BufferedReader(new FileReader("UserData.txt"))) {
        // First pass: Create all users and interests
        	List<String> lines = new ArrayList<>();
           	String line;
           	while ((line = reader.readLine()) != null) {
            	lines.add(line);
           	}

			// Parse users
        	int i = 0;
        	List<UserData> userDataList = new ArrayList<>();
        
        	while (i < lines.size()) {
            	if (lines.get(i).trim().isEmpty()) {
                	i++;
                	continue;
           		}	
            
            	UserData userData = new UserData();
            	userData.id = Integer.parseInt(lines.get(i++).trim());
            	userData.name = lines.get(i++).trim();
           		String[] nameParts = userData.name.split(" ", 2);
            	userData.firstName = nameParts[0];

            	// ← FIXED (removed ternary)
            	if (nameParts.length > 1) {
                	userData.lastName = nameParts[1];
            	} else {
                	userData.lastName = "";
            	}

            	userData.username = lines.get(i++).trim();
            	userData.password = lines.get(i++).trim();
            	userData.numFriends = Integer.parseInt(lines.get(i++).trim());
            
            	userData.friendIds = new ArrayList<>();
            	for (int j = 0; j < userData.numFriends; j++) {
                	userData.friendIds.add(Integer.parseInt(lines.get(i++).trim()));
            	}
            
            	userData.city = lines.get(i++).trim();
            	userData.numInterests = Integer.parseInt(lines.get(i++).trim());
            
            	userData.interests = new ArrayList<>();
            	for (int j = 0; j < userData.numInterests; j++) {
                	userData.interests.add(lines.get(i++).trim());
           		}
            
            	userDataList.add(userData);
        	}        
        
        	// Create all users
        	for (UserData userData : userDataList) {
            	User user = new User(userData.id, userData.firstName + " " + userData.lastName,
                                 userData.username, userData.password, userData.city);
            
            	// Ensure userById list is large enough
            	while (userByIndex.size() <= userData.id) {
            		userByIndex.add(null);
            	}
            	userByIndex.set(userData.id, user);
            
            	//userCredentials.put(userData.username, user);
            	//allUsers.insert(user);
            
            	// Process interests
            	for (String interestName : userData.interests) {
            		Interest temp = new Interest(-1, interestName);
               		Interest interest = interestMap.get(temp);
                	if (interest == null) {
                    	interest = new Interest(interestMap.getNumElements(), interestName);
                    	interestMap.add(interest);
                    
                    	// Ensure usersByInterest list is large enough
                    	while (usersByInterest.size() <= interest.getId()) {
                        usersByInterest.add(new BST<>());
                   		}
                	}
                	user.addInterest(interest);
                	NAME_COMPARATOR nameCmp = new NAME_COMPARATOR();
                	usersByInterest.get(interest.getId()).insert(user, nameCmp);
            	}
        	}
        
        	// Second pass: Add friendships
        	makeGraph();
        
        	System.out.println("Data loaded successfully! " + userDataList.size() + " users loaded.");
        
		} catch (FileNotFoundException e) {
       		System.out.println("Data file not found. Starting with empty database.");
       		userConnections = new Graph(100); // Default size
    	} catch (IOException e) {
        	System.out.println("Error reading data file: " + e.getMessage());
        	userConnections = new Graph(100);
    	} catch (Exception e) {
        	System.out.println("Error parsing data: " + e.getMessage());
        	e.printStackTrace();
        	userConnections = new Graph(100);
    	}
	}

	

	private void login(){
		Scanner sc = new Scanne(System.in);
		try {
			System.out.println("Please enter your username");
		}
		
		
	}

	private void createAccount(){
	}

	


	//---------Helper method----------------------
	/**
     * Helper class for user data during file loading
     */
    private static class UserData {
        int id;
        String name;
        String firstName;
        String lastName;
        String username;
		String password;
        int numFriends;
        ArrayList<Integer> friendIds;
        String city;
        int numInterests;
        ArrayList<String> interests;
    }
			
}
