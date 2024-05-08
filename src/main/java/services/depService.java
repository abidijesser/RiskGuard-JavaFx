package services;

import java.sql.SQLException;
import java.util.List;

public interface depService<T> {

    void add(T t) throws SQLException;

    void update(T t) throws SQLException;

    void delete(int idep) throws SQLException;

    List<T> getAll() throws SQLException;

    T getById(int idep) throws  SQLException;
}
