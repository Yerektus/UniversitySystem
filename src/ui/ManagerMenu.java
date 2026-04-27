package ui;

import model.users.Manager;
import storage.DataStorage;

import java.util.Scanner;

public class ManagerMenu extends BaseMenu {

    private final Manager manager;

    public ManagerMenu(Scanner scanner, DataStorage storage, Manager manager) {
        super(scanner, storage, manager);
        this.manager = manager;
    }

    @Override
    protected void printMenu() {
        System.out.println("\n--- Manager Menu [" + manager.getFirstName() + "] ---");
        System.out.println("0. Logout");
    }

    @Override
    protected void handleChoice(String choice) {
        switch (choice) {
            case "0": logout(); break;
            default: System.out.println("This menu is not yet implemented.");
        }
    }
}
