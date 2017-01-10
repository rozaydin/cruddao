package com.rhtech.cruddao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;


import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static com.rhtech.cruddao.SqlHelper.*;

/**
 * Created by rozaydin on 12/30/16.
 */
public class CrudDao<T> implements ICrudDao<T> {

    private final String SQL_INSERT_QUERY_AUTO_INCREMENT;
    private final String SQL_MERGE_QUERY;
    private final String SQL_UPDATE_QUERY;
    private final String SQL_DELETE_WITH_ID_QUERY;
    private final String SQL_DELETE_ALL_QUERY;
    private final String SQL_SELECT_WITH_ID_QUERY;
    private final String SQL_SELECT_ALL_QUERY;
    private final String SQL_EXISTS_QUERY;
    private final String SQL_ABSTRACT_SEARCH_QUERY;

    private final JdbcTemplate template;
    private final Class clazz;
    private final Field[] fields;
    private final RowMapper<T> rowMapper;


    public CrudDao(Class clazz, JdbcTemplate template) {

        this.template = template;
        this.clazz = clazz;
        this.fields = clazz.getDeclaredFields();

        final String tableName = decideTableName(clazz);

        this.SQL_INSERT_QUERY_AUTO_INCREMENT = SqlHelper.createSqlInsertQueryIdAutoIncrement(tableName, fields);
        this.SQL_MERGE_QUERY = SqlHelper.createSqlMergeQuery(tableName, fields);
        this.SQL_UPDATE_QUERY = SqlHelper.createSqlUpdateQuery(tableName, fields);
        this.SQL_DELETE_WITH_ID_QUERY = SqlHelper.createSqlDeleteQuery(tableName);
        this.SQL_DELETE_ALL_QUERY = SqlHelper.createSqlDeleteAllQuery(tableName);
        this.SQL_SELECT_WITH_ID_QUERY = SqlHelper.createSqlSelectQuery(tableName);
        this.SQL_SELECT_ALL_QUERY = SqlHelper.createSqlSelectAllQuery(tableName);
        this.SQL_EXISTS_QUERY = SqlHelper.createSqExistsQuery(tableName);
        this.SQL_ABSTRACT_SEARCH_QUERY = SqlHelper.createSqlAbstractSearchQuery(tableName);


        // Create row mapper
        rowMapper = new RowMapper<T>() {
            @Override
            public T mapRow(ResultSet rs, int rowNum) throws SQLException {

                try {
                    //
                    Object instance = CrudDao.this.clazz.newInstance();

                    for (Field field : fields) {
                        if (!field.isSynthetic()) {
                            Object value = rs.getObject(field.getName(), getWrapperClass(field.getType()));
                            field.setAccessible(true);
                            field.set(instance, value);
                        }
                    }

                    return (T) instance;

                } catch (InstantiationException ie) {
                    System.out.println("instantiation error");
                    throw new SQLException(ie.getCause());
                } catch (IllegalAccessException iae) {
                    System.out.println("illegal access error");
                    throw new SQLException(iae.getCause());
                }
            }
        };
    }

    private String decideTableName(Class clazz) {
        return clazz.getSimpleName();
    }

    /**
     * UTIL Methods
     */
    private static final Class getWrapperClass(Class clazz) {

        if (clazz.isPrimitive()) {

            switch (clazz.getName()) {
                case "boolean":
                    return Boolean.class;
                case "byte":
                    return Byte.class;
                case "char":
                    return Character.class;
                case "double":
                    return Double.class;
                case "float":
                    return Float.class;
                case "int":
                    return Integer.class;
                case "long":
                    return Long.class;
                case "short":
                    return Short.class;
                case "void":
                    return Void.class;
                default:
                    return null;
            }
        } else {
            return clazz;
        }
    }

    /**
     *
     * @param t
     * @return
     */
    public int merge (T t) {

        List values = new ArrayList<>();

        try {
            for (Field field : fields) {
                if (!ID_FIELD.equalsIgnoreCase(field.getName()) && !field.isSynthetic()) {
                    // new PropertyDescriptor(field.getName(), clazz).getReadMethod().invoke(t)
                    field.setAccessible(true);
                    values.add(field.get(t));
                }
            }
        } catch (Exception exc) {
            System.out.println(exc);
        }

        // perform sql update
        return template.update(SQL_MERGE_QUERY, values.toArray());
    }

    /**
     * Insert
     *
     * @param t
     * @return
     */
    public int insert(T t) {

        GeneratedKeyHolder holder = new GeneratedKeyHolder();

        PreparedStatementCreator psc = (Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(SQL_INSERT_QUERY_AUTO_INCREMENT, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            //
            try {

                for (Field field : fields) {
                    if (!ID_FIELD.equalsIgnoreCase(field.getName()) && !field.isSynthetic()) {
                        // new PropertyDescriptor(field.getName(), clazz).getReadMethod().invoke(t)
                        field.setAccessible(true);
                        statement.setObject(index, field.get(t));
                        index++;
                    }
                }

            } catch (Exception exc) {
                System.out.println(exc);
                statement = null;
            }

            return statement;
        };

        template.update(psc, holder);
        return (Integer) holder.getKey();

    }

    /**
     * update
     *
     * @param t
     * @return
     * @throws Exception
     */
    public int update(int id, T t) {

        List values = new ArrayList<>();

        try {
            for (Field field : fields) {
                if (!ID_FIELD.equalsIgnoreCase(field.getName()) && !field.isSynthetic()) {
                    // new PropertyDescriptor(field.getName(), clazz).getReadMethod().invoke(t)
                    field.setAccessible(true);
                    values.add(field.get(t));
                }
            }
        } catch (Exception exc) {
            System.out.println(exc);
        }

        // add id field last
        values.add(id);
        return template.update(SQL_UPDATE_QUERY, values.toArray());
    }

    /**
     * delete
     *
     * @param id
     * @return
     */
    public int delete(int id) {
        return template.update(SQL_DELETE_WITH_ID_QUERY, id);
    }

    /**
     * delete all
     *
     * @return
     */
    public int deleteAll() {
        return template.update(SQL_DELETE_ALL_QUERY);
    }

    /**
     * select
     *
     * @param id
     * @return
     */
    public T get(int id) {
        return (T) template.queryForObject(SQL_SELECT_WITH_ID_QUERY, new Object[]{id}, rowMapper);
    }

    /**
     * select all
     *
     * @return
     */
    public List<T> getAll() {
        return template.query(SQL_SELECT_ALL_QUERY, rowMapper);
    }

    /**
     * Check if record exists
     */
    public boolean isRecordExists(int id) {
        return template.queryForObject(SQL_EXISTS_QUERY, new Object[]{id}, Integer.class) == 1;
    }

    /**
     *
     * @param fields
     * @param values
     * @return
     */
    public List<T> find(String[] fields, Object[] values) {

        StringBuilder strb = new StringBuilder();
        strb.append(SQL_ABSTRACT_SEARCH_QUERY);
        for ( String field: fields ) {
            strb.append(field).append("=").append("?");
        }

        return template.query(strb.toString(), values, rowMapper);
    }

}