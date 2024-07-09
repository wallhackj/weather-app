package com.wallhack.weathermap.DAO;

import com.wallhack.weathermap.DAO.Base.AdvancedDAO;
import com.wallhack.weathermap.Model.UsersPOJO;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class UsersDAO extends AdvancedDAO<UsersPOJO> {
    private static final Logger LOGGER = LogManager.getLogger(UsersDAO.class);

    public UsersDAO() {
        super(UsersPOJO.class);
    }

    public Optional<UsersPOJO> getUserByLogin(String login) {
        AtomicReference<Optional<UsersPOJO>> result = new AtomicReference<>(Optional.empty());

        executeInsideTransaction(entityManager -> {
            try {
                TypedQuery<UsersPOJO> query = entityManager.createQuery("SELECT u FROM UsersPOJO u WHERE u.login = :login", UsersPOJO.class);
                query.setParameter("login", login);
                result.set(Optional.ofNullable(query.getSingleResult()));
            }catch (NoResultException e) {
                LOGGER.info("No user found with login: {}", login);
                result.set(Optional.empty());
            }
        });

        return result.get();
    }
}
