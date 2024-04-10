package uz.weather.model.main.forecast;

import lombok.Getter;

@Getter
public class Temperature {
    protected Double Value;
    protected Minimum Minimum;
    protected Maximum Maximum;

    @Override
    public String toString() {
        return "Temperature{" +
                "Value=" + Value +
                "Minimum=" + Minimum +
                ", Maximum=" + Maximum +
                '}';
    }
}
