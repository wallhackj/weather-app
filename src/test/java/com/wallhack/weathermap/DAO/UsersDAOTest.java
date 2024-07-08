package com.wallhack.weathermap.DAO;

import com.wallhack.weathermap.Model.UsersPOJO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsersDAOTest {
    private final UsersDAO usersDAO = new UsersDAO();
    private final Random random = new Random();

    @Test
    @Order(1)
    public void insertTest() {
        String username = "administrator";

        UsersPOJO user = new UsersPOJO(username, "1");
        usersDAO.save(user);

        UsersPOJO user2 = usersDAO.getUserByLogin(username).orElse(null);
        assertNotNull(user2);
        assertEquals(username, user2.getLogin());
    }

    @Test
    @Order(2)
    public void updateTest(){
        String username = "administrator";
        UsersPOJO user = usersDAO.getUserByLogin(username).orElse(null);
        assertNotNull(user);

        var password = "administrator" + random.nextInt(100);
        user.setPassword(password);
        usersDAO.update(user);

        UsersPOJO user2 = usersDAO.getUserByLogin(username).orElse(null);
        assertNotNull(user2);
        assertEquals(password, user2.getPassword());
    }

    @Test
    @Order(3)
    public void findByIdTest(){
        UsersPOJO user = usersDAO.findById(3).orElse(null);
        assertNotNull(user);
        assertEquals("admin1", user.getLogin());
    }

    @Test
    @Order(4)
    public void findAllTest(){
        List<UsersPOJO> users = usersDAO.findAll();
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    @Order(5)
    public void deleteTest(){
        String username = "administrator";
        UsersPOJO user = usersDAO.getUserByLogin(username).orElse(null);
        assertNotNull(user);

        usersDAO.delete(user.getId());
        user = usersDAO.getUserByLogin(username).orElse(null);
        assertNull(user);
    }
  
}