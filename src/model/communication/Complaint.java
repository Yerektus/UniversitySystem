package model.communication;

import model.enums.UrgencyLevel;
import model.users.Student;
import model.users.Teacher;
import java.io.Serializable;
import java.util.Date;

public class Complaint implements Serializable {

    private String complaintId;
    private Teacher sender;
    private Student target;
    private String description;
    private UrgencyLevel urgency;
    private Date createdAt;

    public Complaint(String complaintId, Teacher sender, Student target,
                     String description, UrgencyLevel urgency, Date createdAt) {
        this.complaintId = complaintId;
        this.sender = sender;
        this.target = target;
        this.description = description;
        this.urgency = urgency;
        this.createdAt = createdAt;
    }

    public String getComplaintId() { return complaintId; }
    public void setComplaintId(String complaintId) { this.complaintId = complaintId; }
    public Teacher getSender() { return sender; }
    public void setSender(Teacher sender) { this.sender = sender; }
    public Student getTarget() { return target; }
    public void setTarget(Student target) { this.target = target; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public UrgencyLevel getUrgency() { return urgency; }
    public void setUrgency(UrgencyLevel urgency) { this.urgency = urgency; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Complaint)) return false;
        return complaintId.equals(((Complaint) obj).complaintId);
    }

    @Override
    public int hashCode() { return complaintId.hashCode(); }

    @Override
    public String toString() {
        return "Complaint{from=" + sender.getFirstName() + " " + sender.getLastName()
                + ", about=" + target.getFirstName() + " " + target.getLastName()
                + ", urgency=" + urgency + "}";
    }
}