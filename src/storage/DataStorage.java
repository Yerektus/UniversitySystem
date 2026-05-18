package storage;

import model.academic.Course;
import model.academic.Enrollment;
import model.academic.LessonGroup;
import model.academic.Mark;
import model.academic.StudentOrganization;
import model.communication.Complaint;
import model.communication.Message;
import model.communication.News;
import model.communication.Request;
import model.enums.*;
import model.research.Journal;
import model.research.ResearchPaper;
import model.research.ResearchProject;
import model.research.Researcher;
import model.users.*;

import java.io.*;
import java.util.*;

public class DataStorage {

    private static DataStorage instance;
    private static final String DATA_PATH = new File("src/data").getAbsolutePath();

    private Map<String, User> users;
    private List<Course> courses;
    private List<Journal> journals;
    private List<LessonGroup> lessonGroups;
    private List<StudentOrganization> organizations;
    private List<News> newsList;
    private List<Request> requests;
    private List<Mark> marks;
    private List<ResearchProject> researchProjects;
    private List<Message> messages;
    private List<Complaint> complaints;

    private DataStorage() {
        users = new HashMap<>();
        courses = new ArrayList<>();
        journals = new ArrayList<>();
        lessonGroups = new ArrayList<>();
        organizations = new ArrayList<>();
        newsList = new ArrayList<>();
        requests = new ArrayList<>();
        marks = new ArrayList<>();
        researchProjects = new ArrayList<>();
        messages = new ArrayList<>();
        complaints = new ArrayList<>();

        new File(DATA_PATH).mkdirs();

        if (!load()) {
            seedData();
            saveAll();
        }
    }

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    public static void serialize(Object obj, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_PATH + "/" + fileName + ".txt"))) {
            oos.writeObject(obj);
        } catch (Exception e) {
            System.err.println("Save error [" + fileName + "]: " + e.getMessage());
        }
    }

    public static Object deserialize(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DATA_PATH + "/" + fileName + ".txt"))) {
            return ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public boolean load() {
        Object u  = deserialize("users");
        Object c  = deserialize("courses");
        Object j  = deserialize("journals");
        Object lg = deserialize("lessonGroups");
        Object o  = deserialize("organizations");
        Object n  = deserialize("news");
        Object r  = deserialize("requests");
        Object m  = deserialize("marks");
        Object rp = deserialize("researchProjects");
        Object ms = deserialize("messages");
        Object co = deserialize("complaints");

        if (u == null) return false;

        users         = (Map<String, User>)          u;
        courses       = (List<Course>)               c;
        journals      = (List<Journal>)              j;
        lessonGroups  = (List<LessonGroup>)          lg;
        organizations = (List<StudentOrganization>)  o;
        newsList      = (List<News>)                 n;
        requests      = (List<Request>)              r;
        marks         = (List<Mark>)                 m;
        researchProjects = rp != null ? (List<ResearchProject>) rp : new ArrayList<>();
        messages         = ms != null ? (List<Message>)         ms : new ArrayList<>();
        complaints       = co != null ? (List<Complaint>)       co : new ArrayList<>();
        return true;
    }

    public void saveAll() {
        serialize(users,         "users");
        serialize(courses,       "courses");
        serialize(journals,      "journals");
        serialize(lessonGroups,  "lessonGroups");
        serialize(organizations, "organizations");
        serialize(newsList,         "news");
        serialize(requests,         "requests");
        serialize(marks,            "marks");
        serialize(researchProjects, "researchProjects");
        serialize(messages,         "messages");
        serialize(complaints,       "complaints");
    }

    public void save(Object object) {
        if (object instanceof User) {
            users.put(((User) object).getId(), (User) object);
        } else if (object instanceof Course) {
            courses.add((Course) object);
        } else if (object instanceof Journal) {
            journals.add((Journal) object);
        }
        saveAll();
    }

    public void saveLessonGroup(LessonGroup group)       { lessonGroups.add(group);   saveAll(); }
    public void saveOrganization(StudentOrganization org){ organizations.add(org);    saveAll(); }
    public void saveNews(News news)                      { newsList.add(news);        saveAll(); }
    public void saveRequest(Request request)             { requests.add(request);     saveAll(); }
    public void saveMark(Mark mark)                      { marks.add(mark);           saveAll(); }
    public void saveResearchProject(ResearchProject p)   { researchProjects.add(p);   saveAll(); }
    public void saveMessage(Message message)             { messages.add(message);     saveAll(); }
    public void saveComplaint(Complaint complaint)       { complaints.add(complaint); saveAll(); }

    public void publishPaperToJournal(ResearchPaper paper, Journal journal) {
        News announcement = journal.publishPaper(paper);
        if (announcement != null) {
            announcement.setPinned(true);
            newsList.add(announcement);
        }
        updateTopCitedResearcherNews();
        saveAll();
    }

    private void updateTopCitedResearcherNews() {
        Researcher topResearcher = null;
        int maxCitations = 0;
        for (User u : users.values()) {
            if (u instanceof Researcher) {
                Researcher r = (Researcher) u;
                int total = r.getPapers().stream().mapToInt(ResearchPaper::getCitations).sum();
                if (total > maxCitations) { maxCitations = total; topResearcher = r; }
            }
        }
        if (topResearcher == null) return;
        String name = topResearcher instanceof User
                ? ((User) topResearcher).getFirstName() + " " + ((User) topResearcher).getLastName()
                : "Unknown";

        newsList.removeIf(n -> n.getTitle().startsWith("Top Cited Researcher"));

        String newsId = "N_TOP" + System.currentTimeMillis();
        News topNews = new News(newsId,
                "Top Cited Researcher: " + name,
                name + " is the top cited researcher with " + maxCitations + " total citations.",
                "Research", null);
        topNews.setPinned(true);
        newsList.add(topNews);
    }

    public void removeUser(String userId) {
        users.remove(userId);
        saveAll();
    }

    public void updateAndSave() { saveAll(); }

    public List<Mark> getMarksForStudent(Student student) {
        List<Mark> result = new ArrayList<>();
        for (Mark m : marks)
            if (m.getStudent().getId().equals(student.getId())) result.add(m);
        return result;
    }

    public Map<String, User>           getUsers()         { return users; }
    public List<Course>                getCourses()       { return courses; }
    public List<Journal>               getJournals()      { return journals; }
    public List<LessonGroup>           getLessonGroups()  { return lessonGroups; }
    public List<StudentOrganization>   getOrganizations() { return organizations; }
    public List<News>                  getNewsList()      { return newsList; }
    public List<Request>               getRequests()      { return requests; }
    public List<Mark>                  getMarks()         { return marks; }
    public List<ResearchProject>       getResearchProjects() { return researchProjects; }
    public List<Message>               getMessages()         { return messages; }
    public List<Complaint>             getComplaints()       { return complaints; }

    public List<Message> getMessagesForUser(User user) {
        List<Message> result = new ArrayList<>();
        for (Message m : messages)
            if (m.getReceiver().getId().equals(user.getId())) result.add(m);
        return result;
    }

    private void seedData() {
        Admin admin = new Admin(
                "AD0001", "admin123", "Aibek", "Seitkali",
                "a_seitkali@kbtu.kz", Language.EN, "IT Department");

        Student student = new Student(
                "23B030001", "student123", "Daniyar", "Bekov",
                "d_bekov@kbtu.kz", Language.KZ, "Computer Science", 2);
        student.setGpa(3.5);
        student.setTotalCredits(10);

        Student student2 = new Student(
                "23B030002", "pass123", "Aliya", "Ospanova",
                "a_ospanova@kbtu.kz", Language.KZ, "Information Systems", 1);
        student2.setGpa(3.8);
        student2.setTotalCredits(5);

        GraduateStudent gradStudent = new GraduateStudent(
                "23M030001", "grad123", "Madina", "Nurova",
                "m_nurova@kbtu.kz", Language.RU, "Data Science", 1,
                GraduateType.MASTER, "AI Research");

        Teacher teacher = new Teacher(
                "T030001", "teacher123", "Olga", "Ivanova",
                "o_ivanova@kbtu.kz", Language.RU, "CS Department",
                TeacherPosition.PROFESSOR);

        Teacher teacher2 = new Teacher(
                "T040001", "pass123", "Bekzat", "Akhanov",
                "b_akhanov@kbtu.kz", Language.KZ, "Math Department",
                TeacherPosition.LECTOR);

        Manager manager = new Manager(
                "MG030001", "manager123", "Nursultan", "Akhmetov",
                "n_akhmetov@kbtu.kz", Language.KZ, "Registration Office",
                ManagerType.OR);

        TechSupportSpecialist techSupport = new TechSupportSpecialist(
                "TS0001", "tech123", "Arman", "Zhukov",
                "a_zhukov@kbtu.kz", Language.EN, "Support Department");

        users.put(admin.getId(), admin);
        users.put(student.getId(), student);
        users.put(student2.getId(), student2);
        users.put(gradStudent.getId(), gradStudent);
        users.put(teacher.getId(), teacher);
        users.put(teacher2.getId(), teacher2);
        users.put(manager.getId(), manager);
        users.put(techSupport.getId(), techSupport);

        Course cs101 = new Course("CS101", "Introduction to Programming", 5, CourseType.MAJOR);
        cs101.setTargetMajor("Computer Science"); cs101.setTargetYear(1);
        cs101.addLecturer(teacher);
        cs101.getEnrolledStudents().add(student); cs101.getEnrolledStudents().add(student2);

        Course math101 = new Course("MATH101", "Calculus I", 4, CourseType.MAJOR);
        math101.setTargetMajor("Computer Science"); math101.setTargetYear(1);
        math101.addLecturer(teacher2); math101.getEnrolledStudents().add(student);

        Course cs201 = new Course("CS201", "Data Structures", 5, CourseType.MAJOR);
        cs201.setTargetMajor("Computer Science"); cs201.setTargetYear(2);
        cs201.addLecturer(teacher); cs201.getEnrolledStudents().add(student);

        Course elective = new Course("EL101", "Introduction to Philosophy", 3, CourseType.FREE_ELECTIVE);
        elective.setTargetMajor("Any"); elective.setTargetYear(1);

        courses.add(cs101); courses.add(math101); courses.add(cs201); courses.add(elective);

        LessonGroup g1 = new LessonGroup("G001", cs101, LessonType.LECTURE, teacher,
                DayOfWeek.MONDAY, "09:00", "10:50", "Room 101", 30, "Fall 2025");
        LessonGroup g2 = new LessonGroup("G002", cs101, LessonType.PRACTICE, teacher,
                DayOfWeek.WEDNESDAY, "11:00", "12:50", "Lab 202", 20, "Fall 2025");
        LessonGroup g3 = new LessonGroup("G003", math101, LessonType.LECTURE, teacher2,
                DayOfWeek.TUESDAY, "14:00", "15:50", "Room 301", 35, "Fall 2025");
        LessonGroup g4 = new LessonGroup("G004", cs201, LessonType.LECTURE, teacher,
                DayOfWeek.THURSDAY, "09:00", "10:50", "Room 102", 25, "Fall 2025");

        lessonGroups.add(g1); lessonGroups.add(g2); lessonGroups.add(g3); lessonGroups.add(g4);

        Enrollment e1 = new Enrollment("E001", student, cs101, g1);
        Enrollment e2 = new Enrollment("E002", student, math101, g3);
        Enrollment e3 = new Enrollment("E003", student, cs201, g4);
        student.getEnrollments().add(e1); student.getEnrollments().add(e2); student.getEnrollments().add(e3);

        Enrollment e4 = new Enrollment("E004", student2, cs101, g2);
        student2.getEnrollments().add(e4);

        Mark m1 = new Mark(student, cs101, g1);
        m1.setFirstAttestation(27.5); m1.setSecondAttestation(26.0); m1.setFinalExam(35.0);
        Mark m2 = new Mark(student, math101, g3);
        m2.setFirstAttestation(22.0); m2.setSecondAttestation(24.5); m2.setFinalExam(30.0);
        Mark m3 = new Mark(student, cs201, g4);
        m3.setFirstAttestation(28.0); m3.setSecondAttestation(25.0); m3.setFinalExam(0.0);
        marks.add(m1); marks.add(m2); marks.add(m3);

        StudentOrganization acm = new StudentOrganization("ORG001", "ACM Student Chapter",
                "Promote computer science and technology");
        acm.addMember(student); acm.addMember(student2);
        StudentOrganization robotics = new StudentOrganization("ORG002", "Robotics Club",
                "Build and program robots");
        robotics.addMember(student);
        StudentOrganization debate = new StudentOrganization("ORG003", "Debate Club",
                "Develop critical thinking and public speaking");
        organizations.add(acm); organizations.add(robotics); organizations.add(debate);

        ResearchPaper paper1 = new ResearchPaper(
                "Deep Learning Approaches in NLP", "IEEE Transactions on Neural Networks", 12, "10.1109/tnn.2024.001");
        paper1.setCitations(45); paper1.getAuthors().add(teacher);
        ResearchPaper paper2 = new ResearchPaper(
                "Efficient Algorithms for Graph Traversal", "ACM Computing Surveys", 18, "10.1145/acm.2024.002");
        paper2.setCitations(30); paper2.getAuthors().add(teacher);
        teacher.getPapers().add(paper1); teacher.getPapers().add(paper2);

        Journal journal1 = new Journal("J001", "IEEE Transactions on Neural Networks");
        journal1.getPapers().add(paper1); journal1.subscribe(student);
        Journal journal2 = new Journal("J002", "ACM Computing Surveys");
        journal2.getPapers().add(paper2);
        journals.add(journal1); journals.add(journal2);

        News news1 = new News("N001", "Midterm Exam Schedule Released",
                "Midterm exams will be held from October 14-18.", "Academic", manager);
        News news2 = new News("N002", "University Open Day",
                "Join us on November 5th for our annual Open Day.", "Events", admin);
        News news3 = new News("N003", "New Research Lab Opened",
                "The AI & Data Science Research Lab is now open.", "Research", manager);
        news3.setPinned(true);
        newsList.add(news1); newsList.add(news2); newsList.add(news3);

        Request req1 = new Request("REQ001", "Transcript Request",
                student, "Please provide an official transcript for scholarship application.");
        Request req2 = new Request("REQ002", "Grade Appeal",
                student2, "I would like to appeal my Calculus I final exam grade.");
        Request req3 = new Request("REQ003", "Course Addition Request",
                student, "Requesting to add CS301 after the registration deadline.");
        req3.updateStatus(RequestStatus.VIEWED);
        requests.add(req1); requests.add(req2); requests.add(req3);
    }
}