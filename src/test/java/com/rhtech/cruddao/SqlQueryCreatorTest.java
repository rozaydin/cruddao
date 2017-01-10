package com.rhtech.cruddao;

import com.rhtech.cruddao.ApplicationTestConfiguration;
import com.rhtech.cruddao.SqlHelper;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by rozaydin on 12/30/16.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(/*locations = {"/test-context.xml"},*/ classes = {ApplicationTestConfiguration.class})
public class SqlQueryCreatorTest {

    @Autowired
    private JdbcTemplate template;

    @Test
    public void test() {

        final String tableName = TestClass.class.getSimpleName();
        final Field[] fields = TestClass.class.getDeclaredFields();

        String sqlDeleteQuery = SqlHelper.createSqlDeleteQuery(tableName);
        assertThat("Delete From TestClass Where id=?").isEqualToIgnoringCase(sqlDeleteQuery);

        String sqlSelectAllQuery = SqlHelper.createSqlSelectAllQuery(tableName);
        assertThat("SELECT * FROM TestClass").isEqualToIgnoringCase(sqlSelectAllQuery);

        String sqlSelectOneQuery = SqlHelper.createSqlSelectQuery(tableName);
        assertThat("SELECT * FROM TestClass WHERE Id=?").isEqualToIgnoringCase(sqlSelectOneQuery);

        String sqlUpdateQuery = SqlHelper.createSqlUpdateQuery(tableName, fields);
        assertThat("UPDATE TestClass SET name=?,surname=?,time=?,money=? WHERE id=?").isEqualToIgnoringCase(sqlUpdateQuery);

        String sqlInsertQuery = SqlHelper.createSqlInsertQueryIdAutoIncrement(tableName, fields);
        assertThat("INSERT INTO TestClass(name,surname,time,money) VALUES(?,?,?,?)").isEqualToIgnoringCase(sqlInsertQuery);

        ICrudDao<TestClass> dao = new CrudDao(TestClass.class, template);
        TestClass test = new TestClass(1, "name", "surname", 10l, 10l);
        int id = dao.insert(test);
        assertThat(1).isEqualTo(id);

        TestClass retrieved = dao.get(id);
        assertThat(test).isEqualTo(retrieved);

        TestClass updated = new TestClass(1, "updated name", "updated surname", 1l, 1.0f);
        int updatedCount = dao.update(1, updated);
        assertThat(updatedCount).isEqualTo(1);

        TestClass updatedRetrieved = dao.get(1);
        assertThat(updatedRetrieved).isEqualTo(updated);

        int deletedCount = dao.delete(1);
        assertThat(deletedCount).isEqualTo(1);

        List<TestClass> allRecords = dao.getAll();
        assertThat(allRecords.size()).isEqualTo(0);

        int newId = dao.insert(test);
        assertThat(newId).isEqualTo(2);

        int newId2 = dao.insert(test);
        assertThat(newId2).isEqualTo(3);

        List<TestClass> allRecordsNew = dao.getAll();
        assertThat(allRecordsNew.size()).isEqualTo(2);

        int delectedCount = dao.deleteAll();
        assertThat(delectedCount).isEqualTo(2);

        List<TestClass> allRecordsAfterDeletion = dao.getAll();
        assertThat(allRecordsAfterDeletion.size()).isEqualTo(0);

    }

}