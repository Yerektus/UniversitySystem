package ui;

import model.academic.Course;
import model.academic.Enrollment;
import model.academic.Mark;
import model.academic.StudentOrganization;
import model.communication.News;
import model.users.Student;
import model.users.Teacher;
import storage.DataStorage;
import java.util.List;
import java.util.Scanner;

public class StudentMenu extends BaseMenu {

    protected final Student student;

    public StudentMenu(Scanner scanner, DataStorage storage, Student student) {
        super(scanner, storage, student);
        this.student = student;
    }

    @Override
    protected void printMenu() {
        System.out.println("\n--- Student Menu [" + student.getFirstName() + " " + student.getLastName() + "] ---");
        System.out.println("1.  View profile");
        System.out.println("2.  View available courses");
        System.out.println("3.  Register for a course");
        System.out.println("4.  Drop a course");
        System.out.println("5.  View my enrollments");
        System.out.println("6.  View my marks");
        System.out.println("7.  View transcript");
        System.out.println("8.  View teacher info");
        System.out.println("9.  View student organizations");
        System.out.println("10. Join a student organization");
        System.out.println("11. View news");
        System.out.println("12. Submit tech support request");
        System.out.println("13. Rate a teacher");
        System.out.println("14. Add comment to news");
        System.out.println("0.  Logout");
    }

    @Override
    protected void handleChoice(String choice) {
        switch (choice) {
            case "1":  viewProfile(); break;
            case "2":  viewAvailableCourses(); break;
            case "3":  registerForCourse(); break;
            case "4":  dropCourse(); break;
            case "5":  viewEnrollments(); break;
            case "6":  viewMarks(); break;
            case "7":  viewTranscript(); break;
            case "8":  viewTeacherInfo(); break;
            case "9":  viewOrganizations(); break;
            case "10": joinOrganization(); break;
            case "11": viewNews(); break;
            case "12": submitRequest(); break;
            case "13": rateTeacher(); break;
            case "14": addCommentToNews(); break;
            case "0":  logout(); break;
            default:   System.out.println("Invalid choice.");
        }
    }

    protected void viewProfile() {
        System.out.println("\n--- My Profile ---");
        System.out.printf("%-12s %s%n", "ID:",        student.getId());
        System.out.printf("%-12s %s %s%n", "Name:",   student.getFirstName(), student.getLastName());
        System.out.printf("%-12s %s%n", "Email:",     student.getEmail());
        System.out.printf("%-12s %s%n", "Major:",     student.getMajor());
        System.out.printf("%-12s %d%n", "Year:",      student.getYear());
        System.out.printf("%-12s %.2f%n", "GPA:",     student.getGpa());
        System.out.printf("%-12s %d / %d%n", "Credits:", student.getTotalCredits(), Student.MAX_CREDITS);
        System.out.printf("%-12s %d%n", "Fail Count:", student.getFailCount());
        System.out.printf("%-12s %s%n", "Language:",  student.getLanguage());

    }

    private void viewAvailableCourses() {
        List<Course> courses = storage.getCourses();
        System.out.println("\n--- Available Courses ---");
        if (courses.isEmpty()) {
            System.out.println("  No courses available.");
        } else {
            System.out.printf("%-6s %-35s %-8s %-15s %-10s %-20s%n",
                    "No.", "Name", "Credits", "Type", "Year", "Major");
            for (int i = 0; i < courses.size(); i++) {
                Course c = courses.get(i);
                System.out.printf("%-6d %-35s %-8d %-15s %-10d %-20s%n",
                        i + 1, c.getName(), c.getCredits(), c.getType(),
                        c.getTargetYear(), c.getTargetMajor());
            }
        }

    }

