package uz.weather.model.main.forecast;

import lombok.Getter;

@Getter
public class Sun {
    protected String Rise;
    protected String Set;

    @Override
    public String toString() {
        return "Sun{" +
                "Rise='" + Rise + '\'' +
                ", Set='" + Set + '\'' +
                '}';
    }
}
