package com.wallhack.weathermap.Service;

import com.wallhack.weathermap.DAO.UsersDAO;
import com.wallhack.weathermap.Model.UsersPOJO;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsersServiceTest {
    @Mock
    private UsersDAO usersDAO;

    @InjectMocks
    private UsersService usersService;

    private AutoCloseable autoCloseable;
    private final Random random = new Random();

    @BeforeEach
    public void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @Order(1)
    public void insertTest() {
        String username = "administrator";
        UsersPOJO user = new UsersPOJO();
        user.setLogin(username);
        user.setPassword("1");

        doNothing().when(usersDAO).save(user);
        when(usersDAO.getUserByLogin(username)).thenReturn(Optional.of(user));

        usersService.registerUser(username, "1");

        UsersPOJO retrievedUser = usersService.getUser(username).orElse(null);
        assertNotNull(retrievedUser);
        assertEquals(username, retrievedUser.getLogin());
    }

    @Test
    @Order(2)
    public void updateTest() {
        String username = "administrator";
        UsersPOJO user = new UsersPOJO();
        user.setLogin(username);
        user.setPassword("oldPassword");

        when(usersDAO.getUserByLogin(username)).thenReturn(Optional.of(user));

        UsersPOJO retrievedUser = usersService.getUser(username).orElse(null);
        assertNotNull(retrievedUser);

        String newPassword = "administrator" + random.nextInt(100);
        retrievedUser.setPassword(newPassword);

        doNothing().when(usersDAO).update(retrievedUser);

        usersService.updateUser(retrievedUser);

        when(usersDAO.getUserByLogin(username)).thenReturn(Optional.of(retrievedUser));

        UsersPOJO updatedUser = usersService.getUser(username).orElse(null);
        assertNotNull(updatedUser);
        assertEquals(newPassword, updatedUser.getPassword());
    }

    @Test
    @Order(3)
    public void findByIdTest() {
        UsersPOJO user = new UsersPOJO();
        user.setId(3);
        user.setLogin("admin1");

        when(usersDAO.findById(3)).thenReturn(Optional.of(user));

        UsersPOJO retrievedUser = usersService.findUserById(3).orElse(null);
        assertNotNull(retrievedUser);
        assertEquals("admin1", retrievedUser.getLogin());
    }

    @Test
    @Order(4)
    public void findAllTest() {
        UsersPOJO user1 = new UsersPOJO();
        user1.setLogin("admin1");

        UsersPOJO user2 = new UsersPOJO();
        user2.setLogin("admin2");

        List<UsersPOJO> users = List.of(user1, user2);

        when(usersDAO.findAll()).thenReturn(users);

        List<UsersPOJO> retrievedUsers = usersService.getAllUsers();
        assertNotNull(retrievedUsers);
        assertFalse(retrievedUsers.isEmpty());
    }

    @Test
    @Order(5)
    public void deleteTest() {
        String username = "administrator";
        UsersPOJO user = new UsersPOJO();
        user.setLogin(username);
        user.setId(1);

        when(usersDAO.getUserByLogin(username)).thenReturn(Optional.of(user));
        doNothing().when(usersDAO).delete(user.getId());

        UsersPOJO retrievedUser = usersService.getUser(username).orElse(null);
        assertNotNull(retrievedUser);

        usersService.deleteUser(retrievedUser.getId());

        when(usersDAO.getUserByLogin(username)).thenReturn(Optional.empty());

        UsersPOJO deletedUser = usersService.getUser(username).orElse(null);
        assertNull(deletedUser);
    }
}
