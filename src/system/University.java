package system;

import model.enums.CitationFormat;
import model.enums.SchoolCode;
import model.research.ResearchPaper;
import model.research.Researcher;
import model.users.User;
import storage.DataStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class University {

    private static University instance;
    private final String name;

    private University(String name) {
        this.name = name;
    }

    public static University getInstance() {
        if (instance == null) instance = new University("Kazakh-British Technical University");
        return instance;
    }

    public String getName() { return name; }

    public Researcher getTopCitedResearcher() {
        Researcher top = null;
        int maxCitations = -1;
        for (User u : DataStorage.getInstance().getUsers().values()) {
            if (u instanceof Researcher) {
                Researcher r = (Researcher) u;
                int total = r.getPapers().stream().mapToInt(ResearchPaper::getCitations).sum();
                if (total > maxCitations) { maxCitations = total; top = r; }
            }
        }
        return top;
    }

    public Researcher getTopCitedResearcherByYear(int year) {
        Researcher top = null;
        int maxCitations = -1;
        for (User u : DataStorage.getInstance().getUsers().values()) {
            if (!(u instanceof Researcher)) continue;
            Researcher r = (Researcher) u;
            int total = 0;
            for (ResearchPaper p : r.getPapers()) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(p.getDatePublished());
                if (cal.get(Calendar.YEAR) == year) total += p.getCitations();
            }
            if (total > maxCitations) { maxCitations = total; top = r; }
        }
        return top;
    }

    public Researcher getTopCitedResearcherBySchool(SchoolCode school) {
        Researcher top = null;
        int maxCitations = -1;
        for (User u : DataStorage.getInstance().getUsers().values()) {
            if (!(u instanceof Researcher)) continue;
            if (!u.getId().contains(school.getCode())) continue;
            Researcher r = (Researcher) u;
            int total = r.getPapers().stream().mapToInt(ResearchPaper::getCitations).sum();
            if (total > maxCitations) { maxCitations = total; top = r; }
        }
        return top;
    }

    public void printTopCitedResearcher() {
        Researcher top = getTopCitedResearcher();
        if (top == null) { System.out.println("No researchers found."); return; }
        User u = (User) top;
        System.out.println("Top cited researcher: " + u.getFirstName() + " " + u.getLastName()
                + " | h-index: " + top.calculateHindex()
                + " | papers: " + top.getPapers().size());
    }

    public void printTopCitedResearcherByYear(int year) {
        Researcher top = getTopCitedResearcherByYear(year);
        if (top == null) { System.out.println("No researchers found for year " + year + "."); return; }
        User u = (User) top;
        System.out.println("Top cited researcher in " + year + ": "
                + u.getFirstName() + " " + u.getLastName()
                + " | h-index: " + top.calculateHindex());
    }

    public void printTopCitedResearcherBySchool(SchoolCode school) {
        Researcher top = getTopCitedResearcherBySchool(school);
        if (top == null) { System.out.println("No researchers found for " + school.getDisplayName() + "."); return; }
        User u = (User) top;
        System.out.println("Top cited researcher in " + school.getDisplayName() + ": "
                + u.getFirstName() + " " + u.getLastName()
                + " | h-index: " + top.calculateHindex());
    }

    public void printAllResearcherPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> allPapers = new ArrayList<>();
        for (User u : DataStorage.getInstance().getUsers().values())
            if (u instanceof Researcher)
                allPapers.addAll(((Researcher) u).getPapers());
        if (allPapers.isEmpty()) { System.out.println("No research papers found."); return; }
        allPapers.sort(comparator);
        System.out.println("--- All Research Papers (" + allPapers.size() + ") ---");
        for (ResearchPaper p : allPapers)
            System.out.println(p.getCitation(CitationFormat.PLAIN_TEXT));
    }
}