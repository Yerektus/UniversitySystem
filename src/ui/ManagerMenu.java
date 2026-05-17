package ui;

import model.academic.Course;
import model.academic.Mark;
import model.communication.Complaint;
import model.communication.News;
import model.enums.*;
import model.research.ResearchPaper;
import model.users.*;
import system.University;
import storage.DataStorage;

import java.util.*;

public class ManagerMenu extends BaseMenu {

    private final Manager manager;

    public ManagerMenu(Scanner scanner, DataStorage storage, Manager manager) {
        super(scanner, storage, manager);
        this.manager = manager;
    }

    @Override
    protected void printMenu() {
        System.out.println("\n--- Manager Menu [" + manager.getFirstName() + " " + manager.getLastName() + "] ---");
        System.out.println("1.  View profile");
        System.out.println("2.  Create new user account");
        System.out.println("3.  Assign course to teacher");
        System.out.println("4.  Approve student course registration");
        System.out.println("5.  Add course for registration");
        System.out.println("6.  Manage news");
        System.out.println("7.  View students");
        System.out.println("8.  View teachers");
        System.out.println("9.  Academic performance report");
        System.out.println("10. Print all researchers' papers");
        System.out.println("11. Top cited researcher (university)");
        System.out.println("12. Top cited researcher by year");
        System.out.println("13. Top cited researcher by school");
        System.out.println("14. View complaints");
        System.out.println("0.  Logout");
    }

    @Override
    protected void handleChoice(String choice) {
        switch (choice) {
            case "1":  viewProfile();            break;
            case "2":  createUserAccount();      break;
            case "3":  assignCourseToTeacher();  break;
            case "4":  approveRegistration();    break;
            case "5":  addCourseForRegistration(); break;
            case "6":  manageNews();             break;
            case "7":  viewStudents();           break;
            case "8":  viewTeachers();           break;
            case "9":  academicReport();              break;
            case "10": printAllResearchPapers();      break;
            case "11": topCitedUniversity();          break;
            case "12": topCitedByYear();              break;
            case "13": topCitedBySchool();            break;
            case "14": viewComplaints();               break;
            case "0":  logout();                      break;
            default:   System.out.println("Invalid choice.");
        }
    }

    private void viewProfile() {
        System.out.println("\n--- Profile ---");
        System.out.printf("%-10s %s %s%n", "Name:",     manager.getFirstName(), manager.getLastName());
        System.out.printf("%-10s %s%n",    "Email:",    manager.getEmail());
        System.out.printf("%-10s %s%n",    "Dept:",     manager.getDepartment());
        System.out.printf("%-10s %s%n",    "Type:",     manager.getType());
        System.out.printf("%-10s %s%n",    "Language:", manager.getLanguage());
    }

    private void createUserAccount() {
        System.out.println("\n--- Create User Account ---");
        System.out.println("1. Student");
        System.out.println("2. Graduate Student");
        System.out.println("3. Teacher");
        System.out.println("4. Tech Support Specialist");
        System.out.print("Role: ");
        String role = scanner.nextLine().trim();

        System.out.print("First name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Last name: ");
        String lastName = scanner.nextLine().trim();
        System.out.println("Language: 1. KZ  2. EN  3. RU");
        System.out.print("Choice: ");
        Language language = parseLanguage(scanner.nextLine().trim());
        System.out.print("Department/Faculty: ");
        String department = scanner.nextLine().trim();

        String id;
        String email    = util.UserCredentialGenerator.generateEmail(firstName, lastName);
        String password = util.UserCredentialGenerator.generatePassword();
        User user       = null;

        switch (role) {
            case "1": {
                SchoolCode school = selectSchool();
                System.out.print("Major: ");
                String major = scanner.nextLine().trim();
                System.out.print("Year (1-4): ");
                int year = parseIntSafe(scanner.nextLine().trim(), 1);
                id = util.UserCredentialGenerator.generateStudentId("B", school);
                user = new Student(id, password, firstName, lastName, email, language, major, year);
                break;
            }
            case "2": {
                SchoolCode school = selectSchool();
                System.out.print("Major: ");
                String major = scanner.nextLine().trim();
                System.out.print("Year (1-2): ");
                int year = parseIntSafe(scanner.nextLine().trim(), 1);
                System.out.println("Type: 1. MASTER  2. PHD");
                System.out.print("Choice: ");
                GraduateType gt = scanner.nextLine().trim().equals("2") ? GraduateType.PHD : GraduateType.MASTER;
                String degreeType = gt == GraduateType.PHD ? "P" : "M";
                System.out.print("Research target/topic: ");
                String target = scanner.nextLine().trim();
                id = util.UserCredentialGenerator.generateStudentId(degreeType, school);
                user = new GraduateStudent(id, password, firstName, lastName, email, language, major, year, gt, target);
                break;
            }
            case "3": {
                SchoolCode school = selectSchool();
                System.out.println("Position: 1. TUTOR  2. LECTOR  3. SENIOR  4. PROFESSOR");
                System.out.print("Choice: ");
                TeacherPosition pos;
                switch (scanner.nextLine().trim()) {
                    case "1": pos = TeacherPosition.TUTOR;     break;
                    case "3": pos = TeacherPosition.SENIOR;    break;
                    case "4": pos = TeacherPosition.PROFESSOR; break;
                    default:  pos = TeacherPosition.LECTOR;    break;
                }
                id = util.UserCredentialGenerator.generateTeacherId(school);
                user = new Teacher(id, password, firstName, lastName, email, language, department, pos);
                break;
            }
            case "4":
                id = util.UserCredentialGenerator.generateTechSupportId();
                user = new TechSupportSpecialist(id, password, firstName, lastName, email, language, department);
                break;
            default:
                System.out.println("Invalid role.");
                return;
        }

        storage.save(user);
        System.out.println("Account created.");
        System.out.println("  Email   : " + email);
        System.out.println("  Password: " + password);
    }

