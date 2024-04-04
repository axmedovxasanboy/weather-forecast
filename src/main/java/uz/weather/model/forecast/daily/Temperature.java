package uz.weather.model.forecast.daily;

import lombok.Getter;

@Getter
public class Temperature {
    protected Minimum Minimum;
    protected Maximum Maximum;

    @Override
    public String toString() {
        return "Temperature{" +
                "Minimum=" + Minimum +
                ", Maximum=" + Maximum +
                '}';
    }
}
