public class Interest {
    private int id;        // index in ArrayList<BST<User>>
    private String label;  // e.g. "coding", "swimming"

    public Interest(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() { return id; }
    public String getLabel() { return label; }

    @Override
    public int hashCode() {
        return label.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Interest)) return false;
        Interest other = (Interest) obj;
        return this.label.equals(other.label);
    }

    @Override
    public String toString() {
        return label;
    }
}
