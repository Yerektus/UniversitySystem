package model.research;

import java.util.ArrayList;
import java.util.List;

public class ResearchProject {

    private String projectId;

    private String topic;

    private List<ResearchPaper> publishedPapers;

    private List<Researcher> participants;

    public ResearchProject(String projectId, String topic) {
        this.projectId = projectId;
        this.topic = topic;
        this.publishedPapers = new ArrayList<>();
        this.participants = new ArrayList<>();
    }

    public void addParticipant(Researcher researcher) {
        participants.add(researcher);
        System.out.println("Participant added to project " + topic);
    }

    public void addPaper(ResearchPaper paper) {
        publishedPapers.add(paper);
        System.out.println("Paper added to project: " + paper.getTitle());
    }

    public String getProjectId() { return projectId; }

    public void setProjectId(String projectId) { this.projectId = projectId; }

    public String getTopic() { return topic; }

    public void setTopic(String topic) { this.topic = topic; }

    public List<ResearchPaper> getPublishedPapers() { return publishedPapers; }

    public void setPublishedPapers(List<ResearchPaper> publishedPapers) { this.publishedPapers = publishedPapers; }

    public List<Researcher> getParticipants() { return participants; }

    public void setParticipants(List<Researcher> participants) { this.participants = participants; }

    @Override
    public String toString() {
        return "ResearchProject{id='" + projectId + "', topic='" + topic + "'}";
    }
}
