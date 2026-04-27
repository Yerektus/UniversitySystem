package model.users;

import model.enums.Language;

public abstract class User {

    private String id;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private Language language;

    public User(String id, String password, String firstName, String lastName, String email, Language language) {
        this.id = id;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.language = language;
    }

    public void login() {
        System.out.println(firstName + " logged in.");
    }

    public void logout() {
        System.out.println(firstName + " logged out.");
    }

    public void sendMessage(String to, String text) {
        System.out.println("Message from " + firstName + " to " + to + ": " + text);
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Language getLanguage() { return language; }

    public void setLanguage(Language language) { this.language = language; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + firstName + " " + lastName + "', email='" + email + "'}";
    }
}
