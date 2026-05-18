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
        System.out.println("1. View profile");
        System.out.println("2. Courses");
        System.out.println("3. Academic");
        System.out.println("4. Campus");
        System.out.println("5. Support");
        System.out.println("6. Research");
        System.out.println("0. Logout");
    }

    @Override
    protected void handleChoice(String choice) {
        switch (choice) {
            case "6": researchMenu(); break;
            default:  super.handleChoice(choice);
        }
    }

    private void researchMenu() {
        while (true) {
            System.out.println("\n--- Graduate Student Menu: Research ---");
            System.out.println("1. View / set supervisor");
            System.out.println("2. Publish research paper");
            System.out.println("3. View my papers");
            System.out.println("4. View / join research projects");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            switch (scanner.nextLine().trim()) {
                case "1": viewSetSupervisor();  break;
                case "2": publishPaper();       break;
                case "3": viewPapers();         break;
                case "4": researchProjects();   break;
                case "0": return;
                default:  System.out.println("Invalid choice.");
            }
        }
    }

    private void viewSetSupervisor() {
        System.out.println("\n--- Supervisor ---");
        if (gradStudent.getSupervisor() != null) {
            Researcher sup = gradStudent.getSupervisor();
            String name = sup instanceof User
                    ? ((User) sup).getFirstName() + " " + ((User) sup).getLastName() : "Unknown";
            System.out.println("Current supervisor : " + name);
            System.out.println("H-index            : " + sup.calculateHindex());
            System.out.println("Papers             : " + sup.getPapers().size());
        } else {
            System.out.println("No supervisor assigned yet.");
        }

        System.out.print("Assign a new supervisor? (y/n): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("y")) return;

        List<Researcher> researchers = new ArrayList<>();
        for (User u : storage.getUsers().values())
            if (u instanceof Researcher) researchers.add((Researcher) u);
        if (researchers.isEmpty()) { System.out.println("No researchers found."); return; }

        System.out.println("\n--- Available Supervisors ---");
        for (int i = 0; i < researchers.size(); i++) {
            Researcher r = researchers.get(i);
            String name = r instanceof User
                    ? ((User) r).getFirstName() + " " + ((User) r).getLastName() : "Unknown";
            System.out.println((i + 1) + ". " + name
                    + " | h-index: " + r.calculateHindex()
                    + " | papers: " + r.getPapers().size());
        }
        System.out.print("Choice (0 to cancel): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= researchers.size()) { System.out.println("Cancelled."); return; }

        try {
            gradStudent.setSupervisor(researchers.get(idx));
            storage.updateAndSave();
            Researcher assigned = researchers.get(idx);
            String name = assigned instanceof User
                    ? ((User) assigned).getFirstName() + " " + ((User) assigned).getLastName() : "Unknown";
            System.out.println("Supervisor assigned: " + name);
        } catch (LowHIndexSupervisorException e) {
            System.out.println("Cannot assign supervisor: " + e.getMessage());
        }
    }

    private void publishPaper() {
        System.out.print("Title (0 to cancel): ");
        String title = scanner.nextLine().trim();
        if (title.equals("0")) return;
        System.out.print("Journal: "); String journal = scanner.nextLine().trim();
        System.out.print("Pages: ");   int pages      = parseIntSafe(scanner.nextLine().trim(), 1);
        System.out.print("DOI: ");     String doi     = scanner.nextLine().trim();

        ResearchPaper paper = new ResearchPaper(title, journal, pages, doi);
        paper.getAuthors().add(gradStudent);
        gradStudent.publishPaper(paper);
        storage.updateAndSave();
        System.out.println("Paper published and added to your diploma projects.");
    }

    private void viewPapers() {
        if (gradStudent.getPapers().isEmpty()) { System.out.println("No papers published yet."); return; }
        System.out.println("H-index: " + gradStudent.calculateHindex());
        System.out.println("Sort by: 1. Citations  2. Date  3. Length");
        System.out.print("Choice: ");
        switch (scanner.nextLine().trim()) {
            case "1": gradStudent.printPapers(ResearchPaper.BY_CITATIONS); break;
            case "3": gradStudent.printPapers(ResearchPaper.BY_LENGTH);    break;
            default:  gradStudent.printPapers(ResearchPaper.BY_DATE);      break;
        }
    }

    private void researchProjects() {
        while (true) {
            System.out.println("\n--- Graduate Student Menu: Research Projects ---");
            System.out.println("1. View available projects");
            System.out.println("2. Create new project");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            switch (scanner.nextLine().trim()) {
                case "1": viewAndJoinProjects(); break;
                case "2": createProject();       break;
                case "0": return;
                default:  System.out.println("Invalid choice.");
            }
        }
    }

    private void viewAndJoinProjects() {
        List<ResearchProject> projects = storage.getResearchProjects();
        if (projects.isEmpty()) { System.out.println("No research projects available."); return; }

        System.out.println("\n--- Available Projects ---");
        for (int i = 0; i < projects.size(); i++) {
            ResearchProject p = projects.get(i);
            System.out.println((i + 1) + ". " + p.getTopic()
                    + " | Participants: " + p.getParticipants().size()
                    + " | Papers: " + p.getPublishedPapers().size());
        }
        System.out.print("Join a project (0 to cancel): ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= projects.size()) { System.out.println("Cancelled."); return; }

        projects.get(idx).addParticipant((Researcher) gradStudent);
        storage.updateAndSave();
        System.out.println("Joined project: " + projects.get(idx).getTopic());
    }

    private void createProject() {
        System.out.print("Project topic (0 to cancel): ");
        String topic = scanner.nextLine().trim();
        if (topic.equals("0")) return;
        String id = "PROJ" + System.currentTimeMillis();
        ResearchProject project = new ResearchProject(id, topic);
        project.addParticipant((Researcher) gradStudent);
        storage.saveResearchProject(project);
        System.out.println("Project created: " + topic);
    }
}