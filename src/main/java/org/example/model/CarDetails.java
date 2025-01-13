package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class CarDetails {

    @Getter @Setter private String brand;
    @Getter @Setter private String model;
    @Getter @Setter private String startYear;
    @Getter @Setter private String endYear;
    @Getter @Setter private String startPrice;
    @Getter @Setter private String endPrice;

    @Override
    public String toString() {
        return  "Brand: "      + brand      +
                "Model: "      + model      + "\n" +
                "StartYear: "  + startYear  + "\n" +
                "EndYear: "    + endYear    + "\n" +
                "StartPrice: " + startPrice + "\n" +
                "EndPrice: "   + endPrice;
    }
}