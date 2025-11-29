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
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return this.username.equals(other.username);
    }
}
