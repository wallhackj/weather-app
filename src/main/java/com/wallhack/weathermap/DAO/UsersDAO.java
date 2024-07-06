package com.wallhack.weathermap.DAO;

import com.wallhack.weathermap.DAO.Base.AdvancedDAO;
import com.wallhack.weathermap.Model.UsersPOJO;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class UsersDAO extends AdvancedDAO<UsersPOJO> {
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
                result.set(Optional.empty());
            }
        });

        return result.get();
    }
}
