package uz.weather.model.main;

import lombok.Getter;
import uz.weather.model.main.forecast.*;

@Getter
public class DailyForecasts extends Forecast {
    protected String Date;
    protected Sun Sun;
    protected Temperature Temperature;
    protected RealFeelTemperature RealFeelTemperature;
    protected Day Day;
    protected Night Night;

    @Override
    public String toString() {
        return "DailyForecasts={" +
                "Date='" + Date + '\'' +
                ", Sun=" + Sun +
                ", Temperature=" + Temperature +
                ", RealFeelTemperature=" + RealFeelTemperature +
                ", Day=" + Day +
                ", Night=" + Night +
                '}';
    }
}
