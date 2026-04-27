package ui;

import model.enums.Language;
import model.users.*;
import storage.DataStorage;

import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);
    private final DataStorage storage = DataStorage.getInstance();
    private User currentUser = null;

    public void start() {
        System.out.println("==========================================");
        System.out.println("     Welcome to University System");
        System.out.println("==========================================");
        while (true) {
            showMainMenu();
        }
    }

    private void showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1": register(); break;
            case "2": login(); break;
            case "0":
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }

    private void register() {
        System.out.println("\n--- Register ---");
        System.out.print("First name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Last name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.println("Select language: 1. KZ  2. EN  3. RU");
        System.out.print("Enter choice: ");
        Language language = parseLanguage(scanner.nextLine().trim());

        System.out.println("Register as: 1. Student  2. Teacher");
        System.out.print("Enter choice: ");
        String roleChoice = scanner.nextLine().trim();

        String id = "U" + System.currentTimeMillis();

        if (roleChoice.equals("1")) {
            System.out.print("Major: ");
            String major = scanner.nextLine().trim();
            System.out.print("Year of study (1-4): ");
            int year = parseIntSafe(scanner.nextLine().trim(), 1);
            Student student = new Student(id, password, firstName, lastName, email, language, major, year);
            storage.save(student);
            System.out.println("Student registered successfully! You can now log in.");
        } else if (roleChoice.equals("2")) {
            System.out.print("Department: ");
            String department = scanner.nextLine().trim();
            Teacher teacher = new Teacher(id, password, firstName, lastName, email, language, department,
                    model.enums.TeacherPosition.LECTOR);
            storage.save(teacher);
            System.out.println("Teacher registered successfully! You can now log in.");
        } else {
            System.out.println("Invalid role.");
        }
    }

    private void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User found = null;
        for (User u : storage.getUsers().values()) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                found = u;
                break;
            }
        }

        if (found == null) {
            System.out.println("Invalid email or password.");
            return;
        }

        currentUser = found;
        currentUser.login();
        System.out.println("Welcome, " + currentUser.getFirstName() + "!");
        routeToUserMenu();
    }

    private void routeToUserMenu() {
        if (currentUser instanceof Admin) {
            new AdminMenu(scanner, storage, (Admin) currentUser).show();
        } else if (currentUser instanceof Manager) {
            new ManagerMenu(scanner, storage, (Manager) currentUser).show();
        } else if (currentUser instanceof Teacher) {
            new TeacherMenu(scanner, storage, (Teacher) currentUser).show();
        } else if (currentUser instanceof GraduateStudent) {
            new GraduateStudentMenu(scanner, storage, (GraduateStudent) currentUser).show();
        } else if (currentUser instanceof Student) {
            new StudentMenu(scanner, storage, (Student) currentUser).show();
        } else if (currentUser instanceof TechSupportSpecialist) {
            new TechSupportMenu(scanner, storage, (TechSupportSpecialist) currentUser).show();
        } else {
            System.out.println("No menu available for this user type.");
        }
        currentUser = null;
    }

    private Language parseLanguage(String choice) {
        switch (choice) {
            case "1": return Language.KZ;
            case "3": return Language.RU;
            default: return Language.EN;
        }
    }

    private int parseIntSafe(String input, int defaultVal) {
        try { return Integer.parseInt(input); }
        catch (NumberFormatException e) { return defaultVal; }
    }
}
