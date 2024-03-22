package uz.weather.model.search;

import lombok.Getter;

@Getter
public class AdministrativeArea {
    protected String ID;
    protected String LocalizedName;
    protected String EnglishName;
    protected String LocalizedType;
    protected String EnglishType;

    @Override
    public String toString() {
        return "AdministrativeArea{" +
                "ID='" + ID + '\'' +
                ", LocalizedName='" + LocalizedName + '\'' +
                ", EnglishName='" + EnglishName + '\'' +
                ", LocalizedType='" + LocalizedType + '\'' +
                ", EnglishType='" + EnglishType + '\'' +
                '}';
    }
}
