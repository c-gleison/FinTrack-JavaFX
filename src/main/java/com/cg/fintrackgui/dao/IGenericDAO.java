package com.cg.fintrackgui.dao;

import java.sql.SQLException;
import java.util.List;

// Interface genérica com as operações do padrão Data Access Object (DAO)
public interface IGenericDAO<T> {
    
    // Métodos para persistência, alteração, remoção e consulta de entidades
    void save(T entity) throws SQLException;
    void update(T entity) throws SQLException;
    void remove(String id) throws SQLException;
    T findByID(String id) throws SQLException;
    List<T> findAll() throws SQLException;
}