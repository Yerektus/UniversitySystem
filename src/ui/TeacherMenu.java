package ui;

import model.academic.Course;
import model.academic.LessonGroup;
import model.academic.Mark;
import model.communication.Complaint;
import model.communication.Message;
import model.enums.TeacherPosition;
import model.enums.UrgencyLevel;
import model.research.ResearchPaper;
import model.users.Employee;
import model.users.Student;
import model.users.Teacher;
import model.users.User;
import storage.DataStorage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class TeacherMenu extends BaseMenu {

    private final Teacher teacher;

    public TeacherMenu(Scanner scanner, DataStorage storage, Teacher teacher) {
        super(scanner, storage, teacher);
        this.teacher = teacher;
    }

    @Override
    protected void printMenu() {
        System.out.println("\n--- Teacher Menu [" + teacher.getFirstName() + " " + teacher.getLastName()
                + " | " + teacher.getPosition() + "] ---");
        System.out.println("1. View profile");
        System.out.println("2. Courses");
        System.out.println("3. Communication");
        if (teacher.getPosition() == TeacherPosition.PROFESSOR)
            System.out.println("4. Research");
        System.out.println("0. Logout");
    }

    @Override
    protected void handleChoice(String choice) {
        switch (choice) {
            case "1": viewProfile(); break;
            case "2": coursesMenu(); break;
            case "3": commMenu(); break;
            case "4": if (teacher.getPosition() == TeacherPosition.PROFESSOR) researchMenu();
                else System.out.println("Invalid choice."); break;
            case "0": logout(); break;
            default:  System.out.println("Invalid choice.");
        }
    }

    private void viewProfile() {
        while (true) {
            System.out.println("\n--- Teacher Menu: Profile ---");
            System.out.printf("%-10s %s%n",    "ID:",       teacher.getId());
            System.out.printf("%-10s %s %s%n", "Name:",     teacher.getFirstName(), teacher.getLastName());
            System.out.printf("%-10s %s%n",    "Email:",    teacher.getEmail());
            System.out.printf("%-10s %s%n",    "Dept:",     teacher.getDepartment());
            System.out.printf("%-10s %s%n",    "Position:", teacher.getPosition());
            System.out.printf("%-10s %s%n",    "Language:", teacher.getLanguage());
            if (teacher.getPosition() == TeacherPosition.PROFESSOR) {
                System.out.printf("%-10s %d%n", "H-index:", teacher.calculateHindex());
                System.out.printf("%-10s %d%n", "Papers:",  teacher.getPapers().size());
            }
            System.out.println("\n1. Change language");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            switch (scanner.nextLine().trim()) {
                case "1": changeLanguage(); break;
                case "0": return;
                default:  System.out.println("Invalid choice.");
            }
        }
    }

    private void coursesMenu() {
        while (true) {
            System.out.println("\n--- Teacher Menu: Courses ---");
            System.out.println("1. View my courses");
            System.out.println("2. Put / update marks");
            System.out.println("3. View students in a course");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            switch (scanner.nextLine().trim()) {
                case "1": viewCourses(); break;
                case "2": putMarks(); break;
                case "3": viewStudents(); break;
                case "0": return;
                default:  System.out.println("Invalid choice.");
            }
        }
    }

    private void commMenu() {
        while (true) {
            System.out.println("\n--- Teacher Menu: Communication ---");
            System.out.println("1. Send complaint about a student");
            System.out.println("2. Send message to employee");
            System.out.println("3. View inbox");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            switch (scanner.nextLine().trim()) {
                case "1": sendComplaint(); break;
                case "2": sendMessage(); break;
                case "3": viewInbox(); break;
                case "0": return;
                default:  System.out.println("Invalid choice.");
            }
        }
    }

    private void researchMenu() {
        while (true) {
            System.out.println("\n--- Teacher Menu: Research ---");
            System.out.println("1. View my papers");
            System.out.println("2. Publish new paper");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            switch (scanner.nextLine().trim()) {
                case "1": viewPapers(); break;
                case "2": publishPaper(); break;
                case "0": return;
                default:  System.out.println("Invalid choice.");
            }
        }
    }

    private void viewCourses() {
        List<Course> myCourses = getTeacherCourses();
        if (myCourses.isEmpty()) { System.out.println("No assigned courses."); return; }
        System.out.println("\n--- My Courses ---");
        for (int i = 0; i < myCourses.size(); i++) {
            Course c = myCourses.get(i);
            System.out.println((i + 1) + ". " + c.getCourseId() + " - " + c.getName()
                    + " (" + c.getCredits() + " credits, " + c.getType() + ")");
            for (LessonGroup g : storage.getLessonGroups())
                if (g.getCourse().getCourseId().equals(c.getCourseId())
                        && g.getTeacher().getId().equals(teacher.getId()))
                    System.out.println("   " + g.getType() + " | " + g.getDayOfWeek()
                            + " " + g.getStartTime() + "-" + g.getEndTime()
                            + " | " + g.getRoom() + " | " + g.getSemester());
        }
    }

    private void putMarks() {
        List<Course> myCourses = getTeacherCourses();
        if (myCourses.isEmpty()) { System.out.println("No courses assigned."); return; }

        System.out.println("\n--- Select Course ---");
        for (int i = 0; i < myCourses.size(); i++)
            System.out.println((i + 1) + ". " + myCourses.get(i).getName());
        System.out.print("Choice (0 to cancel): ");
        int ci = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (ci < 0 || ci >= myCourses.size()) { System.out.println("Cancelled."); return; }
        Course course = myCourses.get(ci);

        List<Student> students = course.getEnrolledStudents();
        if (students.isEmpty()) { System.out.println("No students enrolled."); return; }

        System.out.println("\n--- Select Student ---");
        for (int i = 0; i < students.size(); i++)
            System.out.println((i + 1) + ". " + students.get(i).getFirstName() + " " + students.get(i).getLastName());
        System.out.print("Choice (0 to cancel): ");
        int si = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (si < 0 || si >= students.size()) { System.out.println("Cancelled."); return; }
        Student student = students.get(si);

        Mark mark = null;
        for (Mark m : storage.getMarks())
            if (m.getStudent().getId().equals(student.getId())
                    && m.getCourse().getCourseId().equals(course.getCourseId())) { mark = m; break; }
        if (mark == null) {
            mark = new Mark(student, course, getLessonGroupForCourse(course));
            storage.saveMark(mark);
        }

        System.out.println("Current: 1st=" + mark.getFirstAttestation()
                + "  2nd=" + mark.getSecondAttestation()
                + "  Final=" + mark.getFinalExam()
                + "  Total=" + mark.getTotalMark());
        System.out.println("Leave blank to keep current value.");

        System.out.print("1st attestation (max 30): ");
        String v1 = scanner.nextLine().trim();
        if (!v1.isEmpty()) mark.setFirstAttestation(parseDoubleSafe(v1, mark.getFirstAttestation()));

        System.out.print("2nd attestation (max 30): ");
        String v2 = scanner.nextLine().trim();
        if (!v2.isEmpty()) mark.setSecondAttestation(parseDoubleSafe(v2, mark.getSecondAttestation()));

        System.out.print("Final exam (max 40): ");
        String v3 = scanner.nextLine().trim();
        if (!v3.isEmpty()) mark.setFinalExam(parseDoubleSafe(v3, mark.getFinalExam()));

        storage.updateAndSave();
        System.out.println("Saved. Total: " + mark.getTotalMark() + " — " + (mark.isPassed() ? "PASSED" : "FAILED"));
    }

    private void viewStudents() {
        List<Course> myCourses = getTeacherCourses();
        if (myCourses.isEmpty()) { System.out.println("No courses assigned."); return; }

        System.out.println("\n--- Select Course ---");
        for (int i = 0; i < myCourses.size(); i++)
            System.out.println((i + 1) + ". " + myCourses.get(i).getName());
        System.out.print("Choice (0 to cancel): ");
        int ci = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (ci < 0 || ci >= myCourses.size()) { System.out.println("Cancelled."); return; }
        Course course = myCourses.get(ci);

        List<Student> students = course.getEnrolledStudents();
        if (students.isEmpty()) { System.out.println("No students enrolled."); return; }

        System.out.println("\n--- Students in " + course.getName() + " ---");
        System.out.printf("%-12s %-20s %-20s %-6s %-6s %-6s %-8s%n",
                "ID", "First", "Last", "1st", "2nd", "Final", "Total");
        for (Student s : students) {
            Mark mark = null;
            for (Mark m : storage.getMarks())
                if (m.getStudent().getId().equals(s.getId())
                        && m.getCourse().getCourseId().equals(course.getCourseId())) mark = m;
            if (mark != null)
                System.out.printf("%-12s %-20s %-20s %-6.1f %-6.1f %-6.1f %-8.1f%n",
                        s.getId(), s.getFirstName(), s.getLastName(),
                        mark.getFirstAttestation(), mark.getSecondAttestation(),
                        mark.getFinalExam(), mark.getTotalMark());
            else
                System.out.printf("%-12s %-20s %-20s %-6s %-6s %-6s %-8s%n",
                        s.getId(), s.getFirstName(), s.getLastName(), "-", "-", "-", "-");
        }
    }

    private void sendComplaint() {
        List<Student> allStudents = new ArrayList<>();
        for (User u : storage.getUsers().values())
            if (u instanceof Student) allStudents.add((Student) u);
        if (allStudents.isEmpty()) { System.out.println("No students found."); return; }

        System.out.println("\n--- Select Student ---");
        for (int i = 0; i < allStudents.size(); i++)
            System.out.println((i + 1) + ". " + allStudents.get(i).getFirstName() + " " + allStudents.get(i).getLastName());
        System.out.print("Choice (0 to cancel): ");
        int si = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (si < 0 || si >= allStudents.size()) { System.out.println("Cancelled."); return; }
        Student student = allStudents.get(si);

        System.out.println("Urgency: 1. LOW  2. MEDIUM  3. HIGH");
        System.out.print("Choice: ");
        UrgencyLevel level;
        switch (scanner.nextLine().trim()) {
            case "1": level = UrgencyLevel.LOW;    break;
            case "3": level = UrgencyLevel.HIGH;   break;
            default:  level = UrgencyLevel.MEDIUM; break;
        }

        System.out.print("Describe the issue: ");
        String description = scanner.nextLine().trim();

        Complaint complaint = new Complaint(UUID.randomUUID().toString(),
                teacher, student, description, level, new Date());
        storage.saveComplaint(complaint);
        System.out.println("Complaint sent [" + level + "] about " + student.getFirstName() + " " + student.getLastName());
    }

    private void sendMessage() {
        List<Employee> employees = new ArrayList<>();
        for (User u : storage.getUsers().values())
            if (u instanceof Employee && !u.getId().equals(teacher.getId()))
                employees.add((Employee) u);
        if (employees.isEmpty()) { System.out.println("No employees found."); return; }

        System.out.println("\n--- Select Recipient ---");
        for (int i = 0; i < employees.size(); i++)
            System.out.println((i + 1) + ". " + employees.get(i).getFirstName()
                    + " " + employees.get(i).getLastName()
                    + " (" + employees.get(i).getClass().getSimpleName() + ")");
        System.out.print("Choice (0 to cancel): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= employees.size()) { System.out.println("Cancelled."); return; }
        Employee recipient = employees.get(idx);

        System.out.print("Topic: ");   String topic   = scanner.nextLine().trim();
        System.out.print("Message: "); String content = scanner.nextLine().trim();

        Message msg = new Message(UUID.randomUUID().toString(), topic, content, teacher, recipient);
        storage.saveMessage(msg);
        System.out.println("Message sent to " + recipient.getFirstName() + " " + recipient.getLastName());
    }

    private void viewInbox() {
        List<Message> inbox = storage.getMessagesForUser(teacher);
        System.out.println("\n--- Inbox (" + inbox.size() + ") ---");
        if (inbox.isEmpty()) { System.out.println("No messages."); return; }
        for (Message m : inbox) {
            System.out.println("From   : " + m.getSender().getFirstName() + " " + m.getSender().getLastName());
            System.out.println("Topic  : " + m.getTopic());
            System.out.println("Message: " + m.getContent());
            System.out.println("Sent   : " + m.getSentAt());
            System.out.println("---");
        }
    }

    private void viewPapers() {
        if (teacher.getPapers().isEmpty()) { System.out.println("No papers published yet."); return; }
        System.out.println("Sort by: 1. Citations  2. Date  3. Length");
        System.out.print("Choice: ");
        switch (scanner.nextLine().trim()) {
            case "1": teacher.printPapers(ResearchPaper.BY_CITATIONS); break;
            case "3": teacher.printPapers(ResearchPaper.BY_LENGTH); break;
            default:  teacher.printPapers(ResearchPaper.BY_DATE); break;
        }
    }

    private void publishPaper() {
        System.out.print("Title (0 to cancel): ");
        String title = scanner.nextLine().trim();
        if (title.equals("0")) return;
        System.out.print("Journal: "); String journal = scanner.nextLine().trim();
        System.out.print("Pages: ");   int pages      = parseIntSafe(scanner.nextLine().trim(), 1);
        System.out.print("DOI: ");     String doi     = scanner.nextLine().trim();

        ResearchPaper paper = new ResearchPaper(title, journal, pages, doi);
        paper.getAuthors().add(teacher);
        teacher.publishPaper(paper);
        storage.updateAndSave();
        System.out.println("Paper published.");
    }

    private List<Course> getTeacherCourses() {
        List<Course> result = new ArrayList<>();
        for (Course c : storage.getCourses()) {
            for (Teacher t : c.getLecturers())
                if (t.getId().equals(teacher.getId())) { result.add(c); break; }
            for (Teacher t : c.getPracticeInstructors())
                if (t.getId().equals(teacher.getId()) && !result.contains(c)) { result.add(c); break; }
        }
        return result;
    }

    private LessonGroup getLessonGroupForCourse(Course course) {
        for (LessonGroup g : storage.getLessonGroups())
            if (g.getCourse().getCourseId().equals(course.getCourseId())
                    && g.getTeacher().getId().equals(teacher.getId())) return g;
        return null;
    }
}