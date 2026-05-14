package util;

import model.enums.SchoolCode;
import storage.DataStorage;

import java.time.Year;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class UserCredentialGenerator {

    private static final String DOMAIN = "@kbtu.kz";
    private static final Random RANDOM = new Random();

    public static String generateStudentId(String degreeType, SchoolCode school) {
        String yy = String.valueOf(Year.now().getValue()).substring(2);
        String prefix = yy + degreeType + school.getCode();
        return prefix + generateSequential(prefix);
    }

    public static String generateTeacherId(SchoolCode school) {
        String prefix = "T" + school.getCode();
        return prefix + generateSequential(prefix);
    }

    public static String generateManagerId(SchoolCode school) {
        String prefix = "MG" + school.getCode();
        return prefix + generateSequential(prefix);
    }

    public static String generateAdminId() {
        String prefix = "AD";
        return prefix + generateSequential(prefix);
    }

    public static String generateTechSupportId() {
        String prefix = "TS";
        return prefix + generateSequential(prefix);
    }

    private static String generateSequential(String prefix) {
        Set<String> existingIds = new HashSet<>();
        for (model.users.User u : DataStorage.getInstance().getUsers().values())
            existingIds.add(u.getId());

        for (int n = 1; n <= 9999; n++) {
            String candidate = prefix + String.format("%04d", n);
            if (!existingIds.contains(candidate)) return String.format("%04d", n);
        }
        return String.valueOf(System.currentTimeMillis() % 10000);
    }

    public static String generateEmail(String firstName, String lastName) {
        String cleanFirst = clean(firstName);
        String cleanLast  = clean(lastName);

        Set<String> existingEmails = new HashSet<>();
        for (model.users.User u : DataStorage.getInstance().getUsers().values())
            existingEmails.add(u.getEmail().toLowerCase());

        for (int len = 1; len <= cleanFirst.length(); len++) {
            String prefix = cleanFirst.substring(0, len);
            String candidate = prefix + "_" + cleanLast + DOMAIN;
            if (!existingEmails.contains(candidate.toLowerCase())) return candidate;
        }

        int suffix = 2;
        while (true) {
            String candidate = cleanFirst + "_" + cleanLast + suffix + DOMAIN;
            if (!existingEmails.contains(candidate.toLowerCase())) return candidate;
            suffix++;
        }
    }

    public static String generatePassword() {
        String upper   = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower   = "abcdefghijklmnopqrstuvwxyz";
        String digits  = "0123456789";
        String special = "_";
        String all     = upper + lower + digits + special;

        char[] password = new char[8];
        password[0] = upper.charAt(RANDOM.nextInt(upper.length()));
        password[1] = lower.charAt(RANDOM.nextInt(lower.length()));
        password[2] = digits.charAt(RANDOM.nextInt(digits.length()));
        password[3] = special.charAt(RANDOM.nextInt(special.length()));
        for (int i = 4; i < 8; i++)
            password[i] = all.charAt(RANDOM.nextInt(all.length()));

        for (int i = 7; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char tmp = password[i]; password[i] = password[j]; password[j] = tmp;
        }
        return new String(password);
    }

    private static String clean(String s) {
        return s.replaceAll("[^a-zA-Z]", "").toLowerCase();
    }
}