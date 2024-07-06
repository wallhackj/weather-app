package com.wallhack.weathermap.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "LocationsPOJO")
@Table(name = "locations")
@Data
@NoArgsConstructor
public class LocationsPOJO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private UsersPOJO user;

    public LocationsPOJO(double latitude, double longitude, String name, UsersPOJO user) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.user = user;
    }
}
