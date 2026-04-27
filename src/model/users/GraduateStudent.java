package model.users;

import model.enums.GraduateType;
import model.enums.Language;
import model.research.ResearchPaper;
import model.research.Researcher;
import model.enums.CitationFormat;
import java.util.ArrayList;
import java.util.List;

public class GraduateStudent extends Student implements Researcher {

    private GraduateType graduateType;
    private String target;
    private List<ResearchPaper> diplomaProjects;

    public GraduateStudent(String id, String password, String firstName, String lastName,
                           String email, Language language, String major, int year,
                           GraduateType graduateType, String target) {
        super(id, password, firstName, lastName, email, language, major, year);
        this.graduateType = graduateType;
        this.target = target;
        this.diplomaProjects = new ArrayList<>();
    }

    public void addDiplomaProject(ResearchPaper paper) {
        diplomaProjects.add(paper);
        System.out.println("Diploma project added: " + paper.getTitle());
    }

    @Override
    public int calculateHindex() {
        int h = 0;
        List<Integer> citations = new ArrayList<>();
        for (ResearchPaper p : diplomaProjects) citations.add(p.getCitations());
        citations.sort((a, b) -> b - a);
        for (int i = 0; i < citations.size(); i++) {
            if (citations.get(i) >= i + 1) h = i + 1;
            else break;
        }
        return h;
    }

    @Override
    public void printPapers(CitationFormat format) {
        for (ResearchPaper p : diplomaProjects) {
            System.out.println(p.getCitation(format));
        }
    }

    @Override
    public void publishPaper(ResearchPaper paper) {
        diplomaProjects.add(paper);
        System.out.println(getFirstName() + " published: " + paper.getTitle());
    }

    @Override
    public void printProjectResearchPaper(ResearchPaper paper) {
        System.out.println("Project paper: " + paper.getTitle());
    }

    public GraduateType getGraduateType() { return graduateType; }

    public void setGraduateType(GraduateType graduateType) { this.graduateType = graduateType; }

    public String getTarget() { return target; }

    public void setTarget(String target) { this.target = target; }

    public List<ResearchPaper> getDiplomaProjects() { return diplomaProjects; }

    public void setDiplomaProjects(List<ResearchPaper> diplomaProjects) { this.diplomaProjects = diplomaProjects; }
}
