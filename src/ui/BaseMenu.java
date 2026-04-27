package ui;

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

    protected int parseIntSafe(String input, int defaultVal) {
        try { return Integer.parseInt(input); }
        catch (NumberFormatException e) { return defaultVal; }
    }

    protected double parseDoubleSafe(String input, double defaultVal) {
        try { return Double.parseDouble(input); }
        catch (NumberFormatException e) { return defaultVal; }
    }

    protected void pause() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
