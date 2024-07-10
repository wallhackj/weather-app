package com.wallhack.weathermap.Service;

import com.wallhack.weathermap.DAO.Session.SessionsDAO;
import com.wallhack.weathermap.Model.cookieDTO.CookieLocation;
import com.wallhack.weathermap.Model.SessionsPOJO;
import com.wallhack.weathermap.utils.Conectors.CookieProcessor;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

public class SessionsService {
    private final SessionsDAO sessionsDAO = new SessionsDAO();

    public void saveSession(SessionsPOJO session) {
        sessionsDAO.save(session);
    }

    public Optional<SessionsPOJO> getSessionById(UUID id) {
        return sessionsDAO.findById(id);
    }

    public List<SessionsPOJO> getAllSessions() {
        return sessionsDAO.findAll();
    }

    public void deleteSession(UUID id) {
        sessionsDAO.delete(id);
    }

    public void updateSession(SessionsPOJO session) {
        sessionsDAO.update(session);
    }

    public Optional<SessionsPOJO> getSessionByUserId(long userId){
        return sessionsDAO.getSessionByUserId(userId);
    }

    public void deleteExpiredSessions() {
        SessionsService sessionsService = new SessionsService();

        List<SessionsPOJO> allSessions = sessionsService.getAllSessions();
        for (SessionsPOJO session : allSessions) {
            if (isSessionExpired(session)) {
                sessionsService.deleteSession(session.getId());
            }
        }
    }

    public static Timestamp getWhenExpiersSessionTimestamp(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        return new Timestamp(calendar.getTime().getTime());
    }

    private static boolean isSessionExpired(SessionsPOJO session) {
        Instant now = Instant.now();
        Instant expiresAt = session.getExpiresAt().toInstant();
        return now.isAfter(expiresAt);
    }

    public void processCookies(CookieProcessor processor, HttpServletResponse resp, HttpServletRequest req, SessionsService sessionsService) throws IOException, URISyntaxException, InterruptedException, NoResultException {
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : req.getCookies()) {

                if (cookie.getName().equals("sessionId")) {

                    if (cookie.getValue() != null) {
                        SessionsPOJO session = sessionsService
                                .getSessionById(UUID.fromString(cookie.getValue()))
                                .orElse(null);

                        if (session != null) {
                            processor.proceed(resp, new CookieLocation(
                                    session.getUserId().getId()
                                    , "1", 1,1));

                        }else throw new NoResultException("User is not present");
                    }
                }
            }
        }
    }
}
