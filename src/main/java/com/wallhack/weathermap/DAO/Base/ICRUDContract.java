package com.wallhack.weathermap.DAO.Base;

import java.util.List;

public interface ICRUDContract<E>{
    void save(E entity);
    void update(E entity);
    List<E> findAll();
}
