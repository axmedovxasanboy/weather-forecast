package uz.weather.model.search;

import lombok.Getter;

@Getter
public class Search {
    protected String LocalizedName;
    protected Integer Key;
    protected String EnglishName;
    protected Region Region;
    protected Country Country;
    protected AdministrativeArea AdministrativeArea;

    @Override
    public String toString() {
        return "Search{" +
                "LocalizedName='" + LocalizedName + '\'' +
                ", Key=" + Key + '\'' +
                ", EnglishName='" + EnglishName + '\'' +
                ", Region=" + Region +
                ", Country=" + Country +
                ", AdministrativeArea=" + AdministrativeArea +
                '}';
    }
}
