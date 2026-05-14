package storage;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActionLogger {

    private static final String LOG_FILE = "src/data/actions.log";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static ActionLogger instance;

    private ActionLogger() {
        try {
            Files.createDirectories(Paths.get("src/data"));
        } catch (IOException e) {
            System.err.println("ActionLogger: could not create data directory.");
        }
    }

    public static ActionLogger getInstance() {
        if (instance == null) instance = new ActionLogger();
        return instance;
    }

    public void log(String userId, String action) {
        String entry = "[" + LocalDateTime.now().format(FORMATTER) + "] "
                + "User=" + userId + " | " + action;
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(LOG_FILE, true))) {
            writer.write(entry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("ActionLogger: could not write log entry.");
        }
    }

    public String readLogs() {
        try {
            Path path = Paths.get(LOG_FILE);
            if (!Files.exists(path)) return "No log entries yet.";
            return Files.readString(path);
        } catch (IOException e) {
            return "Could not read log file.";
        }
    }
}