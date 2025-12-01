import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import java.io.BufferedReader; 
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class main{
	static BST<User> users;
	static ArrayList<User> userByIndex;
	static HashTable<String> usernamePassword;
	static Graph userConnections;
	static HashTable<String> usernamePass
	static ArrayList<BST<User>> usersByInterest;
	static HashTable<Interest> interestMap;
	static String [] interests;
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
	public static void runApp() {
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
                	makeFile();
					sc.close();
                	System.out.println("Goodbye!");
                	return;   
            	default:
                	System.out.println("Invalid choice. Please try again.");
        	}
    	}
		
	}



   public static void loadData() {
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
            
            	userData.friendIds = new LinkedList<>();
            	for (int j = 0; j < userData.numFriends; j++) {
                	userData.friendIds.addLast(Integer.parseInt(lines.get(i++).trim()));
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
        	userByIndex = new ArrayList<>();
        	usersByInterest = new ArrayList<>();
        	users = new BST<>();
        	usernamePassword = new HashTable<>(100);
        	interestMap = new HashTable<>(100);
        	NAME_COMPARATOR nameCmp = new NAME_COMPARATOR();
        	
        	for (UserData userData : userDataList) {
            	User user = new User(userData.id, userData.firstName + " " + userData.lastName,
                                 userData.username, userData.password, userData.city);
        		user.setFriendIds(userData.friendIds);
            
            	// Ensure userById list is large enough
            	while (userByIndex.size() <= userData.id) {
            		userByIndex.add(null);
            	}
            	userByIndex.set(userData.id, user);
            
            	usernamePassword.add(userData.username + userData.password);
            	users.insert(user, nameCmp);
            
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
                	usersByInterest.get(interest.getId()).insert(user, nameCmp);
            	}
        	}
        
        	// Second pass: Add friendships
        	makeGraph();
        	for (User user : userByIndex) {
        		if(user != null) {
        			user.getFriendIds().positionIterator();
        			while (!user.getFriendIds().offEnd()) {
        				user.addFriend(userByIndex.get(user.getFriendIds().getIterator()));
        				user.getFriendIds().advanceIterator();
        			}
        		}
        	}
        
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

	

	private static void login(){
		Scanner sc = new Scanner(System.in);
	    System.out.println("Enter username:");
	    String inputUsername = sc.nextLine().trim();
	    System.out.println("Enter password:");
	    String inputPassword = sc.nextLine();
	    User tempUser = new User(inputUsername, "");
	    User storedUser = usernamePass.get(tempUser);
		
	    if (storedUser == null) {
	        System.out.println("Username not found.");
	    }
		else if (storedUser.getPassword().equals(inputPassword)) {
	        System.out.println("Login successful! Welcome, " + storedUser.getName() + "!");
	        currUser = storedUser;
		}
		else {
	        System.out.println("Incorrect password.");
	    }
		sc.close();
		
	}

	private static void createAccount(){
    // master interest list
	    String[] interestList = {
	        "Fitness", "Philanthropy", "Motorsport", "Environment",
	        "Social Justice", "Art", "Comedy", "Theater", "Martial Arts"
	    };
	
	    Scanner sc = new Scanner(System.in);
	
	    System.out.println("Please enter your full name:");
	    String fullName = sc.nextLine().trim();
	
	    // get username, ensure unique (check BST of users)
	    System.out.println("Please type your desired username:");
	    String username = sc.nextLine().trim();
	
	    // create temporary User used only for searching by username
	    User tempUserForSearch = new User(username, null);
	    while (users.search(tempUserForSearch) != null) {
	        System.out.println("Sorry that username is already taken, please try again.");
	        username = sc.nextLine().trim();
	        tempUserForSearch = new User(username, null);
	    }
	
	    System.out.println("Please type your password:");
	    String password = sc.nextLine();
	
	    // select interests
	    System.out.println("Please select your interests. Enter numbers one at a time.");
	    System.out.println("Enter 0 when finished.\n");
	
	    // show numbered list
	    for (int i = 0; i < interestList.length; i++) {
	        System.out.printf("%d: %s%n", i + 1, interestList[i]);
	    }
	
	    // collect chosen interest indices -> interest names
	    ArrayList<String> chosenInterests = new ArrayList<>();
	    while (true) {
	        System.out.print("Choice (0 to finish): ");
	        String line = sc.nextLine().trim();
	        if (line.isEmpty()) {
	            System.out.println("No input; try again.");
	            continue;
	        }
	
	        int choice;
	        try {
	            choice = Integer.parseInt(line);
	        } catch (NumberFormatException e) {
	            System.out.println("Invalid number. Please enter a number from the list or 0 to finish.");
	            continue;
	        }
	
	        if (choice == 0) break;
	
	        if (choice < 1 || choice > interestList.length) {
	            System.out.println("Choice out of range. Try again.");
	            continue;
	        }
	
	        String interestName = interestList[choice - 1];
	        if (!chosenInterests.contains(interestName)) {
	            chosenInterests.add(interestName);
	            System.out.println("Added: " + interestName);
	        } else {
	            System.out.println("You already selected " + interestName);
	        }
	    }
	
	    System.out.println("Please enter your hometown:");
	    String city = sc.nextLine().trim();
	
	    // Determine a new user id.
	    // If you maintain userByIndex (ArrayList<User>) use its size as next id.
	    // Otherwise fall back to BST size + 1.
	    int newId = 0;
	    try {
	        // try userByIndex if available
	        newId = userByIndex.size();         // if userByIndex index starts at 0 and you want id starting at 1 adjust accordingly
	    } catch (Exception e) {
	        // fallback: use BST size + 1 (assumes users.getSize() exists)
	        newId = users.getSize() + 1;
	    }
	
	    // Build LinkedList<String> of interestStrings using your project's LinkedList class
	    LinkedList<String> interestStringsLL = new LinkedList<>();
	    for (String s : chosenInterests) {
	        interestStringsLL.addLast(s);
	    }
	
	    // Empty friend list; use your LinkedList<Integer> type
	    LinkedList<Integer> friendIdsLL = new LinkedList<>();
	
	    // Create the full User using the constructor you provided:
	    // User(int id, String name, String username, String password, int totalFriends,
	    //      LinkedList<Integer> friendIds, String city, int totalInterests,
	    //      LinkedList<String> interestStrings, LinkedList<Interest> interests)
	    LinkedList<Interest> emptyInterests = new LinkedList<>(); // no Interest objects yet
	    User newUser = new User(
	        newId,
	        fullName,
	        username,
	        password,
	        0,                   // totalFriends
	        friendIdsLL,
	        city,
	        interestStringsLL.getLength(), // totalInterests
	        interestStringsLL,
	        emptyInterests
	    );
	
	    // Insert into BST of users
	    users.insert(newUser);
	
	    // If you also keep an ArrayList userByIndex, ensure list large enough and set index
	    try {
	        while (userByIndex.size() <= newId) userByIndex.add(null);
	        userByIndex.set(newId, newUser);
	    } catch (Exception ignored) { /* no userByIndex in this project; ignore */ }
	
	    // If you keep a hash table of Users (HashTable<User>), add it there too:
	    try {
	        userTable.add(newUser); // rename userTable to your actual HashTable<User> field if you have it
	    } catch (Exception ignored) { /* if you don't have a HashTable<User> field, ignore */ }
	
	    // If you keep a username->password hashtable/map, add mapping
	    try {
	        passwords.put(username, password); // if you have a Hashtable<String,String> passwords field
	    } catch (Exception ignored) { /* ignore if passwords map not present */ }
	
	    // If you track Interests mapping (Interest objects), add creation / linking here:
	    // For each interestString, you probably want to find/create the corresponding Interest object
	    // and call newUser.addInterest(interest) and usersByInterest.get(interestId).insert(newUser).
	    // That logic depends on how you store Interest objects in your project.
	
	    System.out.println("Account created successfully for " + fullName + " (" + username + ").");
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
        LinkedList<Integer> friendIds;
        String city;
        int numInterests;
        ArrayList<String> interests;
    }

	/**
 	 * User menu after login
 	 */
	private void userMenu() {
    	boolean loggedIn = true;
    
    	while (loggedIn) {
        	System.out.println("\n" + currtUser.getDetailedProfile()); // Need help here: implement how to display the data of the current User?
        	System.out.println("--- User Menu ---");
        	System.out.println("1. View My Friends");
        	System.out.println("2. Make New Friends");
        	System.out.println("3. Logout");
       		System.out.print("Enter choice: ");
        
        	String choice = scanner.nextLine().trim();
        
        	switch (choice) {
            	case "1":
                	viewFriendsMenu();
                	continue;  
            	case "2":
                	makeNewFriendsMenu();
                	continue;  
            	case "3":
                	loggedIn = false;
               		System.out.println("Logged out successfully.");
                	continue;  
            	default:
                	System.out.println("Invalid choice. Please try again.");
                	// no need for continue here, loop continues naturally
        	}
    	}
	}

	/**
 	 * View Friends submenu
	 */
	private void viewFriendsMenu() {
    	if (currUser.getFriendsByName().isEmpty()) {
       		System.out.println("\nYou have no friends yet. Start making connections!");
        	return;
    	}
    
    	boolean back = false;
    	while (!back) {
        	System.out.println("\n--- View My Friends ---");
        	System.out.println("1. View Friends Sorted by Name");
        	System.out.println("2. Search by Friend Name");
        	System.out.println("3. Back");
        	System.out.print("Enter choice: ");
        
        	String choice = scanner.nextLine().trim();
        
        	switch (choice) {
            	case "1":
                	viewFriendsSorted();
                	continue;  
            	case "2":
                	searchFriendByName();
                	continue;  
            	case "3":
               		back = true;
                	continue;  
            	default:
                System.out.println("Invalid choice. Please try again.");  
        	}
    	}
	}
			
}
