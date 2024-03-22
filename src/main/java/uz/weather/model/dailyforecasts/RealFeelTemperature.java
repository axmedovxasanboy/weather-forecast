package uz.weather.model.dailyforecasts;

import lombok.Getter;

@Getter
public class RealFeelTemperature {
    protected Minimum Minimum;
    protected Maximum Maximum;

    @Override
    public String toString() {
        return "RealFeelTemperature{" +
                "Minimum=" + Minimum +
                ", Maximum=" + Maximum +
                '}';
    }
}
