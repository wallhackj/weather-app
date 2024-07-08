package com.wallhack.weathermap.Service;

import com.wallhack.weathermap.DAO.UsersDAO;
import com.wallhack.weathermap.Model.UsersPOJO;

import java.util.List;
import java.util.Optional;

public class UsersService {
    private final UsersDAO usersDAO =  new UsersDAO();

    public void registerUser(String username, String password) {
        usersDAO.save(new UsersPOJO(username, password));
    }

    public Optional<UsersPOJO> findUserById(long id) {
       return usersDAO.findById(id);
    }

    public Optional<UsersPOJO> getUser(String username) {
        return usersDAO.getUserByLogin(username);
    }

    public List<UsersPOJO> getAllUsers() {
        return usersDAO.findAll();
    }

    public void updateUser(UsersPOJO user) {
        usersDAO.update(user);
    }

    public void deleteUser(long id) {
        usersDAO.delete(id);
    }

}
