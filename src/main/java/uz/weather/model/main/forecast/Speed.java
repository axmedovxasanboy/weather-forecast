package uz.weather.model.main.forecast;

import lombok.Getter;

@Getter
public class Speed {
    protected Double Value;
    protected String Unit;

    @Override
    public String toString() {
        return "Speed{" +
                "Value=" + Value +
                ", Unit='" + Unit + '\'' +
                '}';
    }
}
