package uz.weather.model.headline;

import lombok.Getter;

@Getter
public class Headline {
    protected String EffectiveDate;
    protected Integer Severity;
    protected String Text;
    protected String Category;
    protected String EndDate;

    @Override
    public String toString() {
        return "Headline{" +
                "EffectiveDate='" + EffectiveDate + '\'' +
                ", Severity=" + Severity +
                ", Text='" + Text + '\'' +
                ", Category='" + Category + '\'' +
                ", EndDate='" + EndDate + '\'' +
                '}';
    }
}
