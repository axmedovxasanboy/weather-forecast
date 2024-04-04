package uz.weather.model.forecast.daily;

import lombok.Getter;

@Getter
public class Wind {
    protected Speed Speed;

    @Override
    public String toString() {
        return "Wind{" +
                "Speed=" + Speed +
                '}';
    }
}
