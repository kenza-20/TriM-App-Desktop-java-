package services;

import java.util.List;

public interface IService<T> {

    void add(T t);

    void update(T t, int id);

    void delete(int id);

    T getById(int id);
    List<T>getAll();
}
