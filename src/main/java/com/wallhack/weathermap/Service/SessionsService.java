package com.wallhack.weathermap.Service;

import com.wallhack.weathermap.DAO.SessionsDAO;
import com.wallhack.weathermap.Model.SessionsPOJO;
import java.util.List;
import java.util.Optional;

public class SessionsService {
    private final SessionsDAO sessionsDAO = new SessionsDAO();

    public void saveSession(SessionsPOJO session) {
        sessionsDAO.save(session);
    }

    public Optional<SessionsPOJO> getSession(long id) {
        return sessionsDAO.findById(id);
    }

    public List<SessionsPOJO> getAllSessions() {
        return sessionsDAO.findAll();
    }

    public void deleteSession(long id) {
        sessionsDAO.delete(id);
    }

    public void updateSession(SessionsPOJO session) {
        sessionsDAO.update(session);
    }

    public Optional<SessionsPOJO> getSessionByUserId(long userId){
        return sessionsDAO.getSessionByUserId(userId);
    }
}
