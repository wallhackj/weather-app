package com.wallhack.weathermap.Service;

import com.wallhack.weathermap.DAO.BaseDAO;
import com.wallhack.weathermap.DAO.ICRUDContract;
import com.wallhack.weathermap.DAO.ICRUDUsers;
import com.wallhack.weathermap.DAO.UsersDAO;
import com.wallhack.weathermap.Model.UsersPOJO;

import java.util.List;
import java.util.Optional;

public class UsersService {
    private final UsersDAO usersDAO =  new UsersDAO();

    public void registerUser(String username, String password) {
        usersDAO.save(new UsersPOJO(username, password));
    }

    public void findUserById(long id) {
        usersDAO.findById(id);
    }

    public Optional<UsersPOJO> getUser(String username) {
        return usersDAO.getUserByLogin(username);
    }

    public List<UsersPOJO> getAllUsers() {
        return usersDAO.findAll();
    }

    public void updateUserPassword(UsersPOJO user) {
        usersDAO.update(user);
    }

    public void deleteUser(long id) {
        usersDAO.delete(id);
    }

}
