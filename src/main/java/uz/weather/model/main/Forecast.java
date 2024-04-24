package uz.weather.model.main;

import uz.weather.Instances;

import java.util.HashMap;
import java.util.List;

public class Forecast {
    protected static final HashMap<Long, Integer> cityKeys = new HashMap<>();
    protected static final HashMap<Long, HashMap<Integer, List<DailyForecasts>>> dailyForecasts = new HashMap<>();
    protected static final HashMap<Long, HashMap<Integer, List<DailyForecasts>>> hourlyForecasts = new HashMap<>();

    public static Integer getCityKey(long chatId) {
        return cityKeys.get(chatId);
    }

    public static void setCityKey(Long chatId, Integer cityKey) {
        cityKeys.put(chatId, cityKey);
    }





}
