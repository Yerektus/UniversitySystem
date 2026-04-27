package model.research;

import model.enums.CitationFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResearchPaper {

    private String title;
    private List<Researcher> authors;
    private String journal;
    private int pages;
    private Date datePublished;
    private String doi;
    private int citations;

    public ResearchPaper(String title, String journal, int pages, String doi) {
        this.title = title;
        this.journal = journal;
        this.pages = pages;
        this.doi = doi;
        this.datePublished = new Date();
        this.citations = 0;
        this.authors = new ArrayList<>();
    }

    public String getCitation(CitationFormat format) {
        switch (format) {
            case APA:
                return title + ". " + journal + ". doi:" + doi;
            case BIBTEX:
                return "@article{" + doi + ", title={" + title + "}, journal={" + journal + "}}";
            default:
                return title + " (" + journal + ")";
        }
    }

    public int compareTo(ResearchPaper other) {
        return Integer.compare(this.citations, other.citations);
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public List<Researcher> getAuthors() { return authors; }

    public void setAuthors(List<Researcher> authors) { this.authors = authors; }

    public String getJournal() { return journal; }

    public void setJournal(String journal) { this.journal = journal; }

    public int getPages() { return pages; }

    public void setPages(int pages) { this.pages = pages; }

    public Date getDatePublished() { return datePublished; }

    public void setDatePublished(Date datePublished) { this.datePublished = datePublished; }

    public String getDoi() { return doi; }

    public void setDoi(String doi) { this.doi = doi; }

    public int getCitations() { return citations; }

    public void setCitations(int citations) { this.citations = citations; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ResearchPaper)) return false;
        ResearchPaper other = (ResearchPaper) obj;
        return doi != null && doi.equals(other.doi);
    }

    @Override
    public int hashCode() {
        return doi != null ? doi.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ResearchPaper{title='" + title + "', journal='" + journal + "', citations=" + citations + "}";
    }
}
