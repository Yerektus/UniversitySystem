package ui;

import model.academic.Course;
import model.academic.Enrollment;
import model.academic.LessonGroup;
import model.academic.Mark;
import model.enums.CitationFormat;
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
        System.out.println("\n--- Student Menu [" + student.getFirstName() + "] ---");
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
        System.out.println("0.  Logout");
    }

    @Override
    protected void handleChoice(String choice) {
        switch (choice) {
            case "1": viewProfile(); break;
            case "2": viewAvailableCourses(); break;
            case "3": registerForCourse(); break;
            case "4": dropCourse(); break;
            case "5": viewEnrollments(); break;
            case "6": viewMarks(); break;
            case "7": student.viewTranscript(); pause(); break;
            case "8": viewTeacherInfo(); break;
            case "9": viewOrganizations(); break;
            case "10": joinOrganization(); break;
            case "0": logout(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    protected void viewProfile() {
        System.out.println("\n--- My Profile ---");
        System.out.println("ID:       " + student.getId());
        System.out.println("Name:     " + student.getFirstName() + " " + student.getLastName());
        System.out.println("Email:    " + student.getEmail());
        System.out.println("Major:    " + student.getMajor());
        System.out.println("Year:     " + student.getYear());
        System.out.println("GPA:      " + student.getGpa());
        System.out.println("Credits:  " + student.getTotalCredits() + "/" + Student.MAX_CREDITS);
        System.out.println("Language: " + student.getLanguage());
        pause();
    }

    private void viewAvailableCourses() {
        System.out.println("\n--- Available Courses ---");
        List<Course> courses = storage.getCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
        } else {
            for (int i = 0; i < courses.size(); i++) {
                Course c = courses.get(i);
                System.out.println((i + 1) + ". " + c.getName()
                        + " [" + c.getCourseId() + "] - " + c.getCredits() + " credits - " + c.getType());
            }
        }
        pause();
    }

    private void registerForCourse() {
        List<Course> courses = storage.getCourses();
        if (courses.isEmpty()) { System.out.println("No courses available."); return; }

        System.out.println("\n--- Register for Course ---");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getName());
        }
        System.out.print("Select course number: ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= courses.size()) { System.out.println("Invalid selection."); return; }

        student.registerForCourse(courses.get(idx));
        pause();
    }

    private void dropCourse() {
        List<Enrollment> enrollments = student.getEnrollments();
        if (enrollments.isEmpty()) { System.out.println("You have no active enrollments."); return; }

        System.out.println("\n--- Drop Course ---");
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment e = enrollments.get(i);
            if (e.isActive()) {
                System.out.println((i + 1) + ". " + e.getCourse().getName());
            }
        }
        System.out.print("Select enrollment number to drop: ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= enrollments.size()) { System.out.println("Invalid selection."); return; }

        enrollments.get(idx).drop();
        pause();
    }

    private void viewEnrollments() {
        System.out.println("\n--- My Enrollments ---");
        List<Enrollment> enrollments = student.getEnrollments();
        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found.");
        } else {
            for (Enrollment e : enrollments) {
                System.out.println("- " + e.getCourse().getName()
                        + " | Group: " + e.getLessonGroup().getGroupId()
                        + " | " + e.getDayOfWeek()
                        + " | Active: " + e.isActive());
            }
        }
        pause();
    }

    private void viewMarks() {
        student.viewMarks();
        pause();
    }

    private void viewTeacherInfo() {
        List<Course> courses = storage.getCourses();
        if (courses.isEmpty()) { System.out.println("No courses available."); return; }

        System.out.println("\n--- View Teacher Info ---");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getName());
        }
        System.out.print("Select course: ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= courses.size()) { System.out.println("Invalid selection."); return; }

        student.viewTeacherInfo(courses.get(idx));
        pause();
    }

    private void viewOrganizations() {
        System.out.println("\n--- Student Organizations ---");
        if (storage.getOrganizations().isEmpty()) {
            System.out.println("No organizations found.");
        } else {
            storage.getOrganizations().forEach(o ->
                System.out.println("- " + o.getName() + " | Members: " + o.getMembers().size())
            );
        }
        pause();
    }

    private void joinOrganization() {
        if (storage.getOrganizations().isEmpty()) { System.out.println("No organizations available."); return; }

        System.out.println("\n--- Join Organization ---");
        var orgs = storage.getOrganizations();
        for (int i = 0; i < orgs.size(); i++) {
            System.out.println((i + 1) + ". " + orgs.get(i).getName());
        }
        System.out.print("Select organization: ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= orgs.size()) { System.out.println("Invalid selection."); return; }

        student.joinOrganization(orgs.get(idx));
        pause();
    }
}
