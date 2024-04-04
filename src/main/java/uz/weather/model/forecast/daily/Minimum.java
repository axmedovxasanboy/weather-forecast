package uz.weather.model.forecast.daily;

import lombok.Getter;

@Getter
public class Minimum {
    protected Double Value;
    protected String Unit;

    @Override
    public String toString() {
        return "Minimum{" +
                "Value=" + Value +
                ", Unit='" + Unit + '\'' +
                '}';
    }
}

