package com.wallhack.weathermap.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Entity(name = "Sessions")
@Table(name = "sessions")
@Data
@NoArgsConstructor
public class SessionsPOJO{
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private UsersPOJO userId;

    @Column(nullable = false)
    private Date expiresAt;

    public SessionsPOJO(Date expiresAt, UsersPOJO userId) {
        this.expiresAt = expiresAt;
        this.userId = userId;
    }
}




