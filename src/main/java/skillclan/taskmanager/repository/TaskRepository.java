package skillclan.taskmanager.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import skillclan.taskmanager.model.Task;
import skillclan.taskmanager.model.TaskStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private final DataSource dataSource;

    //try this
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TaskRepository(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate){
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Task> create(Task task){
        final String sql = """
                              INSERT INTO tasks (title, description, status)
                              VALUES (:title, :description, :status)
                              """;
        KeyHolder keyHolder = new GeneratedKeyHolder(); // - Побачив у прикладі, але ніколи ще не використовував до цього
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", task.getTitle());
        params.addValue("description", task.getDescription());
        params.addValue("status", task.getStatus().getDbValue());
        try {
            jdbcTemplate.update(sql, params, keyHolder, new String[] {"id"});
            task.setId(keyHolder.getKey().intValue());
            return Optional.of(task);
        } catch (Exception e){
            System.out.println("Щось пішло не так під час підключення або виконання запиту створення таски в БД: " + e);
        }
        return Optional.empty();
    }

    public List<Task> findAll() {
        final String sql = "SELECT id, title, description, status FROM tasks";
        final List<Task> tasks = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement s = connection.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setStatus(TaskStatus.fromDbValue(rs.getString("status")));
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.out.println("Щось пішло не так під час підключення або виконання запиту отримання всіх тасок з БД: " + e);
        }
        return tasks;
    }

    public Optional<Task> findById(int id){
        final String sql = """
                              SELECT id, title, description, status FROM tasks
                              WHERE id = ?
                              """;
        try(Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    Task task = new Task();
                    task.setId(rs.getInt("id"));
                    task.setTitle(rs.getString("title"));
                    task.setDescription((rs.getString("description")));
                    task.setStatus(TaskStatus.valueOf(rs.getString("status")));
                    return Optional.of(task);
                }
            }
        }
        catch (SQLException e){
            System.out.println("Щось пішло не так під час пошуку таски по ІД: " + e);
        }
        return Optional.empty();
    }

    public boolean update(Task task, int id){
        final String sql = """
                              UPDATE tasks SET title = ?, description = ?, status = ?
                              WHERE id = ?
                              """;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus().getDbValue());
            ps.setInt(4, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
        catch (SQLException e){
            System.out.println("Щось пішло не так під час оновлення таски з ІД = " + id + ": " + e);
        }
        return false;
    }

    public boolean delete(int id){
        final String sql = """
                              DELETE FROM tasks
                              WHERE id = ?
                              """;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
        catch (SQLException e){
            System.out.println("Щось пішло не так під час видалення таски з ІД = " + id + ": " + e);
        }
        return false;
    }
}
