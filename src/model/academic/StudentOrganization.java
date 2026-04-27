package model.academic;

import model.users.Student;
import java.util.ArrayList;
import java.util.List;

public class StudentOrganization {

    private String orgId;
    private String name;
    private String target;
    private List<Student> members;

    public StudentOrganization(String orgId, String name, String target) {
        this.orgId = orgId;
        this.name = name;
        this.target = target;
        this.members = new ArrayList<>();
    }

    public void addMember(Student student) {
        if (!members.contains(student)) {
            members.add(student);
            System.out.println(student.getFirstName() + " joined " + name);
        }
    }

    public void setHead(Student student) {
        System.out.println(student.getFirstName() + " is now head of " + name);
    }

    public String getOrgId() { return orgId; }

    public void setOrgId(String orgId) { this.orgId = orgId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getTarget() { return target; }

    public void setTarget(String target) { this.target = target; }

    public List<Student> getMembers() { return members; }

    public void setMembers(List<Student> members) { this.members = members; }

    @Override
    public String toString() {
        return "StudentOrganization{name='" + name + "', members=" + members.size() + "}";
    }
}
