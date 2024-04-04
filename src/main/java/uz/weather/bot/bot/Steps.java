package uz.weather.bot.bot;

import org.telegram.telegrambots.meta.api.objects.Contact;

import java.util.HashMap;

public class Steps {

    private static final HashMap<Long, String> steps = new HashMap<>();
    protected static final HashMap<Long, Contact> contacts = new HashMap<>();

    public static String get(Long key) {
        return steps.getOrDefault(key, "main");
    }

    public static void set(Long key, String step) {
        steps.put(key, step);
    }
    public static Contact getContact(Long key) {
        return contacts.getOrDefault(key, null);
    }
    public static void setContact(Long key, Contact contact) {
        contacts.put(key, contact);
    }
}
