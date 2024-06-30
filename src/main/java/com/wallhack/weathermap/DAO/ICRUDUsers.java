package com.wallhack.weathermap.DAO;


import com.wallhack.weathermap.Model.UsersPOJO;

import java.util.Optional;

public interface ICRUDUsers {
    Optional<UsersPOJO> getUserByLogin(String login);
}
