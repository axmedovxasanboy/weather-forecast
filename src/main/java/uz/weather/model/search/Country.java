package uz.weather.model.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Country {
    protected String ID;
    protected String EnglishName;

    @Override
    public String toString() {
        return "Country{" +
                "ID='" + ID + '\'' +
                ", EnglishName='" + EnglishName + '\'' +
                '}';
    }
}
