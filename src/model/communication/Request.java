package model.communication;

import model.enums.RequestStatus;
import model.users.User;
import java.util.Date;

public class Request {

    private String requestId;

    private String topic;

    private User sender;

    private String description;

    private RequestStatus status;

    private Date createdAt;

    public Request(String requestId, String topic, User sender, String description) {
        this.requestId = requestId;
        this.topic = topic;
        this.sender = sender;
        this.description = description;
        this.status = RequestStatus.NEW;
        this.createdAt = new Date();
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void updateStatus(RequestStatus status) {
        this.status = status;
        System.out.println("Request '" + topic + "' status updated to: " + status);
    }

    public String getRequestId() { return requestId; }

    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getTopic() { return topic; }

    public void setTopic(String topic) { this.topic = topic; }

    public User getSender() { return sender; }

    public void setSender(User sender) { this.sender = sender; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Request{topic='" + topic + "', status=" + status + "}";
    }
}
