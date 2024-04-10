package uz.weather;

import lombok.Getter;
import lombok.Setter;
import uz.weather.model.main.WeatherForecast;

@Getter
@Setter
public class Instances {
    public static WeatherForecast forecast = new WeatherForecast();
}
