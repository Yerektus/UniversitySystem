package model.communication;

import model.users.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message {

    private String messageId;

    private String message;

    private String topic;

    private User sender;

    private User receiver;

    private String content;

    private boolean pinned;

    private Date publishedAt;

    private User author;

    private List<String> comments;

    public Message(String messageId, String topic, String content, User sender, User receiver) {
        this.messageId = messageId;
        this.topic = topic;
        this.content = content;
        this.message = content;
        this.sender = sender;
        this.receiver = receiver;
        this.author = sender;
        this.pinned = false;
        this.publishedAt = new Date();
        this.comments = new ArrayList<>();
    }

    public void addComment(User user, String text) {
        comments.add(user.getFirstName() + ": " + text);
    }

    public boolean isPinned() {
        return pinned;
    }

    public String getMessageId() { return messageId; }

    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getTopic() { return topic; }

    public void setTopic(String topic) { this.topic = topic; }

    public User getSender() { return sender; }

    public void setSender(User sender) { this.sender = sender; }

    public User getReceiver() { return receiver; }

    public void setReceiver(User receiver) { this.receiver = receiver; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public boolean isPinnedValue() { return pinned; }

    public void setPinned(boolean pinned) { this.pinned = pinned; }

    public Date getPublishedAt() { return publishedAt; }

    public void setPublishedAt(Date publishedAt) { this.publishedAt = publishedAt; }

    public User getAuthor() { return author; }

    public void setAuthor(User author) { this.author = author; }

    public List<String> getComments() { return comments; }

    public void setComments(List<String> comments) { this.comments = comments; }

    @Override
    public String toString() {
        return "Message{from=" + sender.getFirstName() + ", to=" + receiver.getFirstName()
                + ", topic='" + topic + "'}";
    }
}
