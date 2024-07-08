package com.wallhack.weathermap.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallhack.weathermap.DAO.Session.SessionsDAO;
import com.wallhack.weathermap.Model.DTO.CookieLocation;
import com.wallhack.weathermap.Model.SessionsPOJO;
import com.wallhack.weathermap.utils.Conectors.CookieProcessor;
import com.wallhack.weathermap.utils.ErrorResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public void processCookies(CookieProcessor processor, HttpServletResponse resp, HttpServletRequest req, ObjectMapper mapper, SessionsService sessionsService) throws IOException, URISyntaxException, InterruptedException {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : req.getCookies()) {
                if (cookie.getName().equals("sessionsId")) {
                    HttpSession session = req.getSession(false);

                    if (session != null && session.getAttribute("userId") != null) {
                        long id = (long) session.getAttribute("userId");
                        if (sessionsService.getSessionByUserId(id).isPresent()) {
                            processor.proceed(resp,new CookieLocation(id,"1", 1,1));
                        } else {
                            resp.setStatus(403);
                            mapper.writeValue(resp.getWriter(), new ErrorResponse(403, "Session expired"));
                        }
                    }
                }
            }
        }
    }
}
