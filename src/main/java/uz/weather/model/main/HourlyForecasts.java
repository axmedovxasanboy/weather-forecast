package uz.weather.model.main;

import lombok.Getter;
import uz.weather.model.main.forecast.RealFeelTemperature;
import uz.weather.model.main.forecast.Temperature;
import uz.weather.model.main.forecast.Wind;

@Getter
public class HourlyForecasts extends Forecast {
    protected String DateTime;
    protected Boolean HasPrecipitation;
    protected String PrecipitationType;
    protected String PrecipitationIntensity;
    protected Temperature Temperature;
    protected RealFeelTemperature RealFeelTemperature;
    protected Wind Wind;
    protected Integer PrecipitationProbability;

    @Override
    public String toString() {
        return "HourlyForecasts={" +
                "DateTime='" + DateTime + '\'' +
                ", HasPrecipitation=" + HasPrecipitation +
                ", PrecipitationType='" + PrecipitationType + '\'' +
                ", PrecipitationIntensity='" + PrecipitationIntensity + '\'' +
                ", Temperature=" + Temperature +
                ", RealFeelTemperature=" + RealFeelTemperature +
                ", Wind=" + Wind +
                ", PrecipitationProbability=" + PrecipitationProbability +
                '}';
    }
}
