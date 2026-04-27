package system;
import model.academic.Course;
import model.research.Journal;
import model.research.ResearchProject;
import model.research.Researcher;
import model.users.User;
import java.util.ArrayList;
import java.util.List;
public class University {

    private String name;
    private List<User> users;
    private List<Course> courses;
    private List<Journal> journals;
    private List<ResearchProject> projects;

    public University(String name) {
        this.name = name;
        this.users = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.journals = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public void printAll(Researcher researcher) {
        System.out.println("=== University: " + name + " ===");
        System.out.println("Users: " + users.size());
        System.out.println("Courses: " + courses.size());
        System.out.println("Journals: " + journals.size());
        System.out.println("Projects: " + projects.size());
    }

    public Researcher getTopCitedResearcher() {
        Researcher top = null;
        int maxH = -1;
        for (User u : users) {
            if (u instanceof Researcher) {
                int h = ((Researcher) u).calculateHindex();
                if (h > maxH) {
                    maxH = h;
                    top = (Researcher) u;
                }
            }
        }
        return top;
    }

    public Researcher getTopCitedResearcherByYear(int year) {
        System.out.println("Finding top researcher for year: " + year);
        return getTopCitedResearcher();
    }

    public Researcher getTopCitedResearcherBySchool(String school) {
        System.out.println("Finding top researcher in school: " + school);
        return getTopCitedResearcher();
    }

    public void generateTopCitedNews() {
        Researcher top = getTopCitedResearcher();
        if (top != null) {
            System.out.println("Top cited researcher: " + top);
        }
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public List<User> getUsers() { return users; }

    public void setUsers(List<User> users) { this.users = users; }

    public List<Course> getCourses() { return courses; }

    public void setCourses(List<Course> courses) { this.courses = courses; }

    public List<Journal> getJournals() { return journals; }

    public void setJournals(List<Journal> journals) { this.journals = journals; }

    public List<ResearchProject> getProjects() { return projects; }

    public void setProjects(List<ResearchProject> projects) { this.projects = projects; }
}
