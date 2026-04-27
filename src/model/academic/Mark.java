package model.academic;

import model.users.Student;

public class Mark {

    private Student student;
    private Course course;
    private LessonGroup lessonGroup;
    private double firstAttestation;
    private double secondAttestation;
    private double finalExam;
    private boolean isPassed;

    public Mark(Student student, Course course, LessonGroup lessonGroup) {
        this.student = student;
        this.course = course;
        this.lessonGroup = lessonGroup;
        this.firstAttestation = 0;
        this.secondAttestation = 0;
        this.finalExam = 0;
        this.isPassed = false;
    }

    public double getTotalMark() {
        return firstAttestation + secondAttestation + finalExam;
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

    public void setFinalExam(double finalExam) {
        this.finalExam = finalExam;
        this.isPassed = getTotalMark() >= 50;
    }

    public boolean isPassed() { return isPassed; }

    public void setPassed(boolean passed) { isPassed = passed; }

    @Override
    public String toString() {
        return "Mark{student=" + student.getFirstName() + ", course=" + course.getName()
                + ", total=" + getTotalMark() + ", passed=" + isPassed + "}";
    }
}