    private void registerForCourse() {
        List<Course> courses = storage.getCourses();
        if (courses.isEmpty()) { System.out.println("No courses available."); return; }

        System.out.println("\n--- Register for Course ---");
        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            System.out.println((i + 1) + ". " + c.getName() + " (" + c.getCredits() + " credits)");
        }
        System.out.print("Select course number (0 to cancel): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= courses.size()) { System.out.println("Cancelled."); return; }
        Course selected = courses.get(idx);

        List<model.academic.LessonGroup> groups = new java.util.ArrayList<>();
        for (model.academic.LessonGroup g : storage.getLessonGroups())
            if (g.getCourse().getCourseId().equals(selected.getCourseId())) groups.add(g);

        model.academic.LessonGroup chosenGroup = null;
        if (groups.isEmpty()) {
            System.out.println("No lesson groups available for this course.");
            return;
        } else if (groups.size() == 1) {
            chosenGroup = groups.get(0);
        } else {
            System.out.println("Select lesson group:");
            for (int i = 0; i < groups.size(); i++) {
                model.academic.LessonGroup g = groups.get(i);
                System.out.println((i + 1) + ". " + g.getType() + " | "
                        + g.getDayOfWeek() + " " + g.getStartTime() + "-" + g.getEndTime()
                        + " | " + g.getRoom());
            }
            System.out.print("Choice: ");
            int gIdx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
            if (gIdx < 0 || gIdx >= groups.size()) { System.out.println("Cancelled."); return; }
            chosenGroup = groups.get(gIdx);
        }

        try {
            student.registerForCourse(selected, chosenGroup);
            storage.updateAndSave();
        } catch (model.exceptions.CreditLimitExceededException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void dropCourse() {
        List<Enrollment> enrollments = student.getEnrollments();
        List<Enrollment> active = new java.util.ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.isActive()) active.add(e);
        }

        if (active.isEmpty()) { System.out.println("You have no active enrollments to drop."); return; }

        System.out.println("\n--- Drop Course ---");
        for (int i = 0; i < active.size(); i++) {
            System.out.println((i + 1) + ". " + active.get(i).getCourse().getName()
                    + " [" + active.get(i).getLessonGroup().getScheduleInfo() + "]");
        }
        System.out.print("Select enrollment to drop (0 to cancel): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= active.size()) { System.out.println("Cancelled."); return; }

        active.get(idx).drop();

    }

    private void viewEnrollments() {
        List<Enrollment> enrollments = student.getEnrollments();
        System.out.println("\n--- My Enrollments ---");
        if (enrollments.isEmpty()) {
            System.out.println("  No enrollments found.");
        } else {
            System.out.printf("%-30s %-10s %-12s %-22s %-8s%n",
                    "Course", "Group", "Day", "Time & Room", "Status");
            for (Enrollment e : enrollments) {
                System.out.printf("%-30s %-10s %-12s %-22s %-8s%n",
                        e.getCourse().getName(),
                        e.getLessonGroup().getGroupId(),
                        e.getDayOfWeek(),
                        e.getLessonGroup().getStartTime() + "-" + e.getLessonGroup().getEndTime()
                                + " " + e.getLessonGroup().getRoom(),
                        e.isActive() ? "Active" : "Dropped");
            }
        }

    }

    private void viewMarks() {
        List<Mark> marks = storage.getMarksForStudent(student);
        System.out.println("\n--- My Marks ---");
        if (marks.isEmpty()) {
            System.out.println("  No marks recorded yet.");
        } else {
            System.out.printf("%-35s %-10s %-10s %-10s %-10s %-8s%n",
                    "Course", "Attest.1", "Attest.2", "Final", "Total", "Passed");
            for (Mark m : marks) {
                System.out.printf("%-35s %-10.1f %-10.1f %-10.1f %-10.1f %-8s%n",
                        m.getCourse().getName(),
                        m.getFirstAttestation(),
                        m.getSecondAttestation(),
                        m.getFinalExam(),
                        m.getTotalMark(),
                        m.isPassed() ? "Yes" : "No");
            }
        }

    }

    private void viewTranscript() {
        List<Enrollment> enrollments = student.getEnrollments();
        List<Mark> marks = storage.getMarksForStudent(student);
        System.out.println("\n--- Transcript ---");
        System.out.println("Student : " + student.getFirstName() + " " + student.getLastName());
        System.out.println("Major   : " + student.getMajor() + "  |  Year: " + student.getYear());
        System.out.println("GPA     : " + student.getGpa());
        if (enrollments.isEmpty()) {
            System.out.println("  No courses enrolled.");
        } else {
            for (Enrollment e : enrollments) {
                String status = e.isActive() ? "In Progress" : "Dropped";
                Mark found = null;
                for (Mark m : marks) {
                    if (m.getCourse().getCourseId().equals(e.getCourse().getCourseId())) {
                        found = m;
                        break;
                    }
                }
                String grade = found != null ? String.format("%.1f", found.getTotalMark()) : "N/A";
                System.out.printf("  %-35s %-12s Grade: %s%n",
                        e.getCourse().getName(), status, grade);
            }
        }

    }

    private void viewTeacherInfo() {
        List<Course> courses = storage.getCourses();
        if (courses.isEmpty()) { System.out.println("No courses available."); return; }

        System.out.println("\n--- Select Course to View Teacher Info ---");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getName());
        }
        System.out.print("Select course (0 to cancel): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= courses.size()) { System.out.println("Cancelled."); return; }

        Course course = courses.get(idx);
        System.out.println("\n--- Teacher Info: " + course.getName() + " ---");
        List<Teacher> lecturers = course.getLecturers();
        if (lecturers.isEmpty()) {
            System.out.println("  No teachers assigned yet.");
        } else {
            for (Teacher t : lecturers) {
                System.out.printf("  %-20s %-15s %-25s %-12s%n",
                        t.getFirstName() + " " + t.getLastName(),
                        t.getPosition(),
                        t.getEmail(),
                        t.getDepartment());
            }
        }

    }

