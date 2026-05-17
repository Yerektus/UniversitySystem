package model.research;

import model.enums.CitationFormat;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ResearchPaper implements Comparable<ResearchPaper>, Serializable {

	private static final long serialVersionUID = 1L;

    public static final Comparator<ResearchPaper> BY_CITATIONS =
            Comparator.comparingInt(ResearchPaper::getCitations).reversed();

    public static final Comparator<ResearchPaper> BY_DATE =
            Comparator.comparing(ResearchPaper::getDatePublished).reversed();

    public static final Comparator<ResearchPaper> BY_LENGTH =
            Comparator.comparingInt(ResearchPaper::getPages).reversed();

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
        String authorNames = buildAuthorNames();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(datePublished);
        int year = cal.get(java.util.Calendar.YEAR);

        switch (format) {
            case BIBTEX:
                return "@article{" + doi + ",\n"
                        + "  title   = {" + title + "},\n"
                        + "  author  = {" + authorNames + "},\n"
                        + "  journal = {" + journal + "},\n"
                        + "  year    = {" + year + "},\n"
                        + "  pages   = {" + pages + "},\n"
                        + "  doi     = {" + doi + "}\n"
                        + "}";
            default:
                return authorNames + " (" + year + "). " + title + ". "
                        + journal + ". " + pages + " pages. doi:" + doi;
        }
    }

    private String buildAuthorNames() {
        if (authors.isEmpty()) return "Unknown";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < authors.size(); i++) {
            if (authors.get(i) instanceof model.users.User) {
                model.users.User u = (model.users.User) authors.get(i);
                sb.append(u.getLastName()).append(", ").append(u.getFirstName().charAt(0)).append(".");
            }
            if (i < authors.size() - 1) sb.append("; ");
        }
        return sb.toString();
    }

    @Override
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