package skillclan.taskmanager.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import skillclan.taskmanager.model.Task;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private static final Logger logger = LoggerFactory.getLogger(TaskRepository.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TaskRepository(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Task> create(Task task){
        final String sql = """
            INSERT INTO tasks (title, description, status)
            VALUES (:title, :description, :status)
            RETURNING id
            """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", task.getTitle());
        params.addValue("description", task.getDescription());
        params.addValue("status", task.getStatus().name());
        try {
            task.setId(jdbcTemplate.queryForObject(sql, params, Integer.class));
            return Optional.of(task);
        } catch (DataAccessException e){
            logger.error("Failed to create task. SQL was: {}", sql, e);
        }
        return Optional.empty();
    }

    public List<Task> findAll() {
        final String sql = "SELECT id, title, description, status FROM tasks";
        try {
            return jdbcTemplate.query(sql, Collections.emptyMap(), new BeanPropertyRowMapper<>(Task.class));
        } catch (DataAccessException e){
            logger.error("Failed to retrieve all tasks. SQL was: {}", sql, e);
        }
        return Collections.emptyList();
    }

    public Optional<Task> findById(int id){
        final String sql = """
            SELECT id, title, description, status FROM tasks
            WHERE id = :id
            """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        try {
            Task task = jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Task.class));
            return Optional.ofNullable(task);
        } catch (DataAccessException e){
            logger.error("Failed to retrieve task by id={}. SQL was: {}", id, sql, e);
        }
        return Optional.empty();
    }

    public boolean update(Task task, int id){
        final String sql = """
            UPDATE tasks SET title = :title, description = :description, status = :status
            WHERE id = :id
            """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("title", task.getTitle());
        params.addValue("description", task.getDescription());
        params.addValue("status", task.getTitle());
        try {
            return jdbcTemplate.update(sql, params) > 0;
        } catch (DataAccessException e){
            logger.error("Failed to update task by id={}. SQL was: {}", id, sql, e);
        }
        return false;
    }

    public boolean delete(int id){
        final String sql = """
             DELETE FROM tasks
             WHERE id = :id
             """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        try {
            return jdbcTemplate.update(sql, params) > 0;
        } catch (DataAccessException e){
            logger.error("Failed to delete task by id={}. SQL was: {}", id, sql, e);
        }
        return false;
    }
}
