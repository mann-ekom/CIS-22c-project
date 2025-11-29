public class main{
  BST Users;
  Hashtable UserNames;
  HashTable Passwords;
  Graph UserConnections;
  ArrayList BST SharedInterest;
  int numUsers;
  
  
  
  
  
  
  
  
  
  
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
