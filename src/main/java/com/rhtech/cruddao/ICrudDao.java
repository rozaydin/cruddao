package com.rhtech.cruddao;

import java.util.List;

/**
 * Created by rozaydin on 12/30/16.
 */
public interface ICrudDao<T> {

    int merge (T t);

    int insert(T t);

    int update(int id, T t);

    T get(int id);

    List<T> getAll();

    int delete(int id);

    int deleteAll();

    boolean isRecordExists(int id);

    List<T> find(String[] fields, Object[] values);

}