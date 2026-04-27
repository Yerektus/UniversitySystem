package model.communication;

import model.users.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class News {

    private String newsId;

    private String title;

    private String content;

    private String topic;

    private User sender;

    private boolean pinned;

    private Date publishedAt;

    private User author;

    private List<String> comments;

    public News(String newsId, String title, String content, String topic, User author) {
        this.newsId = newsId;
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.author = author;
        this.sender = author;
        this.pinned = false;
        this.publishedAt = new Date();
        this.comments = new ArrayList<>();
    }

    public String getNewsId() { return newsId; }

    public void setNewsId(String newsId) { this.newsId = newsId; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getTopic() { return topic; }

    public void setTopic(String topic) { this.topic = topic; }

    public User getSender() { return sender; }

    public void setSender(User sender) { this.sender = sender; }

    public boolean isPinned() { return pinned; }

    public void setPinned(boolean pinned) { this.pinned = pinned; }

    public Date getPublishedAt() { return publishedAt; }

    public void setPublishedAt(Date publishedAt) { this.publishedAt = publishedAt; }

    public User getAuthor() { return author; }

    public void setAuthor(User author) { this.author = author; }

    public List<String> getComments() { return comments; }

    public void setComments(List<String> comments) { this.comments = comments; }

    @Override
    public String toString() {
        return "News{title='" + title + "', topic='" + topic + "', pinned=" + pinned + "}";
    }
}
