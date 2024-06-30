package com.wallhack.weathermap.DAO;

import com.wallhack.weathermap.Model.SessionsPOJO;

import java.util.Optional;

public interface ICRUDSessions {
    Optional<SessionsPOJO> getSessionByUserId(long userId);
}
