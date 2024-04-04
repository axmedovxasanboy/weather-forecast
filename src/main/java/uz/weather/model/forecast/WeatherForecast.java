package uz.weather.model.forecast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import uz.weather.bot.bot.Props;
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
    private Gson gson;
    private Type type;

    public WeatherForecast() {
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        gson = new GsonBuilder().setDateFormat(sdf.toPattern()).create();
        keys.add(Props.get("key1", path));
        keys.add(Props.get("key2", path));
        keys.add(Props.get("key3", path));
        apiLinks.put(API.SEARCH, Props.get("search.api", path));
        apiLinks.put(API.ONE_HOUR_API, Props.get("one.hour.api", path));
        apiLinks.put(API.TWELVE_HOUR_API, Props.get("twelve.hour.api", path));
        apiLinks.put(API.ONE_DAY_API, Props.get("one.day.api", path));
        apiLinks.put(API.FIVE_DAY_API, Props.get("five.day.api", path));
    }

    public List<Search> searchCity(String cityName) {
        type = TypeToken.getParameterized(List.class, Search.class).getType();
        String urlSearch = apiLinks.get(API.SEARCH);
        String apiKey = keys.get(1);
        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlSearch + "?apikey=" + apiKey + "&q=" + cityName))
                    .GET()
                    .build();
            HttpResponse<String> send = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("send.body() = " + send.body());
            String json = send.body();
            return gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }
}
