package util;

import storage.DataStorage;

import java.util.Random;

public class UserCredentialGenerator {

    private static final String DOMAIN = "@kbtu.kz";
    private static final Random RANDOM = new Random();

    public static String generateEmail(String firstName, String lastName) {
        String cleanFirst = clean(firstName);
        String cleanLast  = clean(lastName);

        java.util.Set<String> existingEmails = new java.util.HashSet<>();
        for (model.users.User u : DataStorage.getInstance().getUsers().values())
            existingEmails.add(u.getEmail().toLowerCase());

        for (int len = 1; len <= cleanFirst.length(); len++) {
            String prefix = cleanFirst.substring(0, len);
            String candidate = prefix + "_" + cleanLast + DOMAIN;
            if (!existingEmails.contains(candidate.toLowerCase())) {
                return candidate;
            }
        }

        int suffix = 2;
        while (true) {
            String candidate = cleanFirst + "_" + cleanLast + suffix + DOMAIN;
            if (!existingEmails.contains(candidate.toLowerCase())) return candidate;
            suffix++;
        }
    }

    public static String generatePassword() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "_";
        String all = upper + lower + digits + special;

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