    private void assignCourseToTeacher() {
        List<Course> courses = storage.getCourses();
        List<Teacher> teachers = getTeachers();
        if (courses.isEmpty() || teachers.isEmpty()) { System.out.println("No courses or teachers available."); return; }

        System.out.println("\n--- Select Course ---");
        for (int i = 0; i < courses.size(); i++)
            System.out.println((i + 1) + ". " + courses.get(i).getCourseId() + " - " + courses.get(i).getName());
        System.out.print("Choice: ");
        int ci = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (ci < 0 || ci >= courses.size()) { System.out.println("Invalid."); return; }

        System.out.println("\n--- Select Teacher ---");
        for (int i = 0; i < teachers.size(); i++)
            System.out.println((i + 1) + ". " + teachers.get(i).getFirstName()
                    + " " + teachers.get(i).getLastName() + " [" + teachers.get(i).getPosition() + "]");
        System.out.print("Choice: ");
        int ti = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (ti < 0 || ti >= teachers.size()) { System.out.println("Invalid."); return; }

        System.out.println("Assign as: 1. Lecturer  2. Practice instructor");
        System.out.print("Choice: ");
        String type = scanner.nextLine().trim();

        Course course = courses.get(ci);
        Teacher teacher = teachers.get(ti);
        if (type.equals("2")) course.addPracticalInstructor(teacher);
        else course.addLecturer(teacher);

        storage.updateAndSave();
        System.out.println("Assigned " + teacher.getFirstName() + " to " + course.getName());
    }

    private void approveRegistration() {
        List<Student> students = getStudents();
        List<Course> courses = storage.getCourses();
        if (students.isEmpty() || courses.isEmpty()) { System.out.println("No students or courses available."); return; }

        System.out.println("\n--- Select Student ---");
        for (int i = 0; i < students.size(); i++)
            System.out.println((i + 1) + ". " + students.get(i).getFirstName()
                    + " " + students.get(i).getLastName() + " | Credits: " + students.get(i).getTotalCredits());
        System.out.print("Choice: ");
        int si = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (si < 0 || si >= students.size()) { System.out.println("Invalid."); return; }

        System.out.println("\n--- Select Course ---");
        for (int i = 0; i < courses.size(); i++)
            System.out.println((i + 1) + ". " + courses.get(i).getName() + " (" + courses.get(i).getCredits() + " credits)");
        System.out.print("Choice: ");
        int ci = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (ci < 0 || ci >= courses.size()) { System.out.println("Invalid."); return; }

        Student student = students.get(si);
        Course course = courses.get(ci);
        if (!course.getEnrolledStudents().contains(student))
            course.getEnrolledStudents().add(student);
        student.setTotalCredits(student.getTotalCredits() + course.getCredits());
        storage.updateAndSave();
        System.out.println("Approved " + student.getFirstName() + " for " + course.getName());
    }

