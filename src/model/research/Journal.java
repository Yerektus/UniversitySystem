package model.research;

import model.communication.News;
import model.users.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Journal implements Serializable {

	private String journalId;
    private String name;
    private List<ResearchPaper> papers;
    private List<User> subscribers;

    public Journal(String journalId, String name) {
        this.journalId = journalId;
        this.name = name;
        this.papers = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }

    public void subscribe(User user) {
        if (!subscribers.contains(user)) {
            subscribers.add(user);
            System.out.println(user.getFirstName() + " subscribed to " + name);
        }
    }

    public void unsubscribe(User user) {
        subscribers.remove(user);
        System.out.println(user.getFirstName() + " unsubscribed from " + name);
    }

    public News publishPaper(ResearchPaper paper) {
        papers.add(paper);
        notifySubscribers(paper);
        return buildNewsAnnouncement(paper);
    }

    private void notifySubscribers(ResearchPaper paper) {
        for (User u : subscribers) {
            u.onPaperPublished(paper, this);
        }
    }

    private News buildNewsAnnouncement(ResearchPaper paper) {
        String newsId = "N" + System.currentTimeMillis();
        String title = "New Paper Published in " + name;
        String content = "\"" + paper.getTitle() + "\" has been published in " + name + ".";
        return new News(newsId, title, content, "Research", null);
    }

    public String getJournalId() { return journalId; }
    public void setJournalId(String journalId) { this.journalId = journalId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<ResearchPaper> getPapers() { return papers; }
    public void setPapers(List<ResearchPaper> papers) { this.papers = papers; }
    public List<User> getSubscribers() { return subscribers; }
    public void setSubscribers(List<User> subscribers) { this.subscribers = subscribers; }

    @Override
    public String toString() {
        return "Journal{name='" + name + "', papers=" + papers.size() + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Journal)) return false;
        return journalId.equals(((Journal) obj).journalId);
    }

    @Override
    public int hashCode() { return journalId.hashCode(); }
}