package com.rhtech.cruddao;

import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;

/**
 * Created by rozaydin on 12/30/16.
 */
public class SqlHelper {

    public static final String ID_FIELD = "Id";

    public static final String createSqlInsertQueryIdAutoIncrement(String tableName, Field[] fields) {

        StringBuilder strb = new StringBuilder();
        strb.append("INSERT").append(" ").append("INTO").append(" ").append(tableName).append("(");
        for (int i = 0; i < fields.length; i++) {
            if (!(ID_FIELD.equalsIgnoreCase(fields[i].getName())) && !fields[i].isSynthetic()) {
                strb.append(fields[i].getName());
                strb.append(",");

            }
        }
        // delete last comma
        strb.deleteCharAt(strb.length() - 1);
        strb.append(")");

        strb.append(" ").append("VALUES(");
        for (int i = 0; i < fields.length; i++) {
            if (!(ID_FIELD.equalsIgnoreCase(fields[i].getName())) && !fields[i].isSynthetic()) {
                strb.append("?");
                strb.append(",");
            }
        }
        // delete last comma
        strb.deleteCharAt(strb.length() - 1);
        strb.append(")");

        return strb.toString();
    }

    public static final String createSqlMergeQuery(String tableName, Field[] fields) {

        StringBuilder strb = new StringBuilder();
        strb.append("MERGE").append(" ").append("INTO").append(" ").append(tableName).append("(");
        for (int i = 0; i < fields.length; i++) {
            if (!fields[i].isSynthetic()) {
                strb.append(fields[i].getName());
                strb.append(",");

            }
        }
        // delete last comma
        strb.deleteCharAt(strb.length() - 1);
        strb.append(")");

        strb.append(" ").append("VALUES(");
        for (int i = 0; i < fields.length; i++) {
            if (!fields[i].isSynthetic()) {
                strb.append("?");
                strb.append(",");
            }
        }
        // delete last comma
        strb.deleteCharAt(strb.length() - 1);
        strb.append(")");

        return strb.toString();
    }

    public static final String createSqlUpdateQuery(String tableName, Field[] fields) {

        StringBuilder strb = new StringBuilder();
        strb.append("UPDATE").append(" ").append(tableName).append(" ").append("SET").append(" ");

        for (int i = 0; i < fields.length; i++) {
            if (!(ID_FIELD.equalsIgnoreCase(fields[i].getName())) && !fields[i].isSynthetic()) {
                strb.append(fields[i].getName()).append("=?");
                strb.append(",");
            }
        }
        // delete last comma
        strb.deleteCharAt(strb.length() - 1);

        strb.append(" ").append("WHERE ").append(ID_FIELD).append("=?");
        return strb.toString();
    }

    public static final String createSqlDeleteQuery(String tableName) {
        return "DELETE FROM " + tableName + " WHERE " + ID_FIELD + "=?";
    }

    public static final String createSqlDeleteAllQuery(String tableName) {
        return "DELETE FROM " + tableName;
    }

    public static final String createSqlSelectQuery(String tableName) {
        return "SELECT * FROM " + tableName + " WHERE " + ID_FIELD + "=?";
    }

    public static final String createSqlSelectAllQuery(String tableName) {
        return "SELECT * FROM " + tableName;
    }

    public static final String createSqExistsQuery(String tableName) { return "SELECT count(1) FROM " + tableName + " WHERE id=?"; }

    public static final String createSqlAbstractSearchQuery(String tableName) { return "SELECT * FROM " + tableName + " WHERE "; }

}