package storage;

import model.academic.Course;
import model.academic.Enrollment;
import model.academic.LessonGroup;
import model.academic.Mark;
import model.academic.StudentOrganization;
import model.communication.News;
import model.communication.Request;
import model.enums.*;
import model.research.Journal;
import model.research.ResearchPaper;
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
    private List<Mark> marks;

    private DataStorage() {
        users = new HashMap<>();
        courses = new ArrayList<>();
        journals = new ArrayList<>();
        lessonGroups = new ArrayList<>();
        organizations = new ArrayList<>();
        newsList = new ArrayList<>();
        requests = new ArrayList<>();
        marks = new ArrayList<>();
        seedData();
    }

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    private void seedData() {
        // --- Users ---
        Admin admin = new Admin(
                "admin001", "admin123", "Aibek", "Seitkali",
                "admin@university.kz", Language.EN, "IT Department"
        );

        Student student = new Student(
                "student001", "student123", "Daniyar", "Bekov",
                "student@university.kz", Language.KZ, "Computer Science", 2
        );
        student.setGpa(3.5);
        student.setTotalCredits(10);

        Student student2 = new Student(
                "student002", "pass123", "Aliya", "Ospanova",
                "aliya@university.kz", Language.KZ, "Information Systems", 1
        );
        student2.setGpa(3.8);
        student2.setTotalCredits(5);

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

        Teacher teacher2 = new Teacher(
                "teacher002", "pass123", "Bekzat", "Akhanov",
                "bekzat@university.kz", Language.KZ, "Math Department",
                TeacherPosition.LECTOR
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
        users.put(student2.getId(), student2);
        users.put(gradStudent.getId(), gradStudent);
        users.put(teacher.getId(), teacher);
        users.put(teacher2.getId(), teacher2);
        users.put(manager.getId(), manager);
        users.put(techSupport.getId(), techSupport);

        // --- Courses ---
        Course cs101 = new Course("CS101", "Introduction to Programming", 5, CourseType.MAJOR);
        cs101.setTargetMajor("Computer Science");
        cs101.setTargetYear(1);
        cs101.addLecturer(teacher);
        cs101.getEnrolledStudents().add(student);
        cs101.getEnrolledStudents().add(student2);

        Course math101 = new Course("MATH101", "Calculus I", 4, CourseType.MAJOR);
        math101.setTargetMajor("Computer Science");
        math101.setTargetYear(1);
        math101.addLecturer(teacher2);
        math101.getEnrolledStudents().add(student);

        Course cs201 = new Course("CS201", "Data Structures", 5, CourseType.MAJOR);
        cs201.setTargetMajor("Computer Science");
        cs201.setTargetYear(2);
        cs201.addLecturer(teacher);
        cs201.getEnrolledStudents().add(student);

        Course elective = new Course("EL101", "Introduction to Philosophy", 3, CourseType.FREE_ELECTIVE);
        elective.setTargetMajor("Any");
        elective.setTargetYear(1);

        courses.add(cs101);
        courses.add(math101);
        courses.add(cs201);
        courses.add(elective);

        // --- Lesson Groups ---
        LessonGroup g1 = new LessonGroup("G001", cs101, LessonType.LECTURE, teacher,
                DayOfWeek.MONDAY, "09:00", "10:50", "Room 101", 30, "Fall 2025");
        LessonGroup g2 = new LessonGroup("G002", cs101, LessonType.PRACTICE, teacher,
                DayOfWeek.WEDNESDAY, "11:00", "12:50", "Lab 202", 20, "Fall 2025");
        LessonGroup g3 = new LessonGroup("G003", math101, LessonType.LECTURE, teacher2,
                DayOfWeek.TUESDAY, "14:00", "15:50", "Room 301", 35, "Fall 2025");
        LessonGroup g4 = new LessonGroup("G004", cs201, LessonType.LECTURE, teacher,
                DayOfWeek.THURSDAY, "09:00", "10:50", "Room 102", 25, "Fall 2025");

        lessonGroups.add(g1);
        lessonGroups.add(g2);
        lessonGroups.add(g3);
        lessonGroups.add(g4);

        // --- Enrollments for student ---
        Enrollment e1 = new Enrollment("E001", student, cs101, g1);
        Enrollment e2 = new Enrollment("E002", student, math101, g3);
        Enrollment e3 = new Enrollment("E003", student, cs201, g4);
        student.getEnrollments().add(e1);
        student.getEnrollments().add(e2);
        student.getEnrollments().add(e3);

        Enrollment e4 = new Enrollment("E004", student2, cs101, g2);
        student2.getEnrollments().add(e4);

        // --- Marks for student ---
        Mark m1 = new Mark(student, cs101, g1);
        m1.setFirstAttestation(27.5);
        m1.setSecondAttestation(26.0);
        m1.setFinalExam(35.0);
        marks.add(m1);

        Mark m2 = new Mark(student, math101, g3);
        m2.setFirstAttestation(22.0);
        m2.setSecondAttestation(24.5);
        m2.setFinalExam(30.0);
        marks.add(m2);

        Mark m3 = new Mark(student, cs201, g4);
        m3.setFirstAttestation(28.0);
        m3.setSecondAttestation(25.0);
        m3.setFinalExam(0.0);
        marks.add(m3);

        // --- Student Organizations ---
        StudentOrganization acm = new StudentOrganization("ORG001", "ACM Student Chapter",
                "Promote computer science and technology");
        acm.addMember(student);
        acm.addMember(student2);

        StudentOrganization robotics = new StudentOrganization("ORG002", "Robotics Club",
                "Build and program robots");
        robotics.addMember(student);

        StudentOrganization debate = new StudentOrganization("ORG003", "Debate Club",
                "Develop critical thinking and public speaking");

        organizations.add(acm);
        organizations.add(robotics);
        organizations.add(debate);

        // --- Research Papers for teacher ---
        ResearchPaper paper1 = new ResearchPaper(
                "Deep Learning Approaches in NLP", "IEEE Transactions on Neural Networks", 12, "10.1109/tnn.2024.001");
        paper1.setCitations(45);
        paper1.getAuthors().add(teacher);

        ResearchPaper paper2 = new ResearchPaper(
                "Efficient Algorithms for Graph Traversal", "ACM Computing Surveys", 18, "10.1145/acm.2024.002");
        paper2.setCitations(30);
        paper2.getAuthors().add(teacher);

        teacher.getPapers().add(paper1);
        teacher.getPapers().add(paper2);

        // --- Journals ---
        Journal journal1 = new Journal("J001", "IEEE Transactions on Neural Networks");
        journal1.getPapers().add(paper1);
        journal1.subscribe(student);

        Journal journal2 = new Journal("J002", "ACM Computing Surveys");
        journal2.getPapers().add(paper2);

        journals.add(journal1);
        journals.add(journal2);

        // --- News ---
        News news1 = new News("N001", "Midterm Exam Schedule Released",
                "Midterm exams will be held from October 14-18. Check the portal for your schedule.",
                "Academic", manager);
        News news2 = new News("N002", "University Open Day",
                "Join us on November 5th for our annual Open Day. All students are welcome.",
                "Events", admin);
        News news3 = new News("N003", "New Research Lab Opened",
                "The AI & Data Science Research Lab is now open on the 3rd floor of the main building.",
                "Research", manager);

        newsList.add(news1);
        newsList.add(news2);
        newsList.add(news3);

        // --- Requests ---
        Request req1 = new Request("REQ001", "Transcript Request",
                student, "Please provide an official transcript for scholarship application.");
        Request req2 = new Request("REQ002", "Grade Appeal",
                student2, "I would like to appeal my Calculus I final exam grade.");
        Request req3 = new Request("REQ003", "Course Addition Request",
                student, "Requesting to add CS301 after the registration deadline.");
        req3.updateStatus(RequestStatus.VIEWED);

        requests.add(req1);
        requests.add(req2);
        requests.add(req3);
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

    public void saveMark(Mark mark) {
        marks.add(mark);
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

    public List<Mark> getMarksForStudent(Student student) {
        List<Mark> result = new ArrayList<>();
        for (Mark m : marks) {
            if (m.getStudent().getId().equals(student.getId())) {
                result.add(m);
            }
        }
        return result;
    }

    public Map<String, User> getUsers() { return users; }
    
    public List<Course> getCourses() { return courses; }
    
    public List<Journal> getJournals() { return journals; }
    
    public List<LessonGroup> getLessonGroups() { return lessonGroups; }
    
    public List<StudentOrganization> getOrganizations() { return organizations; }
    
    public List<News> getNewsList() { return newsList; }
    
    public List<Request> getRequests() { return requests; }
    
    public List<Mark> getMarks() { return marks; }
}
