import java.util.Comparator;

public class User {
    private int id;
    private String name;
    private String userName;
    private String password;
    private String city;
    private BST<User> friendsByName;
    private LinkedList<Interest> interests;

    /** Comparator for ordering Users by name, then username */
    public static final Comparator<User> NAME_COMPARATOR = new Comparator<User>() {
        @Override
        public int compare(User a, User b) {
            int cmp = a.name.compareToIgnoreCase(b.name);
            if (cmp != 0) return cmp;
            return a.userName.compareToIgnoreCase(b.userName);
        }
    };

    public User(int id, String name,
                String userName, String password, String city) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.city = city;
        this.friendsByName = new BST<User>();
        this.interests = new LinkedList<Interest>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getCity() {
        return city;
    }

    public BST<User> getFriendsByName() {
        return friendsByName;
    }

    public LinkedList<Interest> getInterests() {
        return interests;
    }

    public void addFriend(User friend) {
        friendsByName.insert(friend, NAME_COMPARATOR);
    }

    public void removeFriend(User friend) {
        friendsByName.remove(friend, NAME_COMPARATOR);
    }

    public void addInterest(Interest interest) {
        interests.addLast(interest);
    }

    @Override
    public int hashCode() {
        return userName.toLowerCase().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return this.userName.equalsIgnoreCase(other.userName);
    }
}
