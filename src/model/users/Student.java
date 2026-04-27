package model.users;

import model.academic.Course;
import model.academic.Enrollment;
import model.academic.StudentOrganization;
import model.enums.Language;
import java.util.ArrayList;
import java.util.List;

public class Student extends User {

    public static final int MAX_CREDITS = 21;

    public static final int MAX_FAILS = 3;

    private String major;

    private int year;

    private double gpa;

    private int totalCredits;

    private int failCount;

    private List<Enrollment> enrollments;

    private List<StudentOrganization> organizations;

    public Student(String id, String password, String firstName, String lastName,
                   String email, Language language, String major, int year) {
        super(id, password, firstName, lastName, email, language);
        this.major = major;
        this.year = year;
        this.gpa = 0.0;
        this.totalCredits = 0;
        this.failCount = 0;
        this.enrollments = new ArrayList<>();
        this.organizations = new ArrayList<>();
    }

    public void registerForCourse(Course course) {
        if (totalCredits + course.getCredits() <= MAX_CREDITS) {
            System.out.println(getFirstName() + " registered for " + course.getName());
        } else {
            System.out.println("Credit limit exceeded.");
        }
    }

    public void dropCourse(Course course) {
        System.out.println(getFirstName() + " dropped " + course.getName());
    }

    public void viewTranscript() {
        System.out.println("Transcript for " + getFirstName() + " " + getLastName());
        enrollments.forEach(e -> System.out.println(" - " + e.getCourse().getName()));
    }

    public void viewMarks() {
        System.out.println("Marks for " + getFirstName() + ":");
        enrollments.forEach(System.out::println);
    }

    public void rateTeacher(Teacher teacher, int rating) {
        System.out.println(getFirstName() + " rated " + teacher.getFirstName() + ": " + rating + "/5");
    }

    public void joinOrganization(StudentOrganization org) {
        organizations.add(org);
        org.addMember(this);
    }

    public void viewTeacherInfo(Course course) {
        System.out.println("Teachers for " + course.getName() + ": " + course.getLecturers());
    }

    public String getMajor() { return major; }

    public void setMajor(String major) { this.major = major; }

    public int getYear() { return year; }

    public void setYear(int year) { this.year = year; }

    public double getGpa() { return gpa; }

    public void setGpa(double gpa) { this.gpa = gpa; }

    public int getTotalCredits() { return totalCredits; }

    public void setTotalCredits(int totalCredits) { this.totalCredits = totalCredits; }

    public int getFailCount() { return failCount; }

    public void setFailCount(int failCount) { this.failCount = failCount; }

    public List<Enrollment> getEnrollments() { return enrollments; }

    public void setEnrollments(List<Enrollment> enrollments) { this.enrollments = enrollments; }

    public List<StudentOrganization> getOrganizations() { return organizations; }

    public void setOrganizations(List<StudentOrganization> organizations) { this.organizations = organizations; }

    @Override
    public String toString() {
        return "Student{id='" + getId() + "', name='" + getFirstName() + " " + getLastName()
                + "', major='" + major + "', year=" + year + ", gpa=" + gpa + "}";
    }
}
