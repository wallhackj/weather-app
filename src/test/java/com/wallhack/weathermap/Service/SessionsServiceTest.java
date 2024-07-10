package com.wallhack.weathermap.Service;

import com.wallhack.weathermap.DAO.UsersDAO;
import com.wallhack.weathermap.Model.SessionsPOJO;
import com.wallhack.weathermap.Model.UsersPOJO;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SessionsServiceTest {
    @Mock
    private UsersDAO usersDAO;

    @Mock
    private SessionsService sessionsService;

    @InjectMocks
    private SessionsPOJO sessionsPOJO;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void init(){
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @Order(1)
    public void insertTest() {
        UsersPOJO user = new UsersPOJO();
        user.setLogin("admin");

        when(usersDAO.getUserByLogin("admin")).thenReturn(Optional.of(user));

        SessionsPOJO session = new SessionsPOJO(new Timestamp(System.currentTimeMillis()), user);
        sessionsService.saveSession(session);

        when(sessionsService.getSessionByUserId(user.getId())).thenReturn(Optional.of(session));

        SessionsPOJO retrievedSession = sessionsService.getSessionByUserId(user.getId()).orElse(null);
        assertNotNull(retrievedSession);
        assertEquals(user.getId(), retrievedSession.getUserId().getId());
    }

    @Test
    @Order(2)
    public void updateTest() {
        UsersPOJO user = new UsersPOJO();
        user.setLogin("testUser");

        SessionsPOJO session = new SessionsPOJO(new Timestamp(System.currentTimeMillis()), user);

        when(sessionsService.getSessionByUserId(2)).thenReturn(Optional.of(session));

        Timestamp newTimestamp = new Timestamp(System.currentTimeMillis() + 1000);
        session.setExpiresAt(newTimestamp);
        sessionsService.updateSession(session);

        when(sessionsService.getSessionByUserId(2)).thenReturn(Optional.of(session));

        SessionsPOJO updatedSession = sessionsService.getSessionByUserId(2).orElse(null);
        assertNotNull(updatedSession);
        assertEquals(newTimestamp, updatedSession.getExpiresAt());
    }

    @Test
    @Order(3)
    public void findAllTest() {
        SessionsPOJO session1 = new SessionsPOJO(new Timestamp(System.currentTimeMillis()), new UsersPOJO());
        SessionsPOJO session2 = new SessionsPOJO(new Timestamp(System.currentTimeMillis()), new UsersPOJO());

        List<SessionsPOJO> sessions = List.of(session1, session2);

        when(sessionsService.getAllSessions()).thenReturn(sessions);

        List<SessionsPOJO> retrievedSessions = sessionsService.getAllSessions();
        assertNotNull(retrievedSessions);
        assertFalse(retrievedSessions.isEmpty());
    }

    @Test
    @Order(4)
    public void deleteTest() {
        UsersPOJO user = new UsersPOJO();
        user.setId(2); // or set other necessary fields

        SessionsPOJO session = new SessionsPOJO(new Timestamp(System.currentTimeMillis()), user);

        when(sessionsService.getSessionByUserId(2)).thenReturn(Optional.of(session));

        sessionsService.deleteSession(session.getId());

        when(sessionsService.getSessionById(session.getId())).thenReturn(Optional.empty());

        SessionsPOJO deletedSession = sessionsService.getSessionById(session.getId()).orElse(null);
        assertNull(deletedSession);
    }
}