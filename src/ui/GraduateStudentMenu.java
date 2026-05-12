package ui;

import model.exceptions.LowHIndexSupervisorException;
import model.research.ResearchPaper;
import model.research.ResearchProject;
import model.research.Researcher;
import model.users.GraduateStudent;
import model.users.User;
import storage.DataStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GraduateStudentMenu extends StudentMenu {

    private final GraduateStudent gradStudent;

    public GraduateStudentMenu(Scanner scanner, DataStorage storage, GraduateStudent gradStudent) {
        super(scanner, storage, gradStudent);
        this.gradStudent = gradStudent;
    }

    @Override
    protected void printMenu() {
        System.out.println("\n--- Graduate Student Menu ["
                + gradStudent.getFirstName() + " " + gradStudent.getLastName()
                + " | " + gradStudent.getGraduateType() + "] ---");
        System.out.println("--- Student options ---");
        System.out.println("1.  View profile");
        System.out.println("2.  View available courses");
        System.out.println("3.  Register for a course");
        System.out.println("4.  Drop a course");
        System.out.println("5.  View my enrollments");
        System.out.println("6.  View my marks");
        System.out.println("7.  View transcript");
        System.out.println("8.  View teacher info");
        System.out.println("9.  View student organizations");
        System.out.println("10. Join a student organization");
        System.out.println("11. View news");
        System.out.println("12. Submit tech support request");
        System.out.println("--- Graduate options ---");
        System.out.println("13. View / set supervisor");
        System.out.println("14. Publish research paper");
        System.out.println("15. View my papers");
        System.out.println("16. View / join research projects");
        System.out.println("0.  Logout");
    }

    @Override
    protected void handleChoice(String choice) {
        switch (choice) {
            case "13": viewSetSupervisor();     break;
            case "14": publishPaper();          break;
            case "15": viewPapers();            break;
            case "16": researchProjects();      break;
            default:   super.handleChoice(choice);
        }
    }

    // ── 13. View / set supervisor ─────────────────────────────────────────────

    private void viewSetSupervisor() {
        System.out.println("\n--- Supervisor ---");
        if (gradStudent.getSupervisor() != null) {
            Researcher sup = gradStudent.getSupervisor();
            String name = sup instanceof User
                    ? ((User) sup).getFirstName() + " " + ((User) sup).getLastName()
                    : "Unknown";
            System.out.println("Current supervisor : " + name);
            System.out.println("H-index            : " + sup.calculateHindex());
            System.out.println("Papers             : " + sup.getPapers().size());
        } else {
            System.out.println("No supervisor assigned yet.");
        }

        System.out.println("\nAssign a new supervisor? (y/n)");
        System.out.print("Choice: ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("y")) return;

        // Collect all Researchers from users (Teachers who are Researchers)
        List<Researcher> researchers = new ArrayList<>();
        for (User u : storage.getUsers().values())
            if (u instanceof Researcher) researchers.add((Researcher) u);

        if (researchers.isEmpty()) { System.out.println("No researchers found."); return; }

        System.out.println("\n--- Available Supervisors ---");
        for (int i = 0; i < researchers.size(); i++) {
            Researcher r = researchers.get(i);
            String name = r instanceof User
                    ? ((User) r).getFirstName() + " " + ((User) r).getLastName()
                    : "Unknown";
            System.out.println((i + 1) + ". " + name
                    + " | h-index: " + r.calculateHindex()
                    + " | papers: " + r.getPapers().size());
        }
        System.out.print("Choice: ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= researchers.size()) { System.out.println("Invalid."); return; }

        try {
            gradStudent.setSupervisor(researchers.get(idx));
            storage.updateAndSave();
            Researcher assigned = researchers.get(idx);
            String name = assigned instanceof User
                    ? ((User) assigned).getFirstName() + " " + ((User) assigned).getLastName()
                    : "Unknown";
            System.out.println("Supervisor assigned: " + name);
        } catch (LowHIndexSupervisorException e) {
            System.out.println("Cannot assign supervisor: " + e.getMessage());
        }
    }

    // ── 14. Publish research paper ────────────────────────────────────────────

    private void publishPaper() {
        System.out.println("\n--- Publish Research Paper ---");
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Journal: ");
        String journal = scanner.nextLine().trim();
        System.out.print("Pages: ");
        int pages = parseIntSafe(scanner.nextLine().trim(), 1);
        System.out.print("DOI: ");
        String doi = scanner.nextLine().trim();

        ResearchPaper paper = new ResearchPaper(title, journal, pages, doi);
        paper.getAuthors().add(gradStudent);
        gradStudent.publishPaper(paper);
        storage.updateAndSave();
        System.out.println("Paper published and added to your diploma projects.");
    }

    // ── 15. View my papers ────────────────────────────────────────────────────

    private void viewPapers() {
        if (gradStudent.getPapers().isEmpty()) {
            System.out.println("No papers published yet.");
            pause();
            return;
        }
        System.out.println("H-index: " + gradStudent.calculateHindex());
        System.out.println("Sort by: 1. Citations  2. Date  3. Length");
        System.out.print("Choice: ");
        switch (scanner.nextLine().trim()) {
            case "1": gradStudent.printPapers(ResearchPaper.BY_CITATIONS); break;
            case "3": gradStudent.printPapers(ResearchPaper.BY_LENGTH);    break;
            default:  gradStudent.printPapers(ResearchPaper.BY_DATE);      break;
        }
        pause();
    }

    // ── 16. View / join research projects ────────────────────────────────────

    private void researchProjects() {
        // Research projects are stored inside DataStorage — we need to surface them
        // For now collect from all courses (future: dedicated list in DataStorage)
        System.out.println("\n--- Research Projects ---");
        System.out.println("1. View available projects");
        System.out.println("2. Create new project");
        System.out.print("Choice: ");
        switch (scanner.nextLine().trim()) {
            case "1": viewAndJoinProjects(); break;
            case "2": createProject();       break;
            default:  System.out.println("Invalid.");
        }
    }

    private void viewAndJoinProjects() {
        List<ResearchProject> projects = storage.getResearchProjects();
        if (projects.isEmpty()) {
            System.out.println("No research projects available.");
            pause();
            return;
        }

        System.out.println("\n--- Available Projects ---");
        for (int i = 0; i < projects.size(); i++) {
            ResearchProject p = projects.get(i);
            System.out.println((i + 1) + ". " + p.getTopic()
                    + " | Participants: " + p.getParticipants().size()
                    + " | Papers: " + p.getPublishedPapers().size());
        }
        System.out.print("Join a project? Enter number or 0 to cancel: ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= projects.size()) return;

        projects.get(idx).addParticipant((Researcher) gradStudent);
        storage.updateAndSave();
    }

    private void createProject() {
        System.out.print("Project topic: ");
        String topic = scanner.nextLine().trim();
        String id = "PROJ" + System.currentTimeMillis();
        ResearchProject project = new ResearchProject(id, topic);
        project.addParticipant((Researcher) gradStudent);
        storage.saveResearchProject(project);
        System.out.println("Project created: " + topic);
    }
}