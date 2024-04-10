package uz.weather.model.main.forecast;

import lombok.Getter;

@Getter
public class RealFeelTemperature {
    protected Double Value;
    protected Minimum Minimum;
    protected Maximum Maximum;
    protected String Phrase;

    @Override
    public String toString() {
        return "RealFeelTemperature{" +
                "Value=" + Value +
                ", Minimum=" + Minimum +
                ", Maximum=" + Maximum +
                ", Phrase='" + Phrase + '\'' +
                '}';
    }
}
