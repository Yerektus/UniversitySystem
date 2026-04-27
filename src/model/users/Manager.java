package model.users;

import model.academic.Course;
import model.academic.Enrollment;
import model.academic.LessonGroup;
import model.communication.News;
import model.enums.Language;
import model.enums.ManagerType;
import java.util.List;

public class Manager extends Employee {

    private ManagerType type;

    public Manager(String id, String password, String firstName, String lastName,
                   String email, Language language, String department, ManagerType type) {
        super(id, password, firstName, lastName, email, language, department);
        this.type = type;
    }

    public void assignCourseToTeacher(Course course, Teacher teacher) {
        course.addLecturer(teacher);
        System.out.println("Assigned " + course.getName() + " to " + teacher.getFirstName());
    }

    public void approveCourseRegistration(Student student, Course course) {
        System.out.println("Approved " + student.getFirstName() + " for " + course.getName());
    }

    public void addCourseForRegistration(Course course, String major, int year) {
        course.setTargetMajor(major);
        course.setTargetYear(year);
        System.out.println("Course " + course.getName() + " opened for registration.");
    }

    public void manageNews(News news) {
        System.out.println("Managing news: " + news.getTitle());
    }

    public void viewComparator() {
        System.out.println("Viewing student comparator...");
    }

    public void viewStudentsOfLessonGroup(LessonGroup group) {
        System.out.println("Students in group " + group.getGroupId() + ":");
        group.getEnrolledStudents().forEach(s -> System.out.println(" - " + s.getFirstName()));
    }

    public List<Enrollment> viewStudentSchedule(Student student) {
        return student.getEnrollments();
    }

    public ManagerType getType() { return type; }

    public void setType(ManagerType type) { this.type = type; }
}
