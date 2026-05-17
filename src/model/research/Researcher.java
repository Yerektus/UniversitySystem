package model.research;

import java.util.Comparator;
import java.util.List;

public interface Researcher {
    List<ResearchPaper> getPapers();
    int calculateHindex();
    void printPapers(Comparator<ResearchPaper> c);
    void publishPaper(ResearchPaper paper);
}