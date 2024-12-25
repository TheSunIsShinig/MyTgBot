package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class AutoRiaCars {
    @Getter @Setter private String mark;
    @Getter @Setter private String model;
    @Getter @Setter private String price;
    @Getter @Setter private String year;
    @Getter @Setter private String mileage;
    @Getter @Setter private String region;
    @Getter @Setter private String fuelType;
    @Getter @Setter private String gearBoxType;
    @Getter @Setter private String description;
    @Getter @Setter private String date;
    @Getter @Setter private String link;

    @Override
    public String toString() {
        return  "Name: " + mark + " " + model + "\n" +
                "Price: " + price + "\n" +
                "Year: " + year + "\n" +
                "Mileage: " + mileage + "\n" +
                "Region: " + region + "\n" +
                "FuelType: " + fuelType + "\n" +
                "GearBoxType: " + gearBoxType + "\n" +
                "Description: " + description + "\n" +
                "Date: " +  date +"\n" +
                "Link: " + link;
    }
}
