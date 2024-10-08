package uz.weather.bot.bot;

import org.telegram.telegrambots.meta.api.objects.Contact;
import uz.weather.Instances;
import uz.weather.model.main.DailyForecasts;
import uz.weather.model.main.Forecast;
import uz.weather.model.main.WeatherForecast;

import java.util.HashMap;
import java.util.List;

public class Steps {
    private static final HashMap<Long, String> steps = new HashMap<>();
    protected static final HashMap<Long, Contact> contacts = new HashMap<>();

    public static String get(Long key) {
        return steps.getOrDefault(key, "main");
    }

//    public static List<? extends Forecast> getForecasts(Long chatId, Integer cityKey) {
//        return forecasts.getOrDefault(chatId, null).getOrDefault(cityKey, null);
//    }
//
//    public static void setForecasts(Long chatId, Integer cityKey, List<? extends Forecast> weather) {
//        HashMap<Integer, List<? extends Forecast>> cityForecasts = new HashMap<>();
//        cityForecasts.put(cityKey, weather);
//        forecasts.put(chatId, cityForecasts);
//    }

    public static void set(Long chatId, String step) {
        steps.put(chatId, step);
    }

    public static Contact getContact(Long chatId) {
        return contacts.getOrDefault(chatId, null);
    }

    public static void setContact(Long chatId, Contact contact) {
        contacts.put(chatId, contact);
    }
//
//    public static List<DailyForecasts> getDailyForecast(Long chatId, Integer cityKey, Boolean isNew) {
//        if (isNew){
//
//        }
//        return null;
//    }
}
