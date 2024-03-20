package uz.weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import uz.weather.model.search.Search;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        String json = "[{\"Version\":1,\"Key\":\"355776\",\"Type\":\"City\",\"Rank\":31,\"LocalizedName\":\"Samarkand\",\"EnglishName\":\"Samarkand\",\"PrimaryPostalCode\":\"\",\"Region\":{\"ID\":\"ASI\",\"LocalizedName\":\"Asia\",\"EnglishName\":\"Asia\"},\"Country\":{\"ID\":\"UZ\",\"LocalizedName\":\"Uzbekistan\",\"EnglishName\":\"Uzbekistan\"},\"AdministrativeArea\":{\"ID\":\"SA\",\"LocalizedName\":\"Samarkand\",\"EnglishName\":\"Samarkand\",\"Level\":1,\"LocalizedType\":\"Province\",\"EnglishType\":\"Province\",\"CountryID\":\"UZ\"},\"TimeZone\":{\"Code\":\"UZT\",\"Name\":\"Asia/Samarkand\",\"GmtOffset\":5.0,\"IsDaylightSaving\":false,\"NextOffsetChange\":null},\"GeoPosition\":{\"Latitude\":39.667,\"Longitude\":66.967,\"Elevation\":{\"Metric\":{\"Value\":676.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":2217.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"SupplementalAdminAreas\":[],\"DataSets\":[\"AirQualityCurrentConditions\",\"AirQualityForecasts\",\"ForecastConfidence\",\"FutureRadar\",\"MinuteCast\"]},{\"Version\":1,\"Key\":\"2429253\",\"Type\":\"City\",\"Rank\":85,\"LocalizedName\":\"Samarkand\",\"EnglishName\":\"Samarkand\",\"PrimaryPostalCode\":\"\",\"Region\":{\"ID\":\"ASI\",\"LocalizedName\":\"Asia\",\"EnglishName\":\"Asia\"},\"Country\":{\"ID\":\"RU\",\"LocalizedName\":\"Russia\",\"EnglishName\":\"Russia\"},\"AdministrativeArea\":{\"ID\":\"TA\",\"LocalizedName\":\"Tatarstan\",\"EnglishName\":\"Tatarstan\",\"Level\":1,\"LocalizedType\":\"Republic\",\"EnglishType\":\"Republic\",\"CountryID\":\"RU\"},\"TimeZone\":{\"Code\":\"MSK\",\"Name\":\"Europe/Moscow\",\"GmtOffset\":3.0,\"IsDaylightSaving\":false,\"NextOffsetChange\":null},\"GeoPosition\":{\"Latitude\":54.796,\"Longitude\":52.011,\"Elevation\":{\"Metric\":{\"Value\":222.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":728.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"SupplementalAdminAreas\":[{\"Level\":2,\"LocalizedName\":\"Almetyevsky\",\"EnglishName\":\"Almetyevsky\"}],\"DataSets\":[\"AirQualityCurrentConditions\",\"AirQualityForecasts\",\"Alerts\",\"ForecastConfidence\",\"FutureRadar\",\"MinuteCast\"]},{\"Version\":1,\"Key\":\"799814\",\"Type\":\"City\",\"Rank\":600,\"LocalizedName\":\"Samarkand\",\"EnglishName\":\"Samarkand\",\"PrimaryPostalCode\":\"\",\"Region\":{\"ID\":\"ASI\",\"LocalizedName\":\"Asia\",\"EnglishName\":\"Asia\"},\"Country\":{\"ID\":\"KZ\",\"LocalizedName\":\"Kazakhstan\",\"EnglishName\":\"Kazakhstan\"},\"AdministrativeArea\":{\"ID\":\"KAR\",\"LocalizedName\":\"Karaganda Region\",\"EnglishName\":\"Karaganda Region\",\"Level\":1,\"LocalizedType\":\"Region\",\"EnglishType\":\"Region\",\"CountryID\":\"KZ\"},\"TimeZone\":{\"Code\":\"AQTT\",\"Name\":\"Asia/Almaty\",\"GmtOffset\":5.0,\"IsDaylightSaving\":false,\"NextOffsetChange\":null},\"GeoPosition\":{\"Latitude\":50.1,\"Longitude\":72.833,\"Elevation\":{\"Metric\":{\"Value\":482.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":1580.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"SupplementalAdminAreas\":[],\"DataSets\":[\"AirQualityCurrentConditions\",\"AirQualityForecasts\",\"ForecastConfidence\",\"FutureRadar\",\"MinuteCast\"]},{\"Version\":1,\"Key\":\"720167\",\"Type\":\"City\",\"Rank\":600,\"LocalizedName\":\"Samarkand\",\"EnglishName\":\"Samarkand\",\"PrimaryPostalCode\":\"\",\"Region\":{\"ID\":\"ASI\",\"LocalizedName\":\"Asia\",\"EnglishName\":\"Asia\"},\"Country\":{\"ID\":\"UZ\",\"LocalizedName\":\"Uzbekistan\",\"EnglishName\":\"Uzbekistan\"},\"AdministrativeArea\":{\"ID\":\"SU\",\"LocalizedName\":\"Surkhandarya\",\"EnglishName\":\"Surkhandarya\",\"Level\":1,\"LocalizedType\":\"Province\",\"EnglishType\":\"Province\",\"CountryID\":\"UZ\"},\"TimeZone\":{\"Code\":\"UZT\",\"Name\":\"Asia/Samarkand\",\"GmtOffset\":5.0,\"IsDaylightSaving\":false,\"NextOffsetChange\":null},\"GeoPosition\":{\"Latitude\":38.583,\"Longitude\":68.05,\"Elevation\":{\"Metric\":{\"Value\":1088.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":3571.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"SupplementalAdminAreas\":[],\"DataSets\":[\"AirQualityCurrentConditions\",\"AirQualityForecasts\",\"ForecastConfidence\",\"FutureRadar\",\"MinuteCast\"]}]\n";
        String apiKey = "vTEkGqTFSilngfZzDvVtyGIlyfn9GD0D";
        Scanner scanner = new Scanner(System.in);
        String url = "http://dataservice.accuweather.com/locations/v1/cities/search";
        HttpClient client = HttpClient.newBuilder().build();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        HttpRequest request = null;
        request = HttpRequest.newBuilder()
                .uri(new URI(url + "?apikey=" + apiKey + "&q=" + name))
                .GET()
                .build();
        HttpResponse<String> send = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("send = " + send);
        System.out.println("send.body() = " + send.body());

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Type type = TypeToken.getParameterized(List.class, Search.class).getType();

        Gson gson = new GsonBuilder().setDateFormat(sdf.toPattern()).create();
        List<Search> resp = gson.fromJson(send.body(), type);

        resp.forEach(o -> System.out.println(o.getLocalizedName() + " >> " + o.getKey()));

    }
}