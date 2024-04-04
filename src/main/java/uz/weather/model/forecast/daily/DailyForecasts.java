package uz.weather.model.forecast.daily;

import lombok.Getter;

@Getter
public class DailyForecasts {
    protected String Date;
    protected Sun Sun;
    protected Temperature Temperature;
    protected RealFeelTemperature RealFeelTemperature;
    protected Day Day;
    protected Night Night;

    @Override
    public String toString() {
        return "DailyForecasts{" +
                "Date='" + Date + '\'' +
                ", Sun=" + Sun +
                ", Temperature=" + Temperature +
                ", RealFeelTemperature=" + RealFeelTemperature +
                ", Day=" + Day +
                ", Night=" + Night +
                '}';
    }
}
