package com.wallhack.weathermap.DAO.Session;

import com.wallhack.weathermap.DAO.UsersDAO;
import com.wallhack.weathermap.Model.SessionsPOJO;
import com.wallhack.weathermap.Model.UsersPOJO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SessionsDAOTest {
    SessionsDAO sessionsDAO = new SessionsDAO();
    UsersDAO usersDAO = new UsersDAO();

    @Test
    @Order(1)
    public void insertTest(){
        UsersPOJO user = usersDAO.getUserByLogin("admin").orElse(null);
        assertNotNull(user);

        sessionsDAO.save(new SessionsPOJO(new Timestamp(22) , user));

        SessionsPOJO session = sessionsDAO.getSessionByUserId(user.getId()).orElse(null);
        assertNotNull(session);
        assertEquals(user.getId(), session.getUserId().getId());
    }

    @Test
    @Order(2)
    public void updateTest(){
        SessionsPOJO session = sessionsDAO.getSessionByUserId(2).orElse(null);
        assertNotNull(session);

        Timestamp timestamp = new Timestamp(33);
        session.setExpiresAt(timestamp);
        sessionsDAO.update(session);

        SessionsPOJO sessionTest = sessionsDAO.getSessionByUserId(2).orElse(null);
        assertNotNull(session);
        assert sessionTest != null;
        assertEquals(timestamp, sessionTest.getExpiresAt());
    }

    @Test
    @Order(3)
    public void findAllTest(){
        List<SessionsPOJO> sessions = sessionsDAO.findAll();
        assertNotNull(sessions);
        assertFalse(sessions.isEmpty());
    }

    @Test
    @Order(4)
    public void deleteTest(){
        SessionsPOJO session = sessionsDAO.getSessionByUserId(2).orElse(null);
        assertNotNull(session);

        sessionsDAO.delete(session.getId());
        SessionsPOJO sessionTest = sessionsDAO.getSessionByUserId(2).orElse(null);
        assertNull(sessionTest);
    }
}