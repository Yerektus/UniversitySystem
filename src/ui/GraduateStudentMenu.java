package ui;

import model.users.GraduateStudent;
import storage.DataStorage;

import java.util.Scanner;

public class GraduateStudentMenu extends StudentMenu {

    private final GraduateStudent gradStudent;

    public GraduateStudentMenu(Scanner scanner, DataStorage storage, GraduateStudent gradStudent) {
        super(scanner, storage, gradStudent);
        this.gradStudent = gradStudent;
    }

    @Override
    protected void printMenu() {
        System.out.println("\n--- Graduate Student Menu [" + gradStudent.getFirstName() + "] ---");
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
