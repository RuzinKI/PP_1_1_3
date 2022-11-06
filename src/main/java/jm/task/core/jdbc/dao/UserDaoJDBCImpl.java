package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {


    private static final String CREATE_USERS_TABLE = """
            CREATE TABLE IF NOT EXISTS user(id SERIAL,
            name TEXT,
            lastName TEXT,
            age INT);
            """;

    private static final String DROP_USERS_TABLE = """
            DROP TABLE IF EXISTS user;
            """;

    private static final String SAVE_USER = """
            INSERT INTO user (name, lastName, age)
            VALUE (?, ?, ?);
            """;

    private static final String REMOVE_USER_BY_ID = """
            DELETE FROM user 
            WHERE id = ?;
            """;

    private static final String GET_ALL_USERS = """
            SELECT *
            FROM user;
            """;

    private static final String CLEAN_USERS_TABLE = """
            DELETE FROM user 
            WHERE id IS NOT NULL;
            """;


    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (var connection = Util.get();
             var statement = connection.createStatement()) {
            statement.execute(CREATE_USERS_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try (var connection = Util.get();
             var statement = connection.createStatement()) {
            statement.execute(DROP_USERS_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (var connection = Util.get();
             var prepareStatement = connection.prepareStatement(SAVE_USER)) {

            prepareStatement.setString(1, name);
            prepareStatement.setString(2, lastName);
            prepareStatement.setInt(3, age);

            boolean execute = prepareStatement.execute();
            if (!execute) {
                System.out.println("User с именем " + name + " добавлен в базу данных");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (var connection = Util.get();
             var prepareStatement = connection.prepareStatement(REMOVE_USER_BY_ID)) {

            prepareStatement.setLong(1, id);
            prepareStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        try (var connection = Util.get();
             var prepareStatement = connection.prepareStatement(GET_ALL_USERS)) {

            ResultSet resultSet = prepareStatement.executeQuery();

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(buildUsers(resultSet));
            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User buildUsers(ResultSet resultSet) throws SQLException {
        User user = new User(
                resultSet.getString("name"),
                resultSet.getString("lastName"),
                resultSet.getObject("age", Byte.class)
        );
        user.setId(resultSet.getLong("id"));
        return user;
    }

    public void cleanUsersTable() {
        try (var connection = Util.get();
             var statement = connection.createStatement()) {
            statement.execute(CLEAN_USERS_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
