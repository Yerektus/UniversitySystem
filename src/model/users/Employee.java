package model.users;

import model.enums.Language;

public abstract class Employee extends User {

    private String department;

    public Employee(String id, String password, String firstName, String lastName,
                    String email, Language language, String department) {
        super(id, password, firstName, lastName, email, language);
        this.department = department;
    }

    @Override
    public void sendMessage(String to, String text) {
        System.out.println("[Employee] " + getFirstName() + " -> " + to + ": " + text);
    }

    public String getDepartment() { return department; }

    public void setDepartment(String department) { this.department = department; }
}
