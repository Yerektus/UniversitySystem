package model.communication;

import model.enums.UrgencyLevel;
import java.util.Date;

public class Complaint {

    private String complaintId;

    private String sender;

    private String target;

    private String description;

    private UrgencyLevel urgency;

    private Date createdAt;

    public Complaint(String complaintId, String sender, String target,
                     String description, UrgencyLevel urgency, Date createdAt) {
        this.complaintId = complaintId;
        this.sender = sender;
        this.target = target;
        this.description = description;
        this.urgency = urgency;
        this.createdAt = createdAt;
    }

    public UrgencyLevel getUrgency() {
        return urgency;
    }

    public String getComplaintId() { return complaintId; }

    public void setComplaintId(String complaintId) { this.complaintId = complaintId; }

    public String getSender() { return sender; }

    public void setSender(String sender) { this.sender = sender; }

    public String getTarget() { return target; }

    public void setTarget(String target) { this.target = target; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public void setUrgency(UrgencyLevel urgency) { this.urgency = urgency; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Complaint{sender='" + sender + "', target='" + target + "', urgency=" + urgency + "}";
    }
}
