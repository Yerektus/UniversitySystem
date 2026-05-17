package model.academic;

import model.users.Student;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StudentOrganization implements Serializable {

    private String orgId;
    private String name;
    private String target;
    private List<Student> members;
    private Student head;

    public StudentOrganization(String orgId, String name, String target) {
        this.orgId = orgId;
        this.name = name;
        this.target = target;
        this.members = new ArrayList<>();
        this.head = null;
    }

    public void addMember(Student student) {
        if (!members.contains(student)) {
            members.add(student);
            System.out.println(student.getFirstName() + " joined " + name);
        }
    }

    public void setHead(Student student) {
        if (!members.contains(student)) addMember(student);
        this.head = student;
        System.out.println(student.getFirstName() + " is now head of " + name);
    }

    public Student getHead() { return head; }
    public String getOrgId() { return orgId; }
    public void setOrgId(String orgId) { this.orgId = orgId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    public List<Student> getMembers() { return members; }
    public void setMembers(List<Student> members) { this.members = members; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StudentOrganization)) return false;
        return orgId.equals(((StudentOrganization) obj).orgId);
    }

    @Override
    public int hashCode() { return orgId.hashCode(); }

    @Override
    public String toString() {
        return "StudentOrganization{name='" + name + "', members=" + members.size()
                + ", head=" + (head != null ? head.getFirstName() : "none") + "}";
    }
}