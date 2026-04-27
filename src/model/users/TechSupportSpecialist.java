package model.users;

import model.communication.Request;
import model.enums.Language;
import model.enums.RequestStatus;

public class TechSupportSpecialist extends Employee {

    public TechSupportSpecialist(String id, String password, String firstName, String lastName,
                                  String email, Language language, String department) {
        super(id, password, firstName, lastName, email, language, department);
    }

    public void viewNewRequests() {
        System.out.println("Viewing new requests...");
    }

    public void viewSupervisor() {
        System.out.println("Viewing supervisor info...");
    }

    public void acceptRequest(Request request) {
        request.updateStatus(RequestStatus.ACCEPTED);
        System.out.println("Request accepted by tech support: " + request.getTopic());
    }

    public void markRequestAsDone(Request request) {
        request.updateStatus(RequestStatus.DONE);
        System.out.println("Request marked as done: " + request.getTopic());
    }
}
