package uz.weather.model.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Region {
    protected String ID;
    protected String LocalizedName;
    protected String EnglishName;

    @Override
    public String toString() {
        return "Region{" +
                "ID='" + ID + '\'' +
                ", LocalizedName='" + LocalizedName + '\'' +
                ", EnglishName='" + EnglishName + '\'' +
                '}';
    }
}
