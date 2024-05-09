package services;

import java.sql.SQLException;
import java.util.List;

public interface empService<T> {

    void add(T t) throws SQLException;

    void update(T t) throws SQLException;

    void delete(int id) throws SQLException;

    List<T> getAll() throws SQLException;

    T getById(int id) throws  SQLException;
}