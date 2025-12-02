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
	static Graph userConnections;
	static HashTable<User> usernamePass;
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
	
	private static ArrayList<User> getFriendRecommendations(User curr) {
		//breath first search to find distances of all friends
		userConnections.BFS(curr.getId());
		//starts recommendations based on BFS distances
		//the user, their direct friends, and any unconnected people are not recommended
		ArrayList<Double> recValues = new ArrayList<>(userByIndex.size());
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
			 curr.getInterests().advanceIterator();
		}
		
		//places recommended people in order
		int recSize = 0;
		for (int i = 1; i < recValues.size(); i++) {
			if (recValues.get(i) > 0) recSize++;
		}
		ArrayList<User> recommend = new ArrayList<>(recSize);
		for (int i = 0; i < recSize; i++) {
			int tempIndex = 0;
			for (int j = 1; j < recValues.size(); j++) {
				if (recValues.get(tempIndex) > 0) {
					if (recValues.get(j) > 0 && recValues.get(j) < recValues.get(tempIndex)) {
						tempIndex = j;
					}
				} else {
					if (recValues.get(j) > 0) {
						tempIndex = j;
					}
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
		
    	System.out.println("\n╔════════════════════════════════════════╗");
    	System.out.println("║   Welcome to the Social Network App    ║");
   		System.out.println("╚════════════════════════════════════════╝");

    	while (true) {  
    		Scanner sc = new Scanner(System.in);
        	System.out.println("\n--- Main Menu ---");
        	System.out.println("1. Login");
        	System.out.println("2. Create New Account");
        	System.out.println("3. Quit");
        	System.out.print("Enter choice: ");
        	
        	
	        //System.out.println(usernamePass.toString());
	       
        	String choice = sc.nextLine().trim();
        	
        	switch (choice) {
            	case "1":
                	login();
                	continue; 
            	case "2":
                	createAccount();
                	continue;
            	case "3":
                	//MakeFile();
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
        	usernamePass = new HashTable<>(100);
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
            
            	usernamePass.add(user);
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
	    String inputPassword = sc.nextLine().trim();
	    User tempUser = new User(inputUsername, "");
	    User storedUser = usernamePass.get(tempUser);
		
	    if (storedUser == null) {
	        System.out.println("Username not found.");
	    }
		else if (storedUser.getPassword().equals(inputPassword)) {
	        System.out.println("Login successful! Welcome, " + storedUser.getName() + "!");
	        currUser = storedUser;
	        userMenu();
		}
		else {
	        System.out.println("Incorrect password.");
	    }
	}

	private static void createAccount(){
    // master interest list
		NAME_COMPARATOR nameCmp = new NAME_COMPARATOR();
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
	    while (users.search(tempUserForSearch, nameCmp) != null) {
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
	    int newId = -1;
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
	    LinkedList<Interest> interestsLL = new LinkedList<>(); // no Interest objects yet
	    for (String s : chosenInterests) {
	    	Interest tempinterest = new Interest(-1, s);
	    	interestsLL.addLast(interestMap.get(tempinterest));
	    }
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
	        interestsLL
	    );
	
	    // Insert into BST of users
	    users.insert(newUser, nameCmp);

		newUser.getInterests().positionIterator();
	    while (!newUser.getInterests().offEnd()) {
	    	usersByInterest.get(interestMap.get(newUser.getInterests().getIterator()).getId()).insert(newUser, nameCmp);
	    	newUser.getInterests().advanceIterator();
	    }
	
	    // If you also keep an ArrayList userByIndex, ensure list large enough and set index
	    try {
	        while (userByIndex.size() <= newId) userByIndex.add(null);
	        userByIndex.set(newId, newUser);
	    } catch (Exception ignored) { /* no userByIndex in this project; ignore */ }
	
	    // If you keep a hash table of Users (HashTable<User>), add it there too:
	    try {
	        usernamePass.add(newUser); // rename userTable to your actual HashTable<User> field if you have it
	    } catch (Exception ignored) { /* if you don't have a HashTable<User> field, ignore */ }
	
	
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

	
	//------------------------------------------------------------------------------------------------
	/**
 	 * User menu after login
 	 */
	private static void userMenu() {
    	boolean loggedIn = true;
    
    	while (loggedIn) {
    		Scanner sc = new Scanner(System.in);
        	//System.out.println("\n" + currUser.getDetailedProfile()); // Need help here: implement how to display the data of the current User?
        	System.out.println("--- User Menu ---");
        	System.out.println("1. View My Friends");
        	System.out.println("2. Make New Friends");
        	System.out.println("3. Logout");
       		System.out.print("Enter choice: ");
        
        	String choice = sc.nextLine().trim();
        
        	switch (choice) {
            	case "1":
                	//viewFriendsMenu();
                	continue;  
            	case "2":
                	//makeNewFriendsMenu();
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
	private static void viewFriendsMenu() {
    	if (currUser.getFriendsByName().isEmpty()) {
       		System.out.println("\nYou have no friends yet. Start making connections!");
        	return;
    	}
    
    	boolean back = false;
    	while (!back) {
    		Scanner sc = new Scanner(System.in);
        	System.out.println("\n--- View My Friends ---");
        	System.out.println("1. View Friends Sorted by Name");
        	System.out.println("2. Search by Friend Name");
        	System.out.println("3. Back");
        	System.out.print("Enter choice: ");
        
        	String choice = sc.nextLine().trim();
        
        	switch (choice) {
            	case "1":
                	viewFriendsSorted();
                	continue;  
            	case "2":
                	//searchFriendByName();
                	continue;  
            	case "3":
               		back = true;
                	continue;  
            	default:
                System.out.println("Invalid choice. Please try again.");  
        	}
    	}
	}

	/**
     * Display friends sorted by name
     */
    private static void viewFriendsSorted() {
        System.out.println("\n--- Your Friends (Sorted by Name) ---");
        ArrayList<User> friends = currUser.getFriendsByName().toArrayList(); // created toArrayList() in BST<T> class for this
        
        for (int i = 0; i < friends.size(); i++) {
            System.out.println((i + 1) + ". " + friends.get(i).getName() + 
                             " (" + friends.get(i).getCity() + ")");
        }
        
        System.out.print("\nEnter friend number to view profile (or 0 to go back): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice > 0 && choice <= friends.size()) {
                viewFriendProfile(friends.get(choice - 1));
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

	/**
     * Search for a friend by name
     */
    private void searchFriendByName() {
        System.out.print("\nEnter name: ");
        String name = scanner.nextLine().trim();        
        ArrayList<User> allMatches = currtUser.getFriendsByName().searchByName(name);
        
        if (allMatches.isEmpty()) {
            System.out.println("No friends found with that name.");
        } else {
        	System.out.println("\nFriends found:");
        	for (int i = 0; i < allMatches.size(); i++) {
                System.out.println((i + 1) + ". " + allMatches.get(i).getName() + 
                                 " (" + allMatches.get(i).getCity() + ")");
            }
            
            System.out.print("\nEnter friend number to view profile (or 0 to go back): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice > 0 && choice <= allMatches.size()) {
                    viewFriendProfile(allMatches.get(choice - 1));
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }
	
	/**
     * View a friend's full profile with option to remove
     */
    private void viewFriendProfile(User friend) {
        System.out.println("\n" + friend.toString()); 
        
        System.out.println("1. Remove this friend");
        System.out.println("2. Back");
        System.out.print("Enter choice: ");
        
        String choice = scanner.nextLine().trim();
        if (choice.equals("1")) {
            removeFriend(friend);
        }
    }

	/**
     * Remove a friend
     */
    private void removeFriend(User friend) {
        System.out.print("Are you sure you want to remove " + friend.getName() + 
						 " from your friends? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("yes")) {
            currUser.removeFriend(friend);
            friend.removeFriend(currUser);
	        userConnections.removeEdge(currUser.getId(), friend.getId());						// created removeEdge() in Graph
            System.out.println(friend.getName() + " has been removed from your friends.");
		} else {
            System.out.println("Friend not removed.");
        	}
    }


	
	//-----------------------------------------------------------------------------------------------------------
	
	public static void makeNewFriendsMenu() {
		Scanner sc = new Scanner(System.in);
		boolean back = false;
    	while (!back) {
        	System.out.println("\n--- Make New Friends ---");
        	System.out.println("1. Search for Users by name");
        	System.out.println("2. Search Users by Interests");
        	System.out.println("3. Get Friend Recomendations");
        	System.out.println("4. Back");m
        	System.out.print("Enter choice: ");
        
        	String choice = sc.nextLine().trim();
        
        	switch (choice) {
            	case "1":
                	addFriendsByName();
                	continue;  
            	case "2":
                	//addFriendsByInterest();
                	continue;
            	case "3":
                	//addFriendsByRecommendation();
                	continue;  
            	case "4":
               		back = true;
                	continue;  
            	default:
                System.out.println("Invalid choice. Please try again.");  
        	}
    	}
	}

	public static void addFriendsByName() {
	    Scanner sc = new Scanner(System.in);

	    while (true) {
	        System.out.println("Enter the person's name (or press Enter to return):");
	        String name = sc.nextLine();
	        if (name == null || name.trim().isEmpty()) {
	            System.out.println("Returning to menu.");
	            break;
	        }
	        User tempUser = new User(name, "");
	        User storedUser = users.get(tempUser);
	        BST<User> copy = users;
	        copy.remove(tempUser);
	        User storedUser2 = copy.get(tempUser);
	        if (storedUser == null) {
	            System.out.println("Person not found.");
	            continue;
	        }
	
	        if (storedUser2 != null && !storedUser2.equals(storedUser)) {
	            int indexUser1 = currUser.friendIds.findIndex(storedUser.getId());
	            int indexUser2 = currUser.friendIds.findIndex(storedUser2.getId());
	
	            System.out.println("1: " + storedUser + (indexUser1 != -1 ? " (already a friend)" : ""));
	            System.out.println("2: " + storedUser2 + (indexUser2 != -1 ? " (already a friend)" : ""));
	            System.out.println("Choose 1 or 2 to add as a friend (0 to cancel):");
	
	            int choice;
	            if (!sc.hasNextInt()) {
	                System.out.println("Invalid input. Please enter a number.");
	                sc.nextLine();
	                continue;
	            }
	            choice = sc.nextInt();
	            sc.nextLine(); 
	            if (choice == 0) {
	                System.out.println("Canceled.");
	                continue;
	            }
				else if (choice == 1) {
	                if (indexUser1 != -1) {
	                    System.out.println("That person is already your friend.");
	                    continue;
	                }
	                // Add friend: update id list and BST via existing addFriend (won't touch the other user)
	                currUser.getFriendIds().addLast(storedUser.getId());
	                currUser.addFriend(storedUser);
	                System.out.println("Added " + storedUser.getName() + " as a friend.");
	            }
				else if (choice == 2) {
	                if (indexUser2 != -1) {
	                    System.out.println("That person is already your friend.");
	                    continue;
	                }
	                currUser.getFriendIds().addLast(storedUser2.getId());
	                currUser.addFriend(storedUser2);
	                System.out.println("Added " + storedUser2.getName() + " as a friend.");
	            }
				else {
	                System.out.println("Invalid choice. Please enter 0, 1, or 2.");
	                continue;
	            }
	        }
			else {
	            int index = currUser.friendIds.findIndex(storedUser.getId());
	            if (index != -1) {
	                System.out.println(storedUser.getName() + " is already your friend.");
	                continue;
	            }
	
	            System.out.println("Found: " + storedUser);
	            System.out.println("Add as friend? (1 = yes, 0 = no)");
	
	            int choice;
	            if (!sc.hasNextInt()) {
	                System.out.println("Invalid input. Please enter a number.");
	                sc.nextLine();
	                continue;
	            }
	            choice = sc.nextInt();
	            sc.nextLine();
	
	            if (choice == 1) {
	                currUser.getFriendIds().addLast(storedUser.getId());
	                currUser.addFriend(storedUser);
	                System.out.println("Added " + storedUser.getName() + " as a friend.");
	            }
				else if (choice == 0) {
	                System.out.println("Not added.");
	            }
				else {
	                System.out.println("Invalid choice. Please enter 0 or 1.");
	            }
	        }
		}
	}

	public static void addFriendsByInterest() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the Interest you'd like to find friends with:");
		String interestName = sc.nextLine().trim();
		Interest interest = new Interest(-1, interestName);
		int bucket = interestMap.get(interest).getId();
		BST<User> bst = usersByInterest.get(bucket);
		ArrayList<User> list = bst.toArrayList();
		for (int i = 0; i < list.size(); i++) {
			System.out.println((i+1) + ": " + list.get(i).getName()+ "\n");
		}
		System.out.println("Enter the number of the user you'd like to add or 0 to return:");
		String listString = sc.nextLine().trim();
		int listInt;
		try {
			listInt = Integer.parseInt(listString);
			if (listInt == 0) {
				return;
			} else if (listInt > list.size() || listInt < 0) {
				System.out.println("Invalid input. Returning");
				return;
			} else {
				addFriend(list.get(listInt));
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Returning");
			return;
		}
	}

	public static void addFriendsByRecommendation() {
		Scanner sc = new Scanner(System.in);
		ArrayList<User> recomend = getFriendRecommendations(currUser);
		System.out.println("Here are some people we think could be your friends:");
		for (int i = 1; i <= recomend.size(); i++) {
			System.out.print(i + ": ");
			System.out.println(recomend.get(i).getName());
			System.out.println(recomend.get(i).getUsername() + "\n");
		}
		System.out.println("Enter the number of the friend you wish to add or 0 to return");
		String choice = sc.nextLine().trim();
		int choiceInt;
		try {
			choiceInt = Integer.parseInt(choice);
			if (choiceInt == 0) {
				return;
			} else if (choiceInt >= recomend.size() || choiceInt < 0) {
				System.out.println("Invalid input. Returning");
				return;
			} else {
				addFriend(recomend.get(choiceInt));
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Returning");
			return;
		}
	}
	
	public static void addFriend(User friend) {
		currUser.addFriend(friend);
		int id1 = currUser.getId();
		int id2 = friend.getId();
		userConnections.addDirectedEdge(id1, id2);
	}
}
