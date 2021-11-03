package com.revature.dao;

import java.util.List;

public interface DAO<T> {

    //Create
    T insert(T obj);

    //Read
    List<T> selectAll();
    T select(String value);

    //Update
    T update(T obj);

    //Delete
    boolean delete(T obj);

}