    private void viewOrganizations() {
        List<StudentOrganization> orgs = storage.getOrganizations();
        System.out.println("\n--- Student Organizations ---");
        if (orgs.isEmpty()) {
            System.out.println("  No organizations found.");
        } else {
            System.out.printf("%-6s %-30s %-10s %-40s%n", "No.", "Name", "Members", "Goal");
            for (int i = 0; i < orgs.size(); i++) {
                StudentOrganization o = orgs.get(i);
                boolean isMember = o.getMembers().stream()
                        .anyMatch(s -> s.getId().equals(student.getId()));
                System.out.printf("%-6d %-30s %-10d %-40s %s%n",
                        i + 1, o.getName(), o.getMembers().size(), o.getTarget(),
                        isMember ? "[Member]" : "");
            }
        }

    }

    private void joinOrganization() {
        List<StudentOrganization> orgs = storage.getOrganizations();
        if (orgs.isEmpty()) { System.out.println("No organizations available."); return; }

        System.out.println("\n--- Join Organization ---");
        for (int i = 0; i < orgs.size(); i++) {
            StudentOrganization o = orgs.get(i);
            boolean isMember = o.getMembers().stream()
                    .anyMatch(s -> s.getId().equals(student.getId()));
            System.out.println((i + 1) + ". " + o.getName()
                    + (isMember ? " [Already a member]" : ""));
        }
        System.out.print("Select organization (0 to cancel): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= orgs.size()) { System.out.println("Cancelled."); return; }

        StudentOrganization org = orgs.get(idx);
        boolean already = org.getMembers().stream()
                .anyMatch(s -> s.getId().equals(student.getId()));
        if (already) {
            System.out.println("You are already a member of " + org.getName() + ".");
        } else {
            student.joinOrganization(org);
        }

    }

    private void viewNews() {
        System.out.println("\n--- University News ---");
        if (storage.getNewsList().isEmpty()) {
            System.out.println("  No news available.");
        } else {
            for (News n : storage.getNewsList()) {
                System.out.println("\n  [" + n.getTopic() + "] " + n.getTitle());
                System.out.println("  " + n.getContent());
                System.out.println("  Posted by: "
                        + (n.getAuthor() != null
                            ? n.getAuthor().getFirstName() + " " + n.getAuthor().getLastName()
                            : "System")
                        + (n.isPinned() ? " [PINNED]" : ""));
                if (!n.getComments().isEmpty()) {
                    System.out.println("  Comments:");
                    for (String c : n.getComments()) System.out.println("    > " + c);
                }
                System.out.println("  ---");
            }
        }

    }

    private void submitRequest() {
        System.out.println("\n--- Submit Tech Support Request ---");
        System.out.print("Topic (e.g. Fix projector, Fix printer): ");
        String topic = scanner.nextLine().trim();
        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        String id = "REQ" + System.currentTimeMillis();
        model.communication.Request request = new model.communication.Request(id, topic, student, description);
        storage.saveRequest(request);
        System.out.println("Request submitted: " + topic);
    }

    private void rateTeacher() {
        List<model.users.Teacher> teachers = new java.util.ArrayList<>();
        for (model.users.User u : storage.getUsers().values())
            if (u instanceof model.users.Teacher) teachers.add((model.users.Teacher) u);

        if (teachers.isEmpty()) { System.out.println("No teachers found."); return; }

        System.out.println("\n--- Rate a Teacher ---");
        for (int i = 0; i < teachers.size(); i++)
            System.out.println((i + 1) + ". " + teachers.get(i).getFirstName()
                    + " " + teachers.get(i).getLastName()
                    + " [" + teachers.get(i).getPosition() + "]");
        System.out.print("Select teacher (0 to cancel): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= teachers.size()) { System.out.println("Cancelled."); return; }

        System.out.print("Rating (1-5): ");
        int rating = parseIntSafe(scanner.nextLine().trim(), 0);
        if (rating < 1 || rating > 5) { System.out.println("Invalid rating. Must be 1-5."); return; }

        student.rateTeacher(teachers.get(idx), rating);
        storage.updateAndSave();
    }

    private void addCommentToNews() {
        List<model.communication.News> newsList = storage.getNewsList();
        if (newsList.isEmpty()) { System.out.println("No news available."); return; }

        System.out.println("\n--- Add Comment to News ---");
        for (int i = 0; i < newsList.size(); i++)
            System.out.println((i + 1) + ". [" + newsList.get(i).getTopic() + "] "
                    + newsList.get(i).getTitle());
        System.out.print("Select news (0 to cancel): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= newsList.size()) { System.out.println("Cancelled."); return; }

        System.out.print("Your comment: ");
        String comment = scanner.nextLine().trim();
        if (comment.isEmpty()) { System.out.println("Comment cannot be empty."); return; }

        newsList.get(idx).getComments().add(student.getFirstName() + " " + student.getLastName() + ": " + comment);
        storage.updateAndSave();
        System.out.println("Comment added.");
    }
}