    private void addCourseForRegistration() {
        System.out.println("\n--- Add Course ---");
        System.out.print("Course ID: ");   String id    = scanner.nextLine().trim();
        System.out.print("Name: ");        String name  = scanner.nextLine().trim();
        System.out.print("Credits: ");     int credits  = parseIntSafe(scanner.nextLine().trim(), 3);
        System.out.println("Type: 1. MAJOR  2. MINOR  3. FREE_ELECTIVE");
        System.out.print("Choice: ");
        CourseType type;
        switch (scanner.nextLine().trim()) {
            case "2": type = CourseType.MINOR;         break;
            case "3": type = CourseType.FREE_ELECTIVE; break;
            default:  type = CourseType.MAJOR;         break;
        }
        System.out.print("Target major (or 'Any'): "); String major = scanner.nextLine().trim();
        System.out.print("Target year (1-4): ");       int year = parseIntSafe(scanner.nextLine().trim(), 1);

        Course course = new Course(id, name, credits, type);
        course.setTargetMajor(major);
        course.setTargetYear(year);
        storage.save(course);
        System.out.println("Course '" + name + "' added.");
    }

    private void manageNews() {
        System.out.println("\n--- News Management ---");
        System.out.println("1. View all news");
        System.out.println("2. Create news");
        System.out.println("3. Delete news");
        System.out.print("Choice: ");
        switch (scanner.nextLine().trim()) {
            case "1": viewNews();   break;
            case "2": createNews(); break;
            case "3": deleteNews(); break;
            default:  System.out.println("Invalid.");
        }
    }

    private void viewNews() {
        List<News> newsList = storage.getNewsList();
        if (newsList.isEmpty()) { System.out.println("No news."); return; }
        System.out.println("\n--- News (pinned first) ---");
        newsList.stream()
                .sorted((a, b) -> Boolean.compare(b.isPinned(), a.isPinned()))
                .forEach(n -> System.out.println((n.isPinned() ? "[PINNED] " : "") + n.getTitle()
                        + " [" + n.getTopic() + "] - " + n.getPublishedAt()));
    }

    private void createNews() {
        System.out.print("Title: ");   String title   = scanner.nextLine().trim();
        System.out.print("Content: "); String content = scanner.nextLine().trim();
        System.out.print("Topic: ");   String topic   = scanner.nextLine().trim();

        News news = new News("N" + System.currentTimeMillis(), title, content, topic, manager);
        if (topic.equalsIgnoreCase("Research")) {
            news.setPinned(true);
            System.out.println("Research news will be pinned.");
        }
        storage.saveNews(news);
        System.out.println("News created: " + title);
    }

