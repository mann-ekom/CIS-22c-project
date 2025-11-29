import java.util.Comparator;

public class User {
    private int id;                         
    private String name;                    
    private String username;                
    private String password;                
    private int totalFriends;
    private LinkedList<Integer> friendIds;
    private String city;
    private int totalInterests;
    private LinkedList<String> interestStrings;
    private BST<User> friendsByName;
    private LinkedList<Interest> interests;

    public static final Comparator<User> NAME_COMPARATOR = new Comparator<User>() {
        @Override
        public int compare(User a, User b) {
            int cmp = a.name.compareTo(b.name);
            if (cmp != 0) return cmp;
            return a.username.compareTo(b.username);
        }
    }

    /**
     * Full constructor: takes everything from the file
     * plus a pre-built list of Interest objects.
     */
    public User() {
        this.id = -1;
        this.name = "";
        this.username = "";
        this.password = "";
        this.totalFriends = 0;
        this.friendIds = new LinkedList<Integer>();
        this.city = "";
        this.totalInterests = 0;
        this.interestStrings = new LinkedList<String>();
        this.friendsByName = new BST<User>();
        this.interests = new LinkedList<Interest>();
    }
    
    public User(int id, String name, String username, String password, int totalFriends, LinkedList<Integer> friendIds, String city, int totalInterests, LinkedList<String> interestStrings, LinkedList<Interest> interests) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.totalFriends = totalFriends;
        this.friendIds = (friendIds != null) ? new LinkedList<Integer>(friendIds) : new LinkedList<Integer>();
        this.city = city;
        this.totalInterests = totalInterests;
        this.interestStrings = (interestStrings != null) ? new LinkedList<String>(interestStrings) : new LinkedList<String>();
        this.friendsByName = new BST<User>();
        this.interests = (interests != null) ? new LinkedList<Interest>(interests) : new LinkedList<Interest>();
    }
    
    public int getId() { 
        return id; 
    }
    public String getName() { 
        return name; 
    }
    public String getUsername() { 
        return username; 
    }
    public String getPassword() { 
        return password; 
    }
    public int getTotalFriends() { 
        return totalFriends; 
    }
    public LinkedList<Integer> getFriendIds() { 
        return friendIds; 
    }

    public String getCity() { 
        return city; 
    }
    public int getTotalInterests() { 
        return totalInterests; 
    }
    public LinkedList<String> getInterestStrings() { 
        return interestStrings; 
    }

    public BST<User> getFriendsByName() { 
        return friendsByName; 
    }
    public LinkedList<Interest> getInterests() { 
        return interests; 
    }

     // ===== SETTERS =====

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        if (name == null) name = "";
        this.name = name;
    }

    public void setUsername(String username) {
        if (username == null) username = "";
        this.username = username;
    }

    public void setPassword(String password) {
        if (password == null) password = "";
        this.password = password;
    }

    public void setTotalFriends(int totalFriends) {
        this.totalFriends = totalFriends;
    }

    public void setFriendIds(LinkedList<Integer> friendIds) {
        if (friendIds == null) {
            this.friendIds = new LinkedList<Integer>();
        } else {
            this.friendIds = new LinkedList<Integer>(friendIds);
        }
    }

    public void setCity(String city) {
        if (city == null) city = "";
        this.city = city;
    }

    public void setTotalInterests(int totalInterests) {
        this.totalInterests = totalInterests;
    }

    public void setInterestStrings(LinkedList<String> interestStrings) {
        if (interestStrings == null) {
            this.interestStrings = new LinkedList<String>();
        } else {
            this.interestStrings = new LinkedList<String>(interestStrings);
        }
    }

    public void setFriendsByName(BST<User> friendsTree) {
        if (friendsTree == null) {
            this.friendsByName = new BST<User>();
        } else {
            this.friendsByName = friendsTree;
        }
    }

    public void setInterests(LinkedList<Interest> interests) {
        if (interests == null) {
            this.interests = new LinkedList<Interest>();
        } else {
            this.interests = new LinkedList<Interest>(interests);
        }
    }
    
    // ===== FRIEND & INTEREST HELPERS =====
    public void addFriend(User friend) {
        friendsByName.insert(friend, NAME_COMPARATOR);
    }

    public void removeFriend(User friend) {
        friendsByName.remove(friend, NAME_COMPARATOR);
    }

    public void addConvertedInterest(Interest interest) {
        interests.addLast(interest);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User other = (User) obj;
        return this.name.equals(other.name);
    }
}
