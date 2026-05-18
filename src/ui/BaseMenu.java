package ui;

import model.enums.Language;
import model.users.User;
import storage.DataStorage;

import java.util.Scanner;

public abstract class BaseMenu {

    protected final Scanner scanner;
    protected final DataStorage storage;
    protected final User currentUser;
    protected boolean running = true;

    public BaseMenu(Scanner scanner, DataStorage storage, User currentUser) {
        this.scanner = scanner;
        this.storage = storage;
        this.currentUser = currentUser;
    }

    public void show() {
        while (running) {
            printMenu();
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();
            handleChoice(choice);
        }
    }

    protected abstract void printMenu();

    protected abstract void handleChoice(String choice);

    protected void logout() {
        currentUser.logout();
        System.out.println("You have been logged out.");
        running = false;
    }

    protected void changeLanguage() {
        System.out.println("\n--- Change Language ---");
        System.out.println("Current: " + currentUser.getLanguage());
        System.out.println("1. KZ");
        System.out.println("2. EN");
        System.out.println("3. RU");
        System.out.println("0. Cancel");
        System.out.print("Choice: ");
        switch (scanner.nextLine().trim()) {
            case "1": currentUser.setLanguage(Language.KZ); break;
            case "2": currentUser.setLanguage(Language.EN); break;
            case "3": currentUser.setLanguage(Language.RU); break;
            case "0": return;
            default: System.out.println("Invalid choice."); return;
        }
        storage.updateAndSave();
        System.out.println("Language changed to: " + currentUser.getLanguage());
    }

    protected int parseIntSafe(String input, int defaultVal) {
        try { return Integer.parseInt(input); }
        catch (NumberFormatException e) { return defaultVal; }
    }

    protected double parseDoubleSafe(String input, double defaultVal) {
        try { return Double.parseDouble(input); }
        catch (NumberFormatException e) { return defaultVal; }
    }
}