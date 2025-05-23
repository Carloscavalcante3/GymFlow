package src.model;

public class User {
    private String name;
    private int age;
    private double weight;
    private double height;
    private String username;
    private String password;
    private int points;

    public User(String name, int age, double weight, double height, String username, String password) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.username = username;
        this.password = password;
        this.points = 0;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getWeight() { return weight; }
    public double getHeight() { return height; }
    public int getPoints() { return points; }
    public void addPoints(int p) { this.points += p; }
}
