package ui;

import model.communication.Request;
import model.enums.Language;
import model.enums.ManagerType;
import model.enums.RequestStatus;
import model.enums.TeacherPosition;
import model.users.*;
import storage.DataStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AdminMenu extends BaseMenu {

    private final Admin admin;

    public AdminMenu(Scanner scanner, DataStorage storage, Admin admin) {
        super(scanner, storage, admin);
        this.admin = admin;
    }

    @Override
    protected void printMenu() {
        System.out.println("\n--- Admin Menu [" + admin.getFirstName() + "] ---");
        System.out.println("1. View profile");
        System.out.println("2. View all users");
        System.out.println("3. Add user (Manager / Admin / Tech Support)");
        System.out.println("4. Remove user");
        System.out.println("5. View all requests");
        System.out.println("6. Accept a request");
        System.out.println("7. View logs");
        System.out.println("0. Logout");
    }

    @Override
    protected void handleChoice(String choice) {
        switch (choice) {
            case "1": viewProfile(); break;
            case "2": viewAllUsers(); break;
            case "3": addUser(); break;
            case "4": removeUser(); break;
            case "5": viewRequests(); break;
            case "6": acceptRequest(); break;
            case "7": admin.viewLogs(); pause(); break;
            case "0": logout(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void viewProfile() {
        System.out.println("\n--- My Profile ---");
        System.out.println("ID:         " + admin.getId());
        System.out.println("Name:       " + admin.getFirstName() + " " + admin.getLastName());
        System.out.println("Email:      " + admin.getEmail());
        System.out.println("Department: " + admin.getDepartment());
        pause();
    }

    private void viewAllUsers() {
        System.out.println("\n--- All Users ---");
        storage.getUsers().values().forEach(u ->
            System.out.println("- [" + u.getClass().getSimpleName() + "] "
                    + u.getFirstName() + " " + u.getLastName()
                    + " | " + u.getEmail())
        );
        pause();
    }

    private void addUser() {
        System.out.println("\n--- Add User ---");
        System.out.println("Role: 1. Manager  2. Admin  3. TechSupportSpecialist");
        System.out.print("Enter choice: ");
        String roleChoice = scanner.nextLine().trim();

        System.out.print("First name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Last name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Department: ");
        String department = scanner.nextLine().trim();

        String id = "U" + System.currentTimeMillis();
        User newUser = null;

        switch (roleChoice) {
            case "1":
                System.out.println("Manager type: 1. OR  2. DEPARTMENT  3. DEAN_OFFICE");
                System.out.print("Enter choice: ");
                ManagerType mType;
                switch (scanner.nextLine().trim()) {
                    case "2": mType = ManagerType.DEPARTMENT; break;
                    case "3": mType = ManagerType.DEAN_OFFICE; break;
                    default: mType = ManagerType.OR;
                }
                newUser = new Manager(id, password, firstName, lastName,
                        email, Language.EN, department, mType);
                break;
            case "2":
                newUser = new Admin(id, password, firstName, lastName,
                        email, Language.EN, department);
                break;
            case "3":
                newUser = new TechSupportSpecialist(id, password, firstName, lastName,
                        email, Language.EN, department);
                break;
            default:
                System.out.println("Invalid role.");
                return;
        }

        admin.addUser(newUser);
        storage.save(newUser);
        pause();
    }

    private void removeUser() {
        List<User> users = new ArrayList<>(storage.getUsers().values());
        if (users.isEmpty()) { System.out.println("No users found."); return; }

        System.out.println("\n--- Remove User ---");
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            System.out.println((i + 1) + ". [" + u.getClass().getSimpleName() + "] "
                    + u.getFirstName() + " " + u.getLastName());
        }
        System.out.print("Select user to remove: ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= users.size()) { System.out.println("Invalid."); return; }

        User target = users.get(idx);
        admin.removeUser(target);
        storage.removeUser(target.getId());
        pause();
    }

    private void viewRequests() {
        System.out.println("\n--- All Requests ---");
        List<Request> requests = storage.getRequests();
        if (requests.isEmpty()) {
            System.out.println("No requests found.");
        } else {
            for (int i = 0; i < requests.size(); i++) {
                Request r = requests.get(i);
                System.out.println((i + 1) + ". [" + r.getStatus() + "] "
                        + r.getTopic() + " | From: " + r.getSender().getFirstName());
            }
        }
        pause();
    }

    private void acceptRequest() {
        List<Request> pending = storage.getRequests().stream()
                .filter(r -> r.getStatus() == RequestStatus.NEW || r.getStatus() == RequestStatus.VIEWED)
                .collect(Collectors.toList());

        if (pending.isEmpty()) { System.out.println("No pending requests."); return; }

        System.out.println("\n--- Accept Request ---");
        for (int i = 0; i < pending.size(); i++) {
            System.out.println((i + 1) + ". " + pending.get(i).getTopic()
                    + " | " + pending.get(i).getDescription());
        }
        System.out.print("Select request: ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= pending.size()) { System.out.println("Invalid."); return; }

        admin.acceptRequest(pending.get(idx));
        pause();
    }
}
