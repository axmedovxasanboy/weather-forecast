package uz.weather.model.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import uz.weather.bot.bot.Props;
import uz.weather.model.API;
import uz.weather.model.search.Search;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class WeatherForecast {
    private final String path = "resources/api.properties";
    private final List<String> keys = new ArrayList<>();
    private final Map<API, String> apiLinks = new HashMap<>();
    private final SimpleDateFormat sdf;
    private List<Search> searches = new ArrayList<>();
    @Getter
    private final Gson gson;

    public WeatherForecast() {
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        gson = new GsonBuilder().setDateFormat(sdf.toPattern()).create();
        keys.add(Props.get("key1", path));
        keys.add(Props.get("key2", path));
        keys.add(Props.get("key3", path));
        apiLinks.put(API.SEARCH, Props.get("search.api", path));
        apiLinks.put(API.ONE_HOUR, Props.get("one.hour.api", path));
        apiLinks.put(API.TWELVE_HOUR, Props.get("twelve.hour.api", path));
        apiLinks.put(API.ONE_DAY, Props.get("one.day.api", path));
        apiLinks.put(API.FIVE_DAY, Props.get("five.day.api", path));
    }

    public List<Search> searchList(String cityName) {
        Type type = TypeToken.getParameterized(List.class, Search.class).getType();

        String urlSearch = apiLinks.get(API.SEARCH);
        String apiKey = keys.getFirst();
        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlSearch + "?apikey=" + apiKey + "&q=" + cityName))
                    .GET()
                    .build();
            HttpResponse<String> send = client.send(request, HttpResponse.BodyHandlers.ofString());
            searches = gson.fromJson(send.body(), type);
            return searches;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<? extends Forecast> sendForecasts(Integer cityKey, Boolean isDaily) {
        Type type;
        String urlDaily;
        String apiKey;
        if (isDaily) {
            urlDaily = apiLinks.get(API.FIVE_DAY);
            apiKey = keys.get(1);
            type = TypeToken.getParameterized(List.class, DailyForecasts.class).getType();
        } else {
            urlDaily = apiLinks.get(API.TWELVE_HOUR);
            apiKey = keys.get(2);
            type = TypeToken.getParameterized(List.class, HourlyForecasts.class).getType();
        }
        return getForecast(cityKey, urlDaily, apiKey, type, isDaily);
    }

    private List<DailyForecasts> getForecast(Integer cityKey, String url, String apiKey, Type type, Boolean isDaily) {
        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url + cityKey + "?apikey=" + apiKey + "&details=true"))
                    .GET()
                    .build();
            HttpResponse<String> send = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json;
            if (isDaily) {
                json = send.body().substring(send.body().indexOf("DailyForecasts") + "DailyForecasts".length() + 2, send.body().length() - 1);
                System.out.println("json = " + json);
            } else {
                json = send.body();
            }

            return gson.fromJson(json, type);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
