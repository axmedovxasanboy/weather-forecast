package uz.weather.model.search;

import lombok.Getter;

@Getter
public class Search {
    protected Integer Version;
    protected String Key;
    protected String Type;
    protected String Rank;
    protected String LocalizedName;
    protected String EnglishName;
    protected String PrimaryPostalCode;
    protected Region Region;
    protected Country Country;
    protected AdministrativeArea AdministrativeArea;

    @Override
    public String toString() {
        return "Search{" +
                "Version=" + Version +
                ", Key='" + Key + '\'' +
                ", Type='" + Type + '\'' +
                ", Rank='" + Rank + '\'' +
                ", LocalizedName='" + LocalizedName + '\'' +
                ", EnglishName='" + EnglishName + '\'' +
                ", PrimaryPostalCode='" + PrimaryPostalCode + '\'' +
                ", Region=" + Region +
                ", Country=" + Country +
                ", AdminstrativeArea=" + AdministrativeArea +
                '}';
    }
}
