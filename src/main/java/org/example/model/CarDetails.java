package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carDetails")
public class CarDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    @Getter
    private long id;

    @Getter @Setter private String brand;
    @Getter @Setter private String model;
    @Getter @Setter private String startYear;
    @Getter @Setter private String endYear;
    @Getter @Setter private String startPrice;
    @Getter @Setter private String endPrice;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private BotUser botUser;

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