package com.cg.fintrackgui.dao;

import java.sql.SQLException;
import java.util.List;

public interface IGenericDAO<T> {
    
    void save(T entity) throws SQLException;
    void update(T entity) throws SQLException;
    void remove(int id) throws SQLException;
    T findByID(int id) throws SQLException;
    List<T> findAll() throws SQLException;
   
}
