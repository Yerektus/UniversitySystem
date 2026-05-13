package ui;

import model.communication.News;
import model.communication.Request;
import model.enums.*;
import model.users.*;
import storage.ActionLogger;
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
        System.out.println("\n--- Admin Menu [" + admin.getFirstName() + " " + admin.getLastName() + "] ---");
        System.out.println("1. View profile");
        System.out.println("2. View all users");
        System.out.println("3. Add user");
        System.out.println("4. Remove user");
        System.out.println("5. View all requests");
        System.out.println("6. Accept a request");
        System.out.println("7. Reject a request");
        System.out.println("8. View all news");
        System.out.println("9. View action logs");
        System.out.println("10. View system stats");
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
            case "7": rejectRequest(); break;
            case "8": viewAllNews();  break;
            case "9": viewLogs();     break;
            case "10": viewStats();   break;
            case "0": logout();       break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void viewProfile() {
        System.out.println("\n========== My Profile ==========");
        System.out.printf("%-12s %s%n", "ID:",         admin.getId());
        System.out.printf("%-12s %s %s%n", "Name:",    admin.getFirstName(), admin.getLastName());
        System.out.printf("%-12s %s%n", "Email:",      admin.getEmail());
        System.out.printf("%-12s %s%n", "Department:", admin.getDepartment());
        System.out.printf("%-12s %s%n", "Language:",   admin.getLanguage());
        System.out.println("=================================");
        pause();
    }

    private void viewAllUsers() {
        List<User> users = new ArrayList<>(storage.getUsers().values());
        System.out.println("\n========== All Users (" + users.size() + ") ==========");
        System.out.printf("%-6s %-20s %-30s %-25s %-15s%n",
                "No.", "Name", "Email", "Role", "Department");
        System.out.println("---------------------------------------------------------------------------------------------");
        int i = 1;
        for (User u : users) {
            String dept = (u instanceof Employee) ? ((Employee) u).getDepartment() : "-";
            System.out.printf("%-6d %-20s %-30s %-25s %-15s%n",
                    i++,
                    u.getFirstName() + " " + u.getLastName(),
                    u.getEmail(),
                    u.getClass().getSimpleName(),
                    dept);
        }
        System.out.println("=============================================");
        pause();
    }

    private void addUser() {
        System.out.println("\n--- Add User ---");
        System.out.println("Role:");
        System.out.println("1. Student");
        System.out.println("2. Graduate Student");
        System.out.println("3. Teacher");
        System.out.println("4. Manager");
        System.out.println("5. Admin");
        System.out.println("6. Tech Support Specialist");
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
        System.out.print("Department/Faculty: ");
        String department = scanner.nextLine().trim();
        System.out.println("Language: 1. KZ  2. EN  3. RU");
        System.out.print("Enter choice: ");
        Language language = parseLanguage(scanner.nextLine().trim());

        String id = "U" + System.currentTimeMillis();
        User newUser = null;

        switch (roleChoice) {
            case "1": {
                System.out.print("Major: ");
                String major = scanner.nextLine().trim();
                System.out.print("Year (1-4): ");
                int year = parseIntSafe(scanner.nextLine().trim(), 1);
                newUser = new Student(id, password, firstName, lastName, email, language, major, year);
                break;
            }
            case "2": {
                System.out.print("Major: ");
                String major = scanner.nextLine().trim();
                System.out.print("Year (1-2): ");
                int year = parseIntSafe(scanner.nextLine().trim(), 1);
                System.out.println("Type: 1. MASTER  2. PHD");
                GraduateType gt = scanner.nextLine().trim().equals("2") ? GraduateType.PHD : GraduateType.MASTER;
                System.out.print("Research target/topic: ");
                String target = scanner.nextLine().trim();
                newUser = new GraduateStudent(id, password, firstName, lastName, email, language, major, year, gt, target);
                break;
            }
            case "3": {
                System.out.println("Position: 1. TUTOR  2. LECTOR  3. SENIOR  4. PROFESSOR");
                TeacherPosition pos;
                switch (scanner.nextLine().trim()) {
                    case "1": pos = TeacherPosition.TUTOR;     break;
                    case "3": pos = TeacherPosition.SENIOR;    break;
                    case "4": pos = TeacherPosition.PROFESSOR; break;
                    default:  pos = TeacherPosition.LECTOR;    break;
                }
                newUser = new Teacher(id, password, firstName, lastName, email, language, department, pos);
                break;
            }
            case "4": {
                System.out.println("Manager type: 1. OR  2. DEPARTMENT  3. DEAN_OFFICE");
                ManagerType mType;
                switch (scanner.nextLine().trim()) {
                    case "2": mType = ManagerType.DEPARTMENT;  break;
                    case "3": mType = ManagerType.DEAN_OFFICE; break;
                    default:  mType = ManagerType.OR;
                }
                newUser = new Manager(id, password, firstName, lastName, email, language, department, mType);
                break;
            }
            case "5":
                newUser = new Admin(id, password, firstName, lastName, email, language, department);
                break;
            case "6":
                newUser = new TechSupportSpecialist(id, password, firstName, lastName, email, language, department);
                break;
            default:
                System.out.println("Invalid role.");
                return;
        }

        admin.addUser(newUser);
        storage.save(newUser);
        ActionLogger.getInstance().log(admin.getId(), "ADD_USER via AdminMenu: " + email);
        System.out.println("Account created. Email: " + email + " | Password: " + password);
        pause();
    }

    private void removeUser() {
        List<User> users = new ArrayList<>(storage.getUsers().values());
        if (users.isEmpty()) { System.out.println("No users found."); return; }

        System.out.println("\n--- Remove User ---");
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            System.out.printf("%-4d [%-25s] %s %s  (%s)%n",
                    i + 1, u.getClass().getSimpleName(),
                    u.getFirstName(), u.getLastName(), u.getEmail());
        }
        System.out.print("Select user to remove (0 to cancel): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= users.size()) { System.out.println("Cancelled."); return; }

        User target = users.get(idx);
        System.out.print("Are you sure you want to remove " + target.getFirstName()
                + " " + target.getLastName() + "? (yes/no): ");
        String confirm = scanner.nextLine().trim();
        if (!confirm.equalsIgnoreCase("yes")) { System.out.println("Cancelled."); return; }

        admin.removeUser(target);
        storage.removeUser(target.getId());
        pause();
    }

    private void viewRequests() {
        List<Request> requests = storage.getRequests();
        System.out.println("\n========== All Requests (" + requests.size() + ") ==========");
        if (requests.isEmpty()) {
            System.out.println("  No requests found.");
        } else {
            System.out.printf("%-6s %-10s %-25s %-20s %-50s%n",
                    "No.", "Status", "Topic", "From", "Description");
            System.out.println("---------------------------------------------------------------------------------------------");
            for (int i = 0; i < requests.size(); i++) {
                Request r = requests.get(i);
                System.out.printf("%-6d %-10s %-25s %-20s %-50s%n",
                        i + 1,
                        r.getStatus(),
                        r.getTopic(),
                        r.getSender().getFirstName() + " " + r.getSender().getLastName(),
                        r.getDescription());
            }
        }
        System.out.println("=============================================");
        pause();
    }

    private void acceptRequest() {
        List<Request> pending = storage.getRequests().stream()
                .filter(r -> r.getStatus() == RequestStatus.NEW
                        || r.getStatus() == RequestStatus.VIEWED)
                .collect(Collectors.toList());

        if (pending.isEmpty()) { System.out.println("No pending requests to accept."); return; }

        System.out.println("\n--- Accept Request ---");
        for (int i = 0; i < pending.size(); i++) {
            System.out.printf("%-4d [%-8s] %-25s - %s%n",
                    i + 1, pending.get(i).getStatus(),
                    pending.get(i).getTopic(),
                    pending.get(i).getSender().getFirstName());
        }
        System.out.print("Select request (0 to cancel): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= pending.size()) { System.out.println("Cancelled."); return; }

        admin.acceptRequest(pending.get(idx));
        pause();
    }

    private void rejectRequest() {
        List<Request> pending = storage.getRequests().stream()
                .filter(r -> r.getStatus() == RequestStatus.NEW
                        || r.getStatus() == RequestStatus.VIEWED)
                .collect(Collectors.toList());

        if (pending.isEmpty()) { System.out.println("No pending requests to reject."); return; }

        System.out.println("\n--- Reject Request ---");
        for (int i = 0; i < pending.size(); i++) {
            System.out.printf("%-4d [%-8s] %-25s - %s%n",
                    i + 1, pending.get(i).getStatus(),
                    pending.get(i).getTopic(),
                    pending.get(i).getSender().getFirstName());
        }
        System.out.print("Select request (0 to cancel): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= pending.size()) { System.out.println("Cancelled."); return; }

        pending.get(idx).updateStatus(RequestStatus.REJECTED);
        System.out.println("Request rejected.");
        pause();
    }

    private void viewAllNews() {
        List<News> newsList = storage.getNewsList();
        System.out.println("\n========== All News (" + newsList.size() + ") ==========");
        if (newsList.isEmpty()) {
            System.out.println("  No news found.");
        } else {
            for (int i = 0; i < newsList.size(); i++) {
                News n = newsList.get(i);
                System.out.println("\n  " + (i + 1) + ". [" + n.getTopic() + "] " + n.getTitle());
                System.out.println("     " + n.getContent());
                System.out.println("     Posted by: "
                        + (n.getAuthor() != null
                            ? n.getAuthor().getFirstName() + " " + n.getAuthor().getLastName()
                            : "System")
                        + "  |  Pinned: " + n.isPinned());
            }
        }
        System.out.println("==========================================");
        pause();
    }

    private void viewLogs() {
        System.out.println("\n========== Action Logs ==========");
        System.out.println(admin.viewLogs());
        System.out.println("=================================");
        pause();
    }

    private void viewStats() {
        long studentCount = storage.getUsers().values().stream()
                .filter(u -> u instanceof Student).count();
        long teacherCount = storage.getUsers().values().stream()
                .filter(u -> u instanceof Teacher).count();
        long employeeCount = storage.getUsers().values().stream()
                .filter(u -> u instanceof Employee && !(u instanceof Teacher)).count();

        System.out.println("\n========== System Statistics ==========");
        System.out.printf("  %-25s %d%n", "Total Users:",       storage.getUsers().size());
        System.out.printf("  %-25s %d%n", "Students:",          studentCount);
        System.out.printf("  %-25s %d%n", "Teachers:",          teacherCount);
        System.out.printf("  %-25s %d%n", "Staff (non-teacher):", employeeCount);
        System.out.printf("  %-25s %d%n", "Total Courses:",     storage.getCourses().size());
        System.out.printf("  %-25s %d%n", "Lesson Groups:",     storage.getLessonGroups().size());
        System.out.printf("  %-25s %d%n", "Organizations:",     storage.getOrganizations().size());
        System.out.printf("  %-25s %d%n", "Journals:",          storage.getJournals().size());
        System.out.printf("  %-25s %d%n", "Requests:",          storage.getRequests().size());
        System.out.printf("  %-25s %d%n", "News Articles:",     storage.getNewsList().size());
    }
    
    private Language parseLanguage(String choice) {
        switch (choice) {
            case "1": return Language.KZ;
            case "3": return Language.RU;
            default:  return Language.EN;
        }
    }
}