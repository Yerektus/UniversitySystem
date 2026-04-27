package storage;
import model.academic.Course;
import model.research.Journal;
import model.users.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class DataStorage {

    private static DataStorage instance;
    private Map<String, User> users;
    private List<Course> courses;
    private List<Journal> journals;

    private DataStorage() {
        users = new HashMap<>();
        courses = new ArrayList<>();
        journals = new ArrayList<>();
    }

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    public void save(Object object) {
        if (object instanceof User) {
            User user = (User) object;
            users.put(user.getId(), user);
            System.out.println("Saved user: " + user.getFirstName());
        }
        else if (object instanceof Course) {
            courses.add((Course) object);
            System.out.println("Saved course: " + ((Course) object).getName());
        }
        else if (object instanceof Journal) {
            journals.add((Journal) object);
            System.out.println("Saved journal: " + ((Journal) object).getName());
        }
        else {
            System.out.println("Saved object: " + object.toString());
        }
    }

    public void saveAll(String filename) {
        System.out.println("Saving all data to " + filename + "...");
    }

    public void deserializeAll() {
        System.out.println("Deserializing all data...");
    }

    public Map<String, User> getUsers() { return users; }

    public List<Course> getCourses() { return courses; }

    public List<Journal> getJournals() { return journals; }
}
