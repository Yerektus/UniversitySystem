package ui;

import model.users.*;
import storage.DataStorage;

import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);
    private final DataStorage storage = DataStorage.getInstance();

    public void start() {
        System.out.println("");
        System.out.println("     Welcome to University System");
        System.out.println("");
        while (true) {
            showMainMenu();
        }
    }

    private void showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Login");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1": login(); break;
            case "0":
                System.out.println("Goodbye!");
                storage.saveAll();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }

    private void login() {
        System.out.println("\n--- Login ---");
        System.out.print("ID or Email: ");
        String identifier = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User found = null;
        for (User u : storage.getUsers().values()) {
            boolean match = (u.getEmail().equalsIgnoreCase(identifier) || u.getId().equals(identifier))
                    && u.getPassword().equals(password);
            if (match) { found = u; break; }
        }

        if (found == null) {
            System.out.println("Invalid ID/email or password.");
            return;
        }

        found.login();
        System.out.println("Welcome, " + found.getFirstName() + "!");
        routeToUserMenu(found);
    }

    private void routeToUserMenu(User user) {
        if (user instanceof Admin) {
            new AdminMenu(scanner, storage, (Admin) user).show();
        } else if (user instanceof Manager) {
            new ManagerMenu(scanner, storage, (Manager) user).show();
        } else if (user instanceof Teacher) {
            new TeacherMenu(scanner, storage, (Teacher) user).show();
        } else if (user instanceof GraduateStudent) {
            new GraduateStudentMenu(scanner, storage, (GraduateStudent) user).show();
        } else if (user instanceof Student) {
            new StudentMenu(scanner, storage, (Student) user).show();
        } else if (user instanceof TechSupportSpecialist) {
            new TechSupportMenu(scanner, storage, (TechSupportSpecialist) user).show();
        } else {
            System.out.println("No menu available for this user type.");
        }
    }
}