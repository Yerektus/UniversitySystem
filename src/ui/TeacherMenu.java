package ui;

import model.users.Teacher;
import storage.DataStorage;

import java.util.Scanner;

public class TeacherMenu extends BaseMenu {

    private final Teacher teacher;

    public TeacherMenu(Scanner scanner, DataStorage storage, Teacher teacher) {
        super(scanner, storage, teacher);
        this.teacher = teacher;
    }

    @Override
    protected void printMenu() {
        System.out.println("\n--- Teacher Menu [" + teacher.getFirstName() + "] ---");
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
