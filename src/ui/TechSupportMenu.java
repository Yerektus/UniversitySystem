package ui;

import model.users.TechSupportSpecialist;
import storage.DataStorage;

import java.util.Scanner;

public class TechSupportMenu extends BaseMenu {

    private final TechSupportSpecialist techSupport;

    public TechSupportMenu(Scanner scanner, DataStorage storage, TechSupportSpecialist techSupport) {
        super(scanner, storage, techSupport);
        this.techSupport = techSupport;
    }

    @Override
    protected void printMenu() {
        System.out.println("\n--- Tech Support Menu [" + techSupport.getFirstName() + "] ---");
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
