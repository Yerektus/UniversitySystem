package model.communication;

import model.users.User;
import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private String messageId;
    private String topic;
    private String content;
    private User sender;
    private User receiver;
    private Date sentAt;

    public Message(String messageId, String topic, String content, User sender, User receiver) {
        this.messageId = messageId;
        this.topic     = topic;
        this.content   = content;
        this.sender    = sender;
        this.receiver  = receiver;
        this.sentAt    = new Date();
    }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }
    public User getReceiver() { return receiver; }
    public void setReceiver(User receiver) { this.receiver = receiver; }
    public Date getSentAt() { return sentAt; }
    public void setSentAt(Date sentAt) { this.sentAt = sentAt; }

    @Override
    public String toString() {
        return "Message{from=" + sender.getFirstName() + " " + sender.getLastName()
                + ", to=" + receiver.getFirstName() + " " + receiver.getLastName()
                + ", topic='" + topic + "'}";
    }
}

