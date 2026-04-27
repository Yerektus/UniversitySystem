package model.academic;
import model.enums.DayOfWeek;
import model.enums.LessonType;
import model.users.Student;
import model.users.Teacher;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class LessonGroup {

    private String groupId;

    private Course course;

    private LessonType type;

    private Teacher teacher;

    private DayOfWeek dayOfWeek;

    private String startTime;

    private String endTime;

    private String room;

    private int capacity;

    private String semester;

    private List<Enrollment> enrollments;

    public LessonGroup(String groupId, Course course, LessonType type, Teacher teacher,
                       DayOfWeek dayOfWeek, String startTime, String endTime,
                       String room, int capacity, String semester) {
        this.groupId = groupId;
        this.course = course;
        this.type = type;
        this.teacher = teacher;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.capacity = capacity;
        this.semester = semester;
        this.enrollments = new ArrayList<>();
    }

    public List<Student> getEnrolledStudents() {
        return enrollments.stream().map(Enrollment::getStudent).collect(Collectors.toList());
    }

    public int getCurrentCapacity() {
        return enrollments.size();
    }

    public String getScheduleInfo() {
        return dayOfWeek + " " + startTime + "-" + endTime + " in " + room;
    }

    public boolean isFull() {
        return enrollments.size() >= capacity;
    }

    public String getGroupId() { return groupId; }

    public void setGroupId(String groupId) { this.groupId = groupId; }

    public Course getCourse() { return course; }

    public void setCourse(Course course) { this.course = course; }

    public LessonType getType() { return type; }

    public void setType(LessonType type) { this.type = type; }

    public Teacher getTeacher() { return teacher; }

    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    public DayOfWeek getDayOfWeek() { return dayOfWeek; }

    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public String getStartTime() { return startTime; }

    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }

    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getRoom() { return room; }

    public void setRoom(String room) { this.room = room; }

    public int getCapacity() { return capacity; }

    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getSemester() { return semester; }

    public void setSemester(String semester) { this.semester = semester; }

    public List<Enrollment> getEnrollments() { return enrollments; }

    public void setEnrollments(List<Enrollment> enrollments) { this.enrollments = enrollments; }
}
