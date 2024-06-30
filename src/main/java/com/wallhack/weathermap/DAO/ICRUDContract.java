package com.wallhack.weathermap.DAO;

import java.util.List;
import java.util.Optional;

public interface ICRUDContract<E>{
    void save(E entity);
    void update(E entity);
    void delete(long id);
    Optional<E> findById(long id);
    List<E> findAll();
}
