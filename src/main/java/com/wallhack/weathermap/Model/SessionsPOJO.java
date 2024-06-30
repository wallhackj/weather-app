package com.wallhack.weathermap.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity(name = "SessionsPOJO")
@Table(name = "sessions")
@Data
@NoArgsConstructor
public class SessionsPOJO{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UsersPOJO userId;

    @Column(nullable = false)
    private Date expiresAt;

    public SessionsPOJO(Date expiresAt, UsersPOJO userId) {
        this.expiresAt = expiresAt;
        this.userId = userId;
    }
}




