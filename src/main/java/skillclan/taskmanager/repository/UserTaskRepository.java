package skillclan.taskmanager.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserTaskRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserTaskRepository.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserTaskRepository(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean assignTaskToUser(Integer taskId, Integer userId) {
        final String sql = """
                              INSERT INTO user_tasks (user_id, task_id)
                              SELECT :user_id, :task_id
                              WHERE NOT EXISTS (
                                SELECT 1 FROM user_tasks
                                WHERE user_id = :user_id AND task_id = :task_id)
                              """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        params.addValue("task_id", taskId);
        try {
            return jdbcTemplate.update(sql, params) > 0;
        } catch (DataAccessException e){
            logger.error("Failed to assign taskId={} to userId={}. SQL was: {}", taskId, userId, sql, e);
        }
        return false;
    }

    public boolean unassignTaskFromUser(Integer taskId, Integer userId) {
        final String sql = """
                              DELETE FROM user_tasks
                              WHERE user_id = :user_id AND task_id = :task_id
                              """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        params.addValue("task_id", taskId);
        try {
            return jdbcTemplate.update(sql, params) > 0;
        } catch (DataAccessException e){
            logger.error("Failed to unassign taskId={} from userId={}. SQL was: {}", taskId, userId, sql, e);
        }
        return false;
    }
}
