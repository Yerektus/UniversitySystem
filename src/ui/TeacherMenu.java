package ui;

import model.academic.Course;
import model.academic.LessonGroup;
import model.academic.Mark;
import model.communication.Complaint;
import model.communication.Message;
import model.enums.TeacherPosition;
import model.enums.UrgencyLevel;
import model.research.ResearchPaper;
import model.research.ResearchProject;
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
        System.out.println("\n--- Teacher Menu [" + teacher.getFirstName() + " " + teacher.getLastName() + "] ---");
        System.out.println("1. View profile");
        System.out.println("2. View my courses");
        System.out.println("3. Put / update marks");
        System.out.println("4. View students in a course");
        System.out.println("5. Send complaint about a student");
        System.out.println("6. Send message to employee");
        if (teacher.getPosition() == TeacherPosition.PROFESSOR) {
            System.out.println("7. Research papers");
        }
        System.out.println("0. Logout");
    }

    @Override
    protected void handleChoice(String choice) {
        switch (choice) {
            case "1": viewProfile();         break;
            case "2": viewCourses();         break;
            case "3": putMarks();            break;
            case "4": viewStudents();        break;
            case "5": sendComplaint();       break;
            case "6": sendMessage();         break;
            case "7":
                if (teacher.getPosition() == TeacherPosition.PROFESSOR) researchMenu();
                else System.out.println("Invalid choice.");
                break;
            case "0": logout();              break;
            default:  System.out.println("Invalid choice.");
        }
    }

    // ── 1. View profile ───────────────────────────────────────────────────────

    private void viewProfile() {
        System.out.println("\n--- Profile ---");
        System.out.println("Name     : " + teacher.getFirstName() + " " + teacher.getLastName());
        System.out.println("Email    : " + teacher.getEmail());
        System.out.println("Dept     : " + teacher.getDepartment());
        System.out.println("Position : " + teacher.getPosition());
        System.out.println("Language : " + teacher.getLanguage());
        if (teacher.getPosition() == TeacherPosition.PROFESSOR) {
            System.out.println("H-index  : " + teacher.calculateHindex());
            System.out.println("Papers   : " + teacher.getPapers().size());
        }
        pause();
    }

    // ── 2. View my courses ────────────────────────────────────────────────────

    private void viewCourses() {
        List<Course> myCourses = getTeacherCourses();
        if (myCourses.isEmpty()) {
            System.out.println("You have no assigned courses.");
            pause();
            return;
        }
        System.out.println("\n--- My Courses ---");
        for (int i = 0; i < myCourses.size(); i++) {
            Course c = myCourses.get(i);
            System.out.println((i + 1) + ". " + c.getCourseId() + " - " + c.getName()
                    + " (" + c.getCredits() + " credits, " + c.getType() + ")");
            for (LessonGroup g : storage.getLessonGroups()) {
                if (g.getCourse().getCourseId().equals(c.getCourseId())
                        && g.getTeacher().getId().equals(teacher.getId())) {
                    System.out.println("   " + g.getType() + " | " + g.getDayOfWeek()
                            + " " + g.getStartTime() + "-" + g.getEndTime()
                            + " | " + g.getRoom() + " | " + g.getSemester());
                }
            }
        }
        pause();
    }

    // ── 3. Put / update marks ─────────────────────────────────────────────────

    private void putMarks() {
        List<Course> myCourses = getTeacherCourses();
        if (myCourses.isEmpty()) { System.out.println("No courses assigned."); return; }

        System.out.println("\n--- Select Course ---");
        for (int i = 0; i < myCourses.size(); i++)
            System.out.println((i + 1) + ". " + myCourses.get(i).getName());
        System.out.print("Choice: ");
        int ci = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (ci < 0 || ci >= myCourses.size()) { System.out.println("Invalid."); return; }
        Course course = myCourses.get(ci);

        List<Student> students = course.getEnrolledStudents();
        if (students.isEmpty()) { System.out.println("No students enrolled."); return; }

        System.out.println("\n--- Select Student ---");
        for (int i = 0; i < students.size(); i++)
            System.out.println((i + 1) + ". " + students.get(i).getFirstName()
                    + " " + students.get(i).getLastName());
        System.out.print("Choice: ");
        int si = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (si < 0 || si >= students.size()) { System.out.println("Invalid."); return; }
        Student student = students.get(si);

        // Find or create mark
        Mark mark = null;
        for (Mark m : storage.getMarks()) {
            if (m.getStudent().getId().equals(student.getId())
                    && m.getCourse().getCourseId().equals(course.getCourseId())) {
                mark = m;
                break;
            }
        }
        LessonGroup group = getLessonGroupForCourse(course);
        if (mark == null) {
            mark = new Mark(student, course, group);
            storage.saveMark(mark);
        }

        System.out.println("\nCurrent: 1st=" + mark.getFirstAttestation()
                + "  2nd=" + mark.getSecondAttestation()
                + "  Final=" + mark.getFinalExam()
                + "  Total=" + mark.getTotalMark());
        System.out.println("Enter values (leave blank to keep current):");

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
        System.out.println("Marks saved. Total: " + mark.getTotalMark()
                + " — " + (mark.isPassed() ? "PASSED" : "FAILED"));
    }

    // ── 4. View students in a course ──────────────────────────────────────────

    private void viewStudents() {
        List<Course> myCourses = getTeacherCourses();
        if (myCourses.isEmpty()) { System.out.println("No courses assigned."); return; }

        System.out.println("\n--- Select Course ---");
        for (int i = 0; i < myCourses.size(); i++)
            System.out.println((i + 1) + ". " + myCourses.get(i).getName());
        System.out.print("Choice: ");
        int ci = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (ci < 0 || ci >= myCourses.size()) { System.out.println("Invalid."); return; }
        Course course = myCourses.get(ci);

        List<Student> students = course.getEnrolledStudents();
        if (students.isEmpty()) { System.out.println("No students enrolled."); pause(); return; }

        System.out.println("\n--- Students in " + course.getName() + " ---");
        System.out.printf("%-20s %-20s %-6s %-6s %-6s %-8s%n",
                "First", "Last", "1st", "2nd", "Final", "Total");
        System.out.println("-".repeat(70));
        for (Student s : students) {
            Mark mark = null;
            for (Mark m : storage.getMarks())
                if (m.getStudent().getId().equals(s.getId())
                        && m.getCourse().getCourseId().equals(course.getCourseId()))
                    mark = m;
            if (mark != null) {
                System.out.printf("%-20s %-20s %-6.1f %-6.1f %-6.1f %-8.1f%n",
                        s.getFirstName(), s.getLastName(),
                        mark.getFirstAttestation(), mark.getSecondAttestation(),
                        mark.getFinalExam(), mark.getTotalMark());
            } else {
                System.out.printf("%-20s %-20s %-6s %-6s %-6s %-8s%n",
                        s.getFirstName(), s.getLastName(), "-", "-", "-", "-");
            }
        }
        pause();
    }

    // ── 5. Send complaint ─────────────────────────────────────────────────────

    private void sendComplaint() {
        List<Student> allStudents = new ArrayList<>();
        for (User u : storage.getUsers().values())
            if (u instanceof Student) allStudents.add((Student) u);

        if (allStudents.isEmpty()) { System.out.println("No students found."); return; }

        System.out.println("\n--- Select Student ---");
        for (int i = 0; i < allStudents.size(); i++)
            System.out.println((i + 1) + ". " + allStudents.get(i).getFirstName()
                    + " " + allStudents.get(i).getLastName());
        System.out.print("Choice: ");
        int si = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (si < 0 || si >= allStudents.size()) { System.out.println("Invalid."); return; }
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

        Complaint complaint = new Complaint(
                UUID.randomUUID().toString(),
                teacher.getFirstName() + " " + teacher.getLastName(),
                student.getFirstName() + " " + student.getLastName(),
                description, level, new Date());
        System.out.println("Complaint sent [" + level + "] about "
                + student.getFirstName() + " " + student.getLastName());
    }

    // ── 6. Send message ───────────────────────────────────────────────────────

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
        System.out.print("Choice: ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= employees.size()) { System.out.println("Invalid."); return; }
        Employee recipient = employees.get(idx);

        System.out.print("Topic: ");
        String topic = scanner.nextLine().trim();
        System.out.print("Message: ");
        String content = scanner.nextLine().trim();

        Message msg = new Message(UUID.randomUUID().toString(), topic, content, teacher, recipient);
        storage.updateAndSave();
        System.out.println("Message sent to " + recipient.getFirstName() + " " + recipient.getLastName());
    }

    // ── 7. Research (PROFESSOR only) ──────────────────────────────────────────

    private void researchMenu() {
        System.out.println("\n--- Research ---");
        System.out.println("1. View my papers");
        System.out.println("2. Publish new paper");
        System.out.print("Choice: ");
        switch (scanner.nextLine().trim()) {
            case "1": viewPapers();   break;
            case "2": publishPaper(); break;
            default:  System.out.println("Invalid.");
        }
    }

    private void viewPapers() {
        if (teacher.getPapers().isEmpty()) {
            System.out.println("No papers published yet.");
            pause();
            return;
        }
        System.out.println("Sort by: 1. Citations  2. Date  3. Length");
        System.out.print("Choice: ");
        switch (scanner.nextLine().trim()) {
            case "1": teacher.printPapers(model.research.ResearchPaper.BY_CITATIONS); break;
            case "3": teacher.printPapers(model.research.ResearchPaper.BY_LENGTH);    break;
            default:  teacher.printPapers(model.research.ResearchPaper.BY_DATE);      break;
        }
        pause();
    }

    private void publishPaper() {
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Journal: ");
        String journal = scanner.nextLine().trim();
        System.out.print("Pages: ");
        int pages = parseIntSafe(scanner.nextLine().trim(), 1);
        System.out.print("DOI: ");
        String doi = scanner.nextLine().trim();

        ResearchPaper paper = new ResearchPaper(title, journal, pages, doi);
        paper.getAuthors().add(teacher);
        teacher.publishPaper(paper);
        storage.updateAndSave();
        System.out.println("Paper published successfully.");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

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
                    && g.getTeacher().getId().equals(teacher.getId()))
                return g;
        return null;
    }
}

