package uz.weather.model.dailyforecasts;

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
