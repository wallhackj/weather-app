package com.wallhack.weathermap.DAO;

import com.wallhack.weathermap.Model.UsersPOJO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.Optional;

public class UsersDAO extends BaseDAO<UsersPOJO> implements ICRUDUsers {
    public UsersDAO() {
        super(UsersPOJO.class);
    }

    @Override
    public Optional<UsersPOJO> getUserByLogin(String login) {
        UsersPOJO user = null;

        try (EntityManager entityManager = emf.createEntityManager()){
            TypedQuery<UsersPOJO> query = entityManager.createQuery("SELECT u FROM UsersPOJO u WHERE u.login = :login", UsersPOJO.class);
            query.setParameter("login", login);
            user = query.getSingleResult();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(user);
    }
}
