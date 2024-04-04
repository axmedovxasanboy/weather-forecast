package uz.weather.model.search;

import lombok.Getter;

@Getter
public class AdministrativeArea {
    protected String EnglishName;
    protected String EnglishType;

    @Override
    public String toString() {
        return "AdministrativeArea{" +
                ", EnglishName='" + EnglishName + '\'' +
                ", EnglishType='" + EnglishType + '\'' +
                '}';
    }
}
