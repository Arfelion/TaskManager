package skillclan.taskmanager.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import skillclan.taskmanager.model.Task;
import skillclan.taskmanager.model.TaskStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private static final Logger logger = LoggerFactory.getLogger(TaskRepository.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final RowMapper<Task> TASK_ROW_MAPPER = (rs, rowCount) -> {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setStatus(TaskStatus.fromDbValue(rs.getString("status")));
        return task;
    };

    public TaskRepository(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Task> create(Task task){
        final String sql = """
            INSERT INTO tasks (title, description, status)
            VALUES (:title, :description, :status)
            """;
        logger.debug("Executing SQL: {}", sql);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", task.getTitle());
        params.addValue("description", task.getDescription());
        params.addValue("status", task.getStatus().getDbValue());
        try {
            jdbcTemplate.update(sql, params, keyHolder, new String[] {"id"});
            task.setId(keyHolder.getKey().intValue());
            return Optional.of(task);
        } catch (DataAccessException e){
            logger.error("Failed to create task. SQL was: {}", sql, e);
        }
        return Optional.empty();
    }

    public List<Task> findAll() {
        final String sql = "SELECT id, title, description, status FROM tasks";
        try {
            return jdbcTemplate.query(sql, Collections.emptyMap(), TASK_ROW_MAPPER);
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
            Task task = jdbcTemplate.queryForObject(sql, params, TASK_ROW_MAPPER);
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