    private void deleteNews() {
        List<News> newsList = storage.getNewsList();
        if (newsList.isEmpty()) { System.out.println("No news to delete."); return; }
        for (int i = 0; i < newsList.size(); i++)
            System.out.println((i + 1) + ". " + newsList.get(i).getTitle());
        System.out.print("Choice: ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= newsList.size()) { System.out.println("Invalid."); return; }
        System.out.println("Deleted: " + newsList.remove(idx).getTitle());
        storage.updateAndSave();
    }

    private void viewStudents() {
        System.out.println("Sort by: 1. GPA (desc)  2. Name (A-Z)");
        System.out.print("Choice: ");
        String sort = scanner.nextLine().trim();
        List<Student> students = getStudents();
        if (students.isEmpty()) { System.out.println("No students."); return; }
        if (sort.equals("1")) students.sort((a, b) -> Double.compare(b.getGpa(), a.getGpa()));
        else students.sort(Comparator.comparing(Student::getLastName));

        System.out.println("\n--- Students ---");
        System.out.printf("%-20s %-20s %-15s %-6s %-6s%n", "First", "Last", "Major", "Year", "GPA");
        for (Student s : students)
            System.out.printf("%-20s %-20s %-15s %-6d %-6.2f%n",
                    s.getFirstName(), s.getLastName(), s.getMajor(), s.getYear(), s.getGpa());
    }

    private void viewTeachers() {
        List<Teacher> teachers = getTeachers();
        if (teachers.isEmpty()) { System.out.println("No teachers."); return; }
        teachers.sort(Comparator.comparing(Teacher::getLastName));
        System.out.println("\n--- Teachers ---");
        System.out.printf("%-20s %-20s %-15s %-12s%n", "First", "Last", "Department", "Position");
        for (Teacher t : teachers)
            System.out.printf("%-20s %-20s %-15s %-12s%n",
                    t.getFirstName(), t.getLastName(), t.getDepartment(), t.getPosition());
    }

    private void academicReport() {
        List<Mark> marks = storage.getMarks();
        if (marks.isEmpty()) { System.out.println("No marks recorded yet."); return; }

        double total = 0; int passed = 0; int failed = 0;
        for (Mark m : marks) { total += m.getTotalMark(); if (m.isPassed()) passed++; else failed++; }

        System.out.println("\n--- Academic Performance Report ---");
        System.out.printf("%-25s %d%n",   "Total marks:",    marks.size());
        System.out.printf("%-25s %d%n",   "Passed:",         passed);
        System.out.printf("%-25s %d%n",   "Failed:",         failed);
        System.out.printf("%-25s %.2f%n", "Average:",        total / marks.size());
        System.out.printf("%-25s %.1f%%%n","Pass rate:",      (passed * 100.0 / marks.size()));

        Map<String, List<Mark>> byCourse = new LinkedHashMap<>();
        for (Mark m : marks)
            byCourse.computeIfAbsent(m.getCourse().getName(), k -> new ArrayList<>()).add(m);

        System.out.println("\nPer-course:");
        System.out.printf("%-30s %-8s %-8s %-10s%n", "Course", "Passed", "Failed", "Avg");
        for (Map.Entry<String, List<Mark>> e : byCourse.entrySet()) {
            List<Mark> cm = e.getValue();
            long cp = cm.stream().filter(Mark::isPassed).count();
            double ca = cm.stream().mapToDouble(Mark::getTotalMark).average().orElse(0);
            System.out.printf("%-30s %-8d %-8d %-10.2f%n", e.getKey(), cp, cm.size() - cp, ca);
        }
    }

    private void viewComplaints() {
        List<Complaint> complaints = storage.getComplaints();
        System.out.println("\n--- Complaints (" + complaints.size() + ") ---");
        if (complaints.isEmpty()) { System.out.println("No complaints filed."); return; }
        for (int i = 0; i < complaints.size(); i++) {
            Complaint c = complaints.get(i);
            System.out.println((i + 1) + ". [" + c.getUrgency() + "] "
                    + "From: " + c.getSender().getFirstName() + " " + c.getSender().getLastName()
                    + " | About: " + c.getTarget().getFirstName() + " " + c.getTarget().getLastName());
            System.out.println("   " + c.getDescription());
            System.out.println("   Filed: " + c.getCreatedAt());
        }
    }

    private void printAllResearchPapers() {
        System.out.println("Sort by: 1. Citations  2. Date  3. Length");
        System.out.print("Choice: ");
        java.util.Comparator<ResearchPaper> comparator;
        switch (scanner.nextLine().trim()) {
            case "1": comparator = ResearchPaper.BY_CITATIONS; break;
            case "3": comparator = ResearchPaper.BY_LENGTH;    break;
            default:  comparator = ResearchPaper.BY_DATE;      break;
        }
        University.getInstance().printAllResearcherPapers(comparator);
    }

    private void topCitedUniversity() {
        System.out.println("\n--- Top Cited Researcher (University) ---");
        University.getInstance().printTopCitedResearcher();
    }

    private void topCitedByYear() {
        System.out.print("Enter year: ");
        int year = parseIntSafe(scanner.nextLine().trim(), java.time.Year.now().getValue());
        System.out.println("\n--- Top Cited Researcher in " + year + " ---");
        University.getInstance().printTopCitedResearcherByYear(year);
    }

    private void topCitedBySchool() {
        System.out.println("\n--- Select School ---");
        SchoolCode[] values = SchoolCode.values();
        for (int i = 0; i < values.length; i++)
            System.out.println((i + 1) + ". " + values[i].getDisplayName());
        System.out.print("Choice: ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 3) - 1;
        if (idx < 0 || idx >= values.length) idx = 2;
        University.getInstance().printTopCitedResearcherBySchool(values[idx]);
    }

    private List<Student> getStudents() {
        List<Student> list = new ArrayList<>();
        for (User u : storage.getUsers().values())
            if (u instanceof Student) list.add((Student) u);
        return list;
    }

    private List<Teacher> getTeachers() {
        List<Teacher> list = new ArrayList<>();
        for (User u : storage.getUsers().values())
            if (u instanceof Teacher) list.add((Teacher) u);
        return list;
    }

    private Language parseLanguage(String choice) {
        switch (choice) {
            case "1": return Language.KZ;
            case "3": return Language.RU;
            default:  return Language.EN;
        }
    }

    private SchoolCode selectSchool() {
        System.out.println("School:");
        SchoolCode[] values = SchoolCode.values();
        for (int i = 0; i < values.length; i++)
            System.out.println((i + 1) + ". " + values[i].getDisplayName());
        System.out.print("Choice (default FIT): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 3) - 1;
        if (idx < 0 || idx >= values.length) return SchoolCode.FIT;
        return values[idx];
    }
}