package uz.weather.model.main.forecast;

import lombok.Getter;

@Getter
public class Minimum {
    protected Double Value;
    protected String Unit;
    protected String Phrase;

    @Override
    public String toString() {
        return "Minimum{" +
                "Value=" + Value +
                ", Unit='" + Unit + '\'' +
                ", Phrase='" + Phrase + '\'' +
                '}';
    }
}

