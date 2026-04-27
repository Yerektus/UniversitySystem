package model.academic;
import model.enums.CourseType;
import model.users.Student;
import model.users.Teacher;
import java.util.ArrayList;
import java.util.List;
public class Course {

    private String courseId;

    private String name;

    private int credits;

    private CourseType type;

    private String targetMajor;

    private int targetYear;

    private List<Teacher> lecturers;

    private List<Teacher> practiceInstructors;

    private List<Student> enrolledStudents;

    public Course(String courseId, String name, int credits, CourseType type) {
        this.courseId = courseId;
        this.name = name;
        this.credits = credits;
        this.type = type;
        this.lecturers = new ArrayList<>();
        this.practiceInstructors = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
    }

    public void addLecturer(Teacher teacher) {
        lecturers.add(teacher);
    }

    public void addPracticalInstructor(Teacher teacher) {
        practiceInstructors.add(teacher);
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public String getCourseId() { return courseId; }

    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getCredits() { return credits; }

    public void setCredits(int credits) { this.credits = credits; }

    public CourseType getType() { return type; }

    public void setType(CourseType type) { this.type = type; }

    public String getTargetMajor() { return targetMajor; }

    public void setTargetMajor(String targetMajor) { this.targetMajor = targetMajor; }

    public int getTargetYear() { return targetYear; }

    public void setTargetYear(int targetYear) { this.targetYear = targetYear; }

    public List<Teacher> getLecturers() { return lecturers; }

    public void setLecturers(List<Teacher> lecturers) { this.lecturers = lecturers; }

    public List<Teacher> getPracticeInstructors() { return practiceInstructors; }

    public void setPracticeInstructors(List<Teacher> practiceInstructors) { this.practiceInstructors = practiceInstructors; }

    @Override
    public String toString() {
        return "Course{id='" + courseId + "', name='" + name + "', credits=" + credits + ", type=" + type + "}";
    }
} 
