package uz.weather.model.forecast.daily;

import lombok.Getter;

@Getter
public class Maximum {
    protected Double Value;
    protected String Unit;
    protected String Phrase;

    @Override
    public String toString() {
        return "Maximum{" +
                "Value=" + Value +
                ", Unit='" + Unit + '\'' +
                ", Phrase='" + Phrase + '\'' +
                '}';
    }
}
