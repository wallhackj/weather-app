package com.wallhack.weathermap.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "LocationsPOJO")
@Table(name = "locations")
@Data
@NoArgsConstructor
public class LocationsPOJO{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UsersPOJO userId;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    public LocationsPOJO(double latitude, double longitude, String name, UsersPOJO userId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.userId = userId;
    }
}
