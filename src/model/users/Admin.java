package model.users;

import model.communication.Request;
import model.enums.Language;
import model.enums.RequestStatus;
import storage.ActionLogger;

public class Admin extends Employee {


	public Admin(String id, String password, String firstName, String lastName,
                 String email, Language language, String department) {
        super(id, password, firstName, lastName, email, language, department);
    }

    public void addUser(User user) {
        System.out.println("Admin added user: " + user.getFirstName());
        ActionLogger.getInstance().log(getId(), "ADD_USER: " + user.getEmail());
    }

    public void removeUser(User user) {
        System.out.println("Admin removed user: " + user.getFirstName());
        ActionLogger.getInstance().log(getId(), "REMOVE_USER: " + user.getEmail());
    }

    public void viewSupervisor() {
        System.out.println("Viewing supervisor info...");
    }

    public void acceptRequest(Request request) {
        request.updateStatus(RequestStatus.ACCEPTED);
        System.out.println("Request accepted: " + request.getTopic());
        ActionLogger.getInstance().log(getId(), "ACCEPT_REQUEST: " + request.getTopic());
    }

    public String viewLogs() {
        return ActionLogger.getInstance().readLogs();
    }
}