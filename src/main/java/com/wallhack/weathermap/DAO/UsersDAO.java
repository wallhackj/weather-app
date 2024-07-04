package com.wallhack.weathermap.DAO;

import com.wallhack.weathermap.Model.UsersPOJO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Optional;

public class UsersDAO extends BaseDAO<UsersPOJO> implements ICRUDUsers {
    public UsersDAO() {
        super(UsersPOJO.class);
    }

    @Override
    public Optional<UsersPOJO> getUserByLogin(String login) {
        try (EntityManager entityManager = emf.createEntityManager()){
            TypedQuery<UsersPOJO> query = entityManager.createQuery("SELECT u FROM Users u WHERE u.login = :login", UsersPOJO.class);
            query.setParameter("login", login);
            return Optional.ofNullable(query.getSingleResult());
        }catch (Exception e) {
            return Optional.empty();
        }
    }
}
