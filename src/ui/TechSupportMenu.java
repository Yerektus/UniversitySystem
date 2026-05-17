package ui;

import model.communication.Request;
import model.enums.RequestStatus;
import model.users.TechSupportSpecialist;
import storage.DataStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TechSupportMenu extends BaseMenu {

    private final TechSupportSpecialist techSupport;

    public TechSupportMenu(Scanner scanner, DataStorage storage, TechSupportSpecialist techSupport) {
        super(scanner, storage, techSupport);
        this.techSupport = techSupport;
    }

    @Override
    protected void printMenu() {
        long newCount = storage.getRequests().stream()
                .filter(r -> r.getStatus() == RequestStatus.NEW).count();
        System.out.println("\n--- Tech Support Menu [" + techSupport.getFirstName()
                + " " + techSupport.getLastName() + "] ---");
        System.out.println("1. View new requests" + (newCount > 0 ? " (" + newCount + " new)" : ""));
        System.out.println("2. View all requests");
        System.out.println("3. Process a request");
        System.out.println("4. Submit a request");
        System.out.println("0. Logout");
    }

    @Override
    protected void handleChoice(String choice) {
        switch (choice) {
            case "1": viewNewRequests();  break;
            case "2": viewAllRequests();  break;
            case "3": processRequest();   break;
            case "4": submitRequest();    break;
            case "0": logout();           break;
            default:  System.out.println("Invalid choice.");
        }
    }

    private void viewNewRequests() {
        List<Request> newRequests = new ArrayList<>();
        for (Request r : storage.getRequests())
            if (r.getStatus() == RequestStatus.NEW) newRequests.add(r);

        if (newRequests.isEmpty()) {
            System.out.println("No new requests.");

            return;
        }

        System.out.println("\n--- New Requests ---");
        for (Request r : newRequests) {
            r.updateStatus(RequestStatus.VIEWED);
            printRequest(r);
        }
        storage.updateAndSave();
    }

    private void viewAllRequests() {
        List<Request> all = storage.getRequests();
        if (all.isEmpty()) {
            System.out.println("No requests found.");

            return;
        }

        System.out.println("\n--- All Requests ---");
        System.out.println("Filter by: 1. All  2. NEW  3. VIEWED  4. ACCEPTED  5. REJECTED  6. DONE");
        System.out.print("Choice: ");
        String filter = scanner.nextLine().trim();

        RequestStatus filterStatus = null;
        switch (filter) {
            case "2": filterStatus = RequestStatus.NEW;      break;
            case "3": filterStatus = RequestStatus.VIEWED;   break;
            case "4": filterStatus = RequestStatus.ACCEPTED; break;
            case "5": filterStatus = RequestStatus.REJECTED; break;
            case "6": filterStatus = RequestStatus.DONE;     break;
        }

        int count = 0;
        for (Request r : all) {
            if (filterStatus == null || r.getStatus() == filterStatus) {
                printRequest(r);
                count++;
            }
        }
        if (count == 0) System.out.println("No requests with that status.");
    }

    private void processRequest() {
        List<Request> actionable = new ArrayList<>();
        for (Request r : storage.getRequests())
            if (r.getStatus() == RequestStatus.VIEWED
                    || r.getStatus() == RequestStatus.ACCEPTED
                    || r.getStatus() == RequestStatus.NEW)
                actionable.add(r);

        if (actionable.isEmpty()) {
            System.out.println("No requests to process.");
            return;
        }

        System.out.println("\n--- Select Request ---");
        for (int i = 0; i < actionable.size(); i++) {
            Request r = actionable.get(i);
            System.out.println((i + 1) + ". [" + r.getStatus() + "] "
                    + r.getTopic() + " — " + r.getSender().getFirstName()
                    + " " + r.getSender().getLastName());
        }
        System.out.print("Choice: ");
        int idx = parseIntSafe(scanner.nextLine().trim(), 0) - 1;
        if (idx < 0 || idx >= actionable.size()) { System.out.println("Invalid."); return; }
        Request request = actionable.get(idx);

        System.out.println("\nRequest: " + request.getTopic());
        System.out.println("From   : " + request.getSender().getFirstName()
                + " " + request.getSender().getLastName());
        System.out.println("Detail : " + request.getDescription());
        System.out.println("Status : " + request.getStatus());
        System.out.println("\nAction: 1. ACCEPT  2. REJECT  3. Mark as DONE");
        System.out.print("Choice: ");

        switch (scanner.nextLine().trim()) {
            case "1":
                request.updateStatus(RequestStatus.ACCEPTED);
                break;
            case "2":
                request.updateStatus(RequestStatus.REJECTED);
                break;
            case "3":
                request.updateStatus(RequestStatus.DONE);
                break;
            default:
                System.out.println("Invalid action.");
                return;
        }
        storage.updateAndSave();
    }

    private void submitRequest() {
        System.out.println("\n--- Submit Tech Support Request ---");
        System.out.print("Topic (e.g. Fix projector, Fix printer): ");
        String topic = scanner.nextLine().trim();
        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        String id = "REQ" + System.currentTimeMillis();
        Request request = new Request(id, topic, techSupport, description);
        storage.saveRequest(request);
        System.out.println("Request submitted: " + topic);
    }

    private void printRequest(Request r) {
        System.out.println("ID     : " + r.getRequestId());
        System.out.println("Topic  : " + r.getTopic());
        System.out.println("From   : " + r.getSender().getFirstName()
                + " " + r.getSender().getLastName());
        System.out.println("Detail : " + r.getDescription());
        System.out.println("Status : " + r.getStatus());
        System.out.println("Date   : " + r.getCreatedAt());
    }
}