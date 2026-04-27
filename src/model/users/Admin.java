package model.users;
import model.communication.Request;
import model.enums.Language;
import model.enums.RequestStatus;
public class Admin extends Employee {

    public Admin(String id, String password, String firstName, String lastName,
                 String email, Language language, String department) {
        super(id, password, firstName, lastName, email, language, department);
    }

    public void addUser(User user) {
        System.out.println("Admin added user: " + user.getFirstName());
    }

    public void removeUser(User user) {
        System.out.println("Admin removed user: " + user.getFirstName());
    }

    public void viewSupervisor() {
        System.out.println("Viewing supervisor info...");
    }

    public void acceptRequest(Request request) {
        request.updateStatus(RequestStatus.ACCEPTED);
        System.out.println("Request accepted: " + request.getTopic());
    }

    public void viewLogs() {
        System.out.println("Viewing system logs...");
    }
}
