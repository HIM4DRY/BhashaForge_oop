package bhashaforge.user;

public class User {

    private int id;           // database id
    private String username;

    public User(int id, String username) {
        this.id = id;
        this.username = username.trim().toLowerCase();
    }

    public int getId()         { return id; }
    public String getUsername(){ return username; }

    @Override
    public String toString() {
        return "User(" + id + ", " + username + ")";
    }
}