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
        System.out.println("2. User management");
        System.out.println("3. Requests");
        System.out.println("4. News");
        System.out.println("5. System");
        System.out.println("0. Logout");
    }

    @Override
    protected void handleChoice(String choice) {
        switch (choice) {
            case "1": viewProfile();      break;
            case "2": userManagement();   break;
            case "3": requestsMenu();     break;
            case "4": viewAllNews();      break;
            case "5": systemMenu();       break;
            case "0": logout();           break;
            default:  System.out.println("Invalid choice.");
        }
    }

    private void viewProfile() {
        System.out.println("\n--- Profile ---");
        System.out.printf("%-12s %s%n",    "ID:",         admin.getId());
        System.out.printf("%-12s %s %s%n", "Name:",       admin.getFirstName(), admin.getLastName());
        System.out.printf("%-12s %s%n",    "Email:",      admin.getEmail());
        System.out.printf("%-12s %s%n",    "Department:", admin.getDepartment());
        System.out.printf("%-12s %s%n",    "Language:",   admin.getLanguage());
    }

    private void userManagement() {
        while (true) {
            System.out.println("\n--- Admin Menu: User Management ---");
            System.out.println("1. View all users");
            System.out.println("2. Add user");
            System.out.println("3. Remove user");
            System.out.println("4. Update user");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            switch (scanner.nextLine().trim()) {
                case "1": viewAllUsers(); break;
                case "2": addUser();      break;
                case "3": removeUser();   break;
                case "4": updateUser();   break;
                case "0": return;
                default:  System.out.println("Invalid choice.");
            }
        }
    }

    private void requestsMenu() {
        while (true) {
            System.out.println("\n--- Admin Menu: Requests ---");
            System.out.println("1. View all requests");
            System.out.println("2. Accept a request");
            System.out.println("3. Reject a request");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            switch (scanner.nextLine().trim()) {
                case "1": viewRequests();  break;
                case "2": acceptRequest(); break;
                case "3": rejectRequest(); break;
                case "0": return;
                default:  System.out.println("Invalid choice.");
            }
        }
    }

    private void systemMenu() {
        while (true) {
            System.out.println("\n--- Admin Menu: System ---");
            System.out.println("1. View action logs");
            System.out.println("2. View system stats");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            switch (scanner.nextLine().trim()) {
                case "1": viewLogs();  break;
                case "2": viewStats(); break;
                case "0": return;
                default:  System.out.println("Invalid choice.");
            }
        }
    }

    private void viewAllUsers() {
        List<User> users = new ArrayList<>(storage.getUsers().values());
        System.out.println("\n--- All Users (" + users.size() + ") ---");
        System.out.printf("%-4s %-12s %-22s %-32s %-20s %-15s%n",
                "No.", "ID", "Name", "Email", "Role", "Department");
        int i = 1;
        for (User u : users) {
            String dept = (u instanceof Employee) ? ((Employee) u).getDepartment() : "-";
            System.out.printf("%-4d %-12s %-22s %-32s %-20s %-15s%n",
                    i++, u.getId(),
                    u.getFirstName() + " " + u.getLastName(),
                    u.getEmail(), u.getClass().getSimpleName(), dept);
        }
    }

    private void addUser() {
        System.out.println("\n--- Add User ---");
        System.out.println("1. Student");
        System.out.println("2. Graduate Student");
        System.out.println("3. Teacher");
        System.out.println("4. Manager");
        System.out.println("5. Admin");
        System.out.println("6. Tech Support Specialist");
        System.out.println("0. Cancel");
        System.out.print("Role: ");
        String roleChoice = scanner.nextLine().trim();
        if (roleChoice.equals("0")) return;

        System.out.print("First name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Last name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Department/Faculty: ");
        String department = scanner.nextLine().trim();
        System.out.println("Language: 1. KZ  2. EN  3. RU");
        System.out.print("Choice: ");
        Language language = parseLanguage(scanner.nextLine().trim());

        String id;
        String email    = util.UserCredentialGenerator.generateEmail(firstName, lastName);
        String password = util.UserCredentialGenerator.generatePassword();
        User newUser    = null;

        switch (roleChoice) {
            case "1": {
                SchoolCode school = selectSchool();
                System.out.print("Major: ");
                String major = scanner.nextLine().trim();
                System.out.print("Year (1-4): ");
                int year = parseIntSafe(scanner.nextLine().trim(), 1);
                id = util.UserCredentialGenerator.generateStudentId("B", school);
                newUser = new Student(id, password, firstName, lastName, email, language, major, year);
                break;
            }
            case "2": {
                SchoolCode school = selectSchool();
                System.out.print("Major: ");
                String major = scanner.nextLine().trim();
                System.out.print("Year (1-2): ");
                int year = parseIntSafe(scanner.nextLine().trim(), 1);
                System.out.println("Type: 1. MASTER  2. PHD");
                GraduateType gt = scanner.nextLine().trim().equals("2") ? GraduateType.PHD : GraduateType.MASTER;
                String degreeType = gt == GraduateType.PHD ? "P" : "M";
                System.out.print("Research target/topic: ");
                String target = scanner.nextLine().trim();
                id = util.UserCredentialGenerator.generateStudentId(degreeType, school);
                newUser = new GraduateStudent(id, password, firstName, lastName, email, language, major, year, gt, target);
                break;
            }
            case "3": {
                SchoolCode school = selectSchool();
                System.out.println("Position: 1. TUTOR  2. LECTOR  3. SENIOR  4. PROFESSOR");
                TeacherPosition pos;
                switch (scanner.nextLine().trim()) {
                    case "1": pos = TeacherPosition.TUTOR;     break;
                    case "3": pos = TeacherPosition.SENIOR;    break;
                    case "4": pos = TeacherPosition.PROFESSOR; break;
                    default:  pos = TeacherPosition.LECTOR;    break;
                }
                id = util.UserCredentialGenerator.generateTeacherId(school);
                newUser = new Teacher(id, password, firstName, lastName, email, language, department, pos);
                break;
            }
            case "4": {
                SchoolCode school = selectSchool();
                System.out.println("Manager type: 1. OR  2. DEPARTMENT  3. DEAN_OFFICE");
                ManagerType mType;
                switch (scanner.nextLine().trim()) {
                    case "2": mType = ManagerType.DEPARTMENT;  break;
                    case "3": mType = ManagerType.DEAN_OFFICE; break;
                    default:  mType = ManagerType.OR;          break;
                }
                id = util.UserCredentialGenerator.generateManagerId(school);
                newUser = new Manager(id, password, firstName, lastName, email, language, department, mType);
                break;
            }
            case "5":
                id = util.UserCredentialGenerator.generateAdminId();
                newUser = new Admin(id, password, firstName, lastName, email, language, department);
                break;
            case "6":
                id = util.UserCredentialGenerator.generateTechSupportId();
                newUser = new TechSupportSpecialist(id, password, firstName, lastName, email, language, department);
                break;
            default:
                System.out.println("Invalid role.");
                return;
        }

        admin.addUser(newUser);
        storage.save(newUser);
        ActionLogger.getInstance().log(admin.getId(), "ADD_USER: " + email);
        System.out.println("Account created.");
        System.out.println("  ID      : " + newUser.getId());
        System.out.println("  Email   : " + email);
        System.out.println("  Password: " + password);
    }

    private void updateUser() {
        List<User> users = new ArrayList<>(storage.getUsers().values());
        if (users.isEmpty()) { System.out.println("No users found."); return; }

        System.out.println("\n--- Update User ---");
        for (int i = 0; i < users.size(); i++)
            System.out.printf("%-4d %-12s [%-20s] %s %s%n",
                    i + 1, users.get(i).getId(),
                    users.get(i).getClass().getSimpleName(),
                    users.get(i).getFirstName(), users.get(i).getLastName());
        System.out.print("Select user (0 to cancel): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= users.size()) { System.out.println("Cancelled."); return; }
        User user = users.get(idx);

        System.out.println("Editing: " + user.getFirstName() + " " + user.getLastName()
                + " (" + user.getClass().getSimpleName() + ")");
        System.out.println("Leave blank to keep current value.");

        System.out.print("First name [" + user.getFirstName() + "]: ");
        String val = scanner.nextLine().trim();
        if (!val.isEmpty()) user.setFirstName(val);

        System.out.print("Last name [" + user.getLastName() + "]: ");
        val = scanner.nextLine().trim();
        if (!val.isEmpty()) user.setLastName(val);

        System.out.print("Email [" + user.getEmail() + "]: ");
        val = scanner.nextLine().trim();
        if (!val.isEmpty()) user.setEmail(val);

        System.out.println("Language [" + user.getLanguage() + "] 1. KZ  2. EN  3. RU: ");
        val = scanner.nextLine().trim();
        if (!val.isEmpty()) user.setLanguage(parseLanguage(val));

        if (user instanceof Employee) {
            Employee emp = (Employee) user;
            System.out.print("Department [" + emp.getDepartment() + "]: ");
            val = scanner.nextLine().trim();
            if (!val.isEmpty()) emp.setDepartment(val);
        }
        if (user instanceof Teacher) {
            Teacher t = (Teacher) user;
            System.out.println("Position [" + t.getPosition() + "] 1. TUTOR  2. LECTOR  3. SENIOR  4. PROFESSOR: ");
            val = scanner.nextLine().trim();
            if (!val.isEmpty()) {
                switch (val) {
                    case "1": t.setPosition(TeacherPosition.TUTOR);     break;
                    case "2": t.setPosition(TeacherPosition.LECTOR);    break;
                    case "3": t.setPosition(TeacherPosition.SENIOR);    break;
                    case "4": t.setPosition(TeacherPosition.PROFESSOR); break;
                }
            }
        }
        if (user instanceof Student) {
            Student s = (Student) user;
            System.out.print("Major [" + s.getMajor() + "]: ");
            val = scanner.nextLine().trim();
            if (!val.isEmpty()) s.setMajor(val);
            System.out.print("Year [" + s.getYear() + "]: ");
            val = scanner.nextLine().trim();
            if (!val.isEmpty()) s.setYear(parseIntSafe(val, s.getYear()));
        }

        storage.updateAndSave();
        ActionLogger.getInstance().log(admin.getId(), "UPDATE_USER: " + user.getEmail());
        System.out.println("User updated successfully.");
    }

    private void removeUser() {
        List<User> users = new ArrayList<>(storage.getUsers().values());
        if (users.isEmpty()) { System.out.println("No users found."); return; }

        System.out.println("\n--- Remove User ---");
        for (int i = 0; i < users.size(); i++)
            System.out.printf("%-4d %-12s [%-20s] %s %s%n",
                    i + 1, users.get(i).getId(),
                    users.get(i).getClass().getSimpleName(),
                    users.get(i).getFirstName(), users.get(i).getLastName());
        System.out.print("Select user (0 to cancel): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= users.size()) { System.out.println("Cancelled."); return; }

        User target = users.get(idx);
        System.out.print("Confirm removal of " + target.getFirstName() + " " + target.getLastName() + "? (yes/no): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("yes")) { System.out.println("Cancelled."); return; }

        admin.removeUser(target);
        storage.removeUser(target.getId());
    }

    private void viewRequests() {
        List<Request> requests = storage.getRequests();
        System.out.println("\n--- All Requests (" + requests.size() + ") ---");
        if (requests.isEmpty()) { System.out.println("No requests found."); return; }
        System.out.printf("%-4s %-10s %-25s %-20s %-40s%n", "No.", "Status", "Topic", "From", "Description");
        for (int i = 0; i < requests.size(); i++) {
            Request r = requests.get(i);
            System.out.printf("%-4d %-10s %-25s %-20s %-40s%n",
                    i + 1, r.getStatus(), r.getTopic(),
                    r.getSender().getFirstName() + " " + r.getSender().getLastName(),
                    r.getDescription());
        }
    }

    private void acceptRequest() {
        List<Request> pending = storage.getRequests().stream()
                .filter(r -> r.getStatus() == RequestStatus.NEW || r.getStatus() == RequestStatus.VIEWED)
                .collect(Collectors.toList());
        if (pending.isEmpty()) { System.out.println("No pending requests."); return; }

        System.out.println("\n--- Accept Request ---");
        for (int i = 0; i < pending.size(); i++)
            System.out.printf("%-4d [%-8s] %-25s - %s%n",
                    i + 1, pending.get(i).getStatus(),
                    pending.get(i).getTopic(),
                    pending.get(i).getSender().getFirstName());
        System.out.print("Choice (0 to cancel): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= pending.size()) { System.out.println("Cancelled."); return; }
        admin.acceptRequest(pending.get(idx));
        storage.updateAndSave();
    }

    private void rejectRequest() {
        List<Request> pending = storage.getRequests().stream()
                .filter(r -> r.getStatus() == RequestStatus.NEW || r.getStatus() == RequestStatus.VIEWED)
                .collect(Collectors.toList());
        if (pending.isEmpty()) { System.out.println("No pending requests."); return; }

        System.out.println("\n--- Reject Request ---");
        for (int i = 0; i < pending.size(); i++)
            System.out.printf("%-4d [%-8s] %-25s - %s%n",
                    i + 1, pending.get(i).getStatus(),
                    pending.get(i).getTopic(),
                    pending.get(i).getSender().getFirstName());
        System.out.print("Choice (0 to cancel): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= pending.size()) { System.out.println("Cancelled."); return; }
        pending.get(idx).updateStatus(RequestStatus.REJECTED);
        storage.updateAndSave();
        System.out.println("Request rejected.");
    }

    private void viewAllNews() {
        List<News> newsList = storage.getNewsList();
        System.out.println("\n--- All News (" + newsList.size() + ") ---");
        if (newsList.isEmpty()) { System.out.println("No news found."); return; }
        for (int i = 0; i < newsList.size(); i++) {
            News n = newsList.get(i);
            System.out.println((i + 1) + ". [" + n.getTopic() + "] " + n.getTitle()
                    + (n.isPinned() ? " [PINNED]" : ""));
            System.out.println("   " + n.getContent());
            System.out.println("   By: " + (n.getAuthor() != null
                    ? n.getAuthor().getFirstName() + " " + n.getAuthor().getLastName()
                    : "System"));
        }
    }

    private void viewLogs() {
        System.out.println("\n--- Action Logs ---");
        System.out.println(admin.viewLogs());
    }

    private void viewStats() {
        long students = storage.getUsers().values().stream().filter(u -> u instanceof Student).count();
        long teachers = storage.getUsers().values().stream().filter(u -> u instanceof Teacher).count();
        long staff    = storage.getUsers().values().stream().filter(u -> u instanceof Employee && !(u instanceof Teacher)).count();
        System.out.println("\n--- System Statistics ---");
        System.out.printf("%-25s %d%n", "Total Users:",   storage.getUsers().size());
        System.out.printf("%-25s %d%n", "Students:",      students);
        System.out.printf("%-25s %d%n", "Teachers:",      teachers);
        System.out.printf("%-25s %d%n", "Staff:",         staff);
        System.out.printf("%-25s %d%n", "Courses:",       storage.getCourses().size());
        System.out.printf("%-25s %d%n", "Lesson Groups:", storage.getLessonGroups().size());
        System.out.printf("%-25s %d%n", "Organizations:", storage.getOrganizations().size());
        System.out.printf("%-25s %d%n", "Journals:",      storage.getJournals().size());
        System.out.printf("%-25s %d%n", "Requests:",      storage.getRequests().size());
        System.out.printf("%-25s %d%n", "News:",          storage.getNewsList().size());
    }

    private Language parseLanguage(String choice) {
        switch (choice) {
            case "1": return Language.KZ;
            case "3": return Language.RU;
            default:  return Language.EN;
        }
    }

    private SchoolCode selectSchool() {
        System.out.println("School:");
        SchoolCode[] values = SchoolCode.values();
        for (int i = 0; i < values.length; i++)
            System.out.println((i + 1) + ". " + values[i].getDisplayName());
        System.out.print("Choice (default FIT): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 3) - 1;
        if (idx < 0 || idx >= values.length) return SchoolCode.FIT;
        return values[idx];
    }
}