package com.wallhack.weathermap.DAO.Session;

import com.wallhack.weathermap.Model.SessionsPOJO;

import java.util.Optional;
import java.util.UUID;

public interface ICRUDSessions {
    Optional<SessionsPOJO> findById(UUID id);
    void delete(UUID id);
    Optional<SessionsPOJO> getSessionByUserId(long userId);
}
