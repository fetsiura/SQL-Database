package pl.coderslab.entity;

import java.sql.*;

public class UserDao {
    private static final String CREATE_DATABASE_QUERY = "CREATE TABLE users(\n" +
            "    id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
            "    username VARCHAR(255) NOT NULL,\n" +
            "    email VARCHAR(255) NOT NULL UNIQUE KEY ,\n" +
            "    password VARCHAR(60) NOT NULL\n" +
            ");";
    private static final String CREATE_USER_QUERY = "INSERT INTO users (username, email, password) VALUES (?,?,?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET username=?, email=?, password=? where id=?";
    private static final String READ_USER_QUERY = "SELECT * FROM users where id=?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id=?";
    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users";


    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public void createDatabase() {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(CREATE_DATABASE_QUERY);
            statement.executeUpdate();
            System.out.println("Database created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User createUser(User user) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                user.setId(resultSet.getInt(1));
                System.out.println("Inserted User's ID: " + user.getId());
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteUser(int id) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_USER_QUERY);
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("User successfully deleted");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user, int id) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_USER_QUERY);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, this.hashPassword(user.getPassword()));
            statement.setInt(4, id);
            statement.executeUpdate();
            System.out.println("User successfully updated");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readAllUsers() {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(FIND_ALL_USERS_QUERY);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                System.out.format("%s, %s, %s\n", "id=" + user.getId(), "username=" + user.getUserName(), "email=" + user.getEmail());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User readUser(int id) {
         try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(READ_USER_QUERY);
             statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}