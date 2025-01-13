package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class WeatherDetails {
    @Getter @Setter private String cityName;
    @Getter @Setter private String description;
    @Getter @Setter private String temp;
    @Getter @Setter private String feelsLike;
    @Getter @Setter private String pressure;
    @Getter @Setter private String visibility;
    @Getter @Setter private String windSpeed;
    @Getter @Setter private String humidity;

    @Override
    public String toString(){
        return  "City: "        + cityName    +        "\n" +
                "Description: " + description +        "\n" +
                "Temperature: " + temp        + "°C" + "\n" +
                "Feels like: "  + feelsLike   + "°C" + "\n" +
                "Humidity: "    + humidity    + "%"  +"\n" +
                "Pressure: "    + pressure    + "\n" +
                "Visibility: "  + visibility  + "\n" +
                "Wind speed: "  + windSpeed;
    }
}
