package model.academic;

import model.enums.DayOfWeek;
import model.users.Student;
import java.util.Date;

public class Enrollment {

    private String enrollmentId;
    private Student student;
    private Course course;
    private LessonGroup lessonGroup;
    private Date enrolledAt;
    private boolean isActive;

    public Enrollment(String enrollmentId, Student student, Course course, LessonGroup lessonGroup) {
        this.enrollmentId = enrollmentId;
        this.student = student;
        this.course = course;
        this.lessonGroup = lessonGroup;
        this.enrolledAt = new Date();
        this.isActive = true;
    }

    public DayOfWeek getDayOfWeek() {
        return lessonGroup.getDayOfWeek();
    }

    public void drop() {
        this.isActive = false;
        System.out.println(student.getFirstName() + " dropped " + course.getName());
    }

    public String getEnrollmentId() { return enrollmentId; }

    public void setEnrollmentId(String enrollmentId) { this.enrollmentId = enrollmentId; }

    public Student getStudent() { return student; }

    public void setStudent(Student student) { this.student = student; }

    public Course getCourse() { return course; }

    public void setCourse(Course course) { this.course = course; }

    public LessonGroup getLessonGroup() { return lessonGroup; }

    public void setLessonGroup(LessonGroup lessonGroup) { this.lessonGroup = lessonGroup; }

    public Date getEnrolledAt() { return enrolledAt; }

    public void setEnrolledAt(Date enrolledAt) { this.enrolledAt = enrolledAt; }

    public boolean isActive() { return isActive; }

    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        return "Enrollment{student=" + student.getFirstName() + ", course=" + course.getName()
                + ", active=" + isActive + "}";
    }
}
