package skillclan.taskmanager.repository;

import org.springframework.stereotype.Repository;
import skillclan.taskmanager.model.User;

import java.sql.SQLException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final DataSource dataSource;

    public UserRepository(DataSource dataSource){
        this.dataSource = dataSource;
    }
    public Optional<User> create(User user) {
        final String sql = """
                              INSERT INTO users (name, email, phone_number)
                              VALUES (?, ?, ?)
                              """;
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhoneNumber());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            System.out.println("Щось пішло не так під час підключення або виконання запиту створення юзера в БД: " + e);
        }
        return Optional.empty();
    }

    public Optional<User> findById(int id){
        final String sql = "SELECT id, email, name, phone_number FROM users WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhoneNumber(rs.getString(("phone_number")));
                    return Optional.of(user);
                }
            }
        }
        catch (SQLException e){
            System.out.println("Щось пішло не так під час пошуку користувача по ІД: " + e);
        }
        return Optional.empty();
    }
    public List<User> findAll(){
        final String sql = "SELECT id, email, name, phone_number FROM users";
        final List<User> users = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery(sql)){
            while (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phone_number"));
                users.add(user);
            }
        }
        catch (SQLException e){
            System.out.println("Щось пішло не так під час отримання всіх покистувачів: " + e);
        }
        return users;
    }
    public boolean update(User user, int id){
        final String sql = """
                              UPDATE users SET name = ?, email = ?, phone_number = ?
                              WHERE id = ?
                              """;
        try(Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
           ps.setString(1, user.getName());
           ps.setString(2, user.getEmail());
           ps.setString(3, user.getPhoneNumber());
           ps.setInt(4, id);
           int affectedRows = ps.executeUpdate();
           return affectedRows > 0;
        }
        catch (SQLException e){
            System.out.println("Щось пішло не так під час оновлення користувача з ІД = " + id + ": " + e);
        }
        return false;
    }
    public boolean delete(int id){
        final String sql = "DELETE FROM users WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
        catch (SQLException e){
            System.out.println("Щось пішло не так під час видалення користувача з ІД = " + id + ": " + e);
        }
        return false;
    }
}
