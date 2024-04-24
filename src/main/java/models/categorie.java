package models;

public class categorie {
    private int id;
    private String name;

    public categorie(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    // Override toString if you want to use the category name as the display text for ComboBox items
    @Override
    public String toString() {
        return name;
    }
}
