package uz.weather.model.main.forecast;

import lombok.Getter;

@Getter
public class Day {
    protected Boolean HasPrecipitation;
    protected String PrecipitationType;
    protected String PrecipitationIntensity;
    protected String PrecipitationProbability;
    protected String ShortPhrase;
    protected String LongPhrase;
    protected Wind Wind;

    @Override
    public String toString() {
        return "Day{" +
                "HasPrecipitation=" + HasPrecipitation +
                ", PrecipitationType='" + PrecipitationType + '\'' +
                ", PrecipitationIntensity='" + PrecipitationIntensity + '\'' +
                ", PrecipitationProbability='" + PrecipitationProbability + '\'' +
                ", ShortPhrase='" + ShortPhrase + '\'' +
                ", LongPhrase='" + LongPhrase + '\'' +
                ", Wind=" + this.Wind +
                '}';
    }
}
