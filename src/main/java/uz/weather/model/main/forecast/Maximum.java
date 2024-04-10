package uz.weather.model.main.forecast;

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
