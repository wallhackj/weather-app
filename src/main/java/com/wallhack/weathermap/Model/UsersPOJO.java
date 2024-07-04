package com.wallhack.weathermap.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Users")
@Table(name = "users", indexes = {
        @Index(name = "users_login_key", columnList = "login", unique = true)
})
@Data
@NoArgsConstructor
public class UsersPOJO {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column(unique = true, nullable = false)
        private String login;

        @Column(nullable = false)
        private String password;

        public UsersPOJO(String login, String password) {
                this.login = login;
                this.password = password;
        }
}



