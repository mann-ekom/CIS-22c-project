public class Interest {
    private int id;        
    private String label;  

    public Interest(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() { 
        return id; 
    }
    public String getLabel() { 
        return label; 
    }

    public void setId(int id){
        this.id = id;
    }

    public void setLabel(String label){
        this.label = label;
    }

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
