package model.research;

import model.enums.CitationFormat;

public interface Researcher {
    int calculateHindex();
    void printPapers(CitationFormat format);
    void publishPaper(ResearchPaper paper);
    void printProjectResearchPaper(ResearchPaper paper);
}
