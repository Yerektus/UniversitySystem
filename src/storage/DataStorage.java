package storage;

import model.academic.Course;
import model.academic.LessonGroup;
import model.academic.StudentOrganization;
import model.communication.News;
import model.communication.Request;
import model.enums.*;
import model.research.Journal;
import model.users.*;

import java.util.*;

public class DataStorage {

    private static DataStorage instance;

    private Map<String, User> users;
    private List<Course> courses;
    private List<Journal> journals;
    private List<LessonGroup> lessonGroups;
    private List<StudentOrganization> organizations;
    private List<News> newsList;
    private List<Request> requests;

    private DataStorage() {
        users = new HashMap<>();
        courses = new ArrayList<>();
        journals = new ArrayList<>();
        lessonGroups = new ArrayList<>();
        organizations = new ArrayList<>();
        newsList = new ArrayList<>();
        requests = new ArrayList<>();
        seedUsers();
    }

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    private void seedUsers() {
        Admin admin = new Admin(
                "admin001", "admin123", "Aibek", "Seitkali",
                "admin@university.kz", Language.EN, "IT Department"
        );

        Student student = new Student(
                "student001", "student123", "Daniyar", "Bekov",
                "student@university.kz", Language.KZ, "Computer Science", 2
        );

        GraduateStudent gradStudent = new GraduateStudent(
                "grad001", "grad123", "Madina", "Nurova",
                "grad@university.kz", Language.RU, "Data Science", 1,
                GraduateType.MASTER, "AI Research"
        );

        Teacher teacher = new Teacher(
                "teacher001", "teacher123", "Olga", "Ivanova",
                "teacher@university.kz", Language.RU, "CS Department",
                TeacherPosition.PROFESSOR
        );

        Manager manager = new Manager(
                "manager001", "manager123", "Nursultan", "Akhmetov",
                "manager@university.kz", Language.KZ, "Registration Office",
                ManagerType.OR
        );

        TechSupportSpecialist techSupport = new TechSupportSpecialist(
                "tech001", "tech123", "Arman", "Zhukov",
                "tech@university.kz", Language.EN, "Support Department"
        );

        users.put(admin.getId(), admin);
        users.put(student.getId(), student);
        users.put(gradStudent.getId(), gradStudent);
        users.put(teacher.getId(), teacher);
        users.put(manager.getId(), manager);
        users.put(techSupport.getId(), techSupport);
    }

    public void save(Object object) {
        if (object instanceof User) {
            User user = (User) object;
            users.put(user.getId(), user);
            System.out.println("Saved: " + user.getFirstName() + " " + user.getLastName());
        } else if (object instanceof Course) {
            courses.add((Course) object);
            System.out.println("Saved course: " + ((Course) object).getName());
        } else if (object instanceof Journal) {
            journals.add((Journal) object);
            System.out.println("Saved journal: " + ((Journal) object).getName());
        }
    }

    public void saveLessonGroup(LessonGroup group) {
        lessonGroups.add(group);
    }

    public void saveOrganization(StudentOrganization org) {
        organizations.add(org);
    }

    public void saveNews(News news) {
        newsList.add(news);
    }

    public void saveRequest(Request request) {
        requests.add(request);
    }

    public void removeUser(String userId) {
        users.remove(userId);
        System.out.println("User removed: " + userId);
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
    public List<LessonGroup> getLessonGroups() { return lessonGroups; }
    public List<StudentOrganization> getOrganizations() { return organizations; }
    public List<News> getNewsList() { return newsList; }
    public List<Request> getRequests() { return requests; }
}
