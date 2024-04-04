package uz.weather.model.forecast.daily;


import lombok.Getter;

@Getter
public class Night {
    protected Boolean HasPrecipitation;
    protected String PrecipitationType;
    protected String PrecipitationIntensity;
    protected String ShortPhrase;
    protected String LongPhrase;
    protected Wind Wind;

    @Override
    public String toString() {
        return "Night{" +
                "HasPrecipitation=" + HasPrecipitation +
                ", PrecipitationType='" + PrecipitationType + '\'' +
                ", PrecipitationIntensity='" + PrecipitationIntensity + '\'' +
                ", ShortPhrase='" + ShortPhrase + '\'' +
                ", LongPhrase='" + LongPhrase + '\'' +
                ", Wind=" + this.Wind +
                '}';
    }
}
