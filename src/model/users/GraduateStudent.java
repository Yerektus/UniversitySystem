package model.users;

import model.enums.CitationFormat;
import model.enums.GraduateType;
import model.enums.Language;
import model.exceptions.LowHIndexSupervisorException;
import model.research.ResearchPaper;
import model.research.Researcher;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GraduateStudent extends Student implements Researcher {

	private GraduateType graduateType;
    private String target;
    private List<ResearchPaper> papers;
    private Researcher supervisor;

    public GraduateStudent(String id, String password, String firstName, String lastName,
                           String email, Language language, String major, int year,
                           GraduateType graduateType, String target) {
        super(id, password, firstName, lastName, email, language, major, year);
        this.graduateType = graduateType;
        this.target = target;
        this.papers = new ArrayList<>();
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

    public List<ResearchPaper> getDiplomaProjects() {
        return papers;
    }

    public Researcher getSupervisor() { return supervisor; }

    public void setSupervisor(Researcher supervisor) throws LowHIndexSupervisorException {
        if (supervisor.calculateHindex() < 3) {
            throw new LowHIndexSupervisorException(
                "Supervisor h-index is " + supervisor.calculateHindex()
                + ", minimum required is 3."
            );
        }
        this.supervisor = supervisor;
    }

    public GraduateType getGraduateType() { return graduateType; }

    public void setGraduateType(GraduateType graduateType) { this.graduateType = graduateType; }

    public String getTarget() { return target; }

    public void setTarget(String target) { this.target = target; }

    public void setPapers(List<ResearchPaper> papers) { this.papers = papers; }
}