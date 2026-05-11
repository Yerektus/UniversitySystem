package model.users;

import model.academic.Course;
import model.academic.Mark;
import model.communication.Complaint;
import model.enums.CitationFormat;
import model.enums.Language;
import model.enums.TeacherPosition;
import model.enums.UrgencyLevel;
import model.research.ResearchPaper;
import model.research.Researcher;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Teacher extends Employee implements Researcher {

	private static final long serialVersionUID = 1L;
	private TeacherPosition position;
    private List<ResearchPaper> papers;

    public Teacher(String id, String password, String firstName, String lastName,
                   String email, Language language, String department, TeacherPosition position) {
        super(id, password, firstName, lastName, email, language, department);
        this.position = position;
        this.papers = new ArrayList<>();
    }

    public void viewCourses() {
        System.out.println("Viewing courses for " + getFirstName() + "...");
    }

    public void manageCourse(Course course) {
        System.out.println(getFirstName() + " managing course: " + course.getName());
    }

    public void viewStudents() {
        System.out.println("Viewing students...");
    }

    public void getStudent(Student student, Mark mark) {
        System.out.println("Student: " + student.getFirstName() + ", Mark: " + mark.getTotalMark());
    }

    public void sendComplaint(Student student, UrgencyLevel level) {
        Complaint complaint = new Complaint(
            java.util.UUID.randomUUID().toString(),
            getFirstName(),
            student.getFirstName(),
            "Complaint about student behavior",
            level,
            new Date()
        );
        System.out.println("Complaint sent against " + student.getFirstName() + " [" + level + "]");
    }

    public void generateMarkReport(Course course) {
        System.out.println("Generating mark report for course: " + course.getName());
    }

    @Override
    public List<ResearchPaper> getPapers() {
        return papers;
    }

    @Override
    public int calculateHindex() {
        List<Integer> citations = new ArrayList<>();
        for (ResearchPaper p : papers) citations.add(p.getCitations());
        citations.sort((a, b) -> b - a);
        int h = 0;
        for (int i = 0; i < citations.size(); i++) {
            if (citations.get(i) >= i + 1) h = i + 1;
            else break;
        }
        return h;
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> c) {
        List<ResearchPaper> sorted = new ArrayList<>(papers);
        sorted.sort(c);
        for (ResearchPaper p : sorted) System.out.println(p.getCitation(CitationFormat.PLAIN_TEXT));
    }

    @Override
    public void publishPaper(ResearchPaper paper) {
        papers.add(paper);
        System.out.println(getFirstName() + " published: " + paper.getTitle());
    }

    public TeacherPosition getPosition() { return position; }

    public void setPosition(TeacherPosition position) { this.position = position; }

    public void setPapers(List<ResearchPaper> papers) { this.papers = papers; }
}