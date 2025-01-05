package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class BotUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Getter @Setter
    private long id;

    @Getter @Setter
    @Column(nullable = false, unique = true)
    private String username;

    @Getter @Setter
    @OneToMany(mappedBy = "botUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarDetails> carDetails;
}


