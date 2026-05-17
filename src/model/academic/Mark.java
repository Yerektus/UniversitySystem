package model.academic;

import model.users.Student;
import java.io.Serializable;

public class Mark implements Serializable {

    private Student student;
    private Course course;
    private LessonGroup lessonGroup;
    private double firstAttestation;
    private double secondAttestation;
    private double finalExam;

    public Mark(Student student, Course course, LessonGroup lessonGroup) {
        this.student = student;
        this.course = course;
        this.lessonGroup = lessonGroup;
        this.firstAttestation = 0;
        this.secondAttestation = 0;
        this.finalExam = 0;
    }

    public double getTotalMark() {
        return firstAttestation + secondAttestation + finalExam;
    }

    public boolean isPassed() {
        return getTotalMark() >= 50;
    }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public LessonGroup getLessonGroup() { return lessonGroup; }
    public void setLessonGroup(LessonGroup lessonGroup) { this.lessonGroup = lessonGroup; }
    public double getFirstAttestation() { return firstAttestation; }
    public void setFirstAttestation(double firstAttestation) { this.firstAttestation = firstAttestation; }
    public double getSecondAttestation() { return secondAttestation; }
    public void setSecondAttestation(double secondAttestation) { this.secondAttestation = secondAttestation; }
    public double getFinalExam() { return finalExam; }
    public void setFinalExam(double finalExam) { this.finalExam = finalExam; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Mark)) return false;
        Mark other = (Mark) obj;
        return student.getId().equals(other.student.getId())
                && course.getCourseId().equals(other.course.getCourseId());
    }

    @Override
    public int hashCode() {
        return (student.getId() + course.getCourseId()).hashCode();
    }

    @Override
    public String toString() {
        return "Mark{student=" + student.getFirstName() + " " + student.getLastName()
                + ", course=" + course.getName()
                + ", total=" + getTotalMark()
                + ", passed=" + isPassed() + "}";
    }
}