//package skillclan.taskmanager.repository;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.dao.DataAccessException;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.stereotype.Repository;
//import skillclan.taskmanager.model.Task;
//
//import java.util.*;
//
//@Repository
//public class UserTaskRepository {
//
//    private static final Logger logger = LoggerFactory.getLogger(UserTaskRepository.class);
//
//    private final NamedParameterJdbcTemplate jdbcTemplate;
//
//    public UserTaskRepository(NamedParameterJdbcTemplate jdbcTemplate){
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public boolean assignTaskToUser(Integer taskId, Integer userId) {
//        final String sql = """
//                              INSERT INTO user_tasks (user_id, task_id)
//                              SELECT :user_id, :task_id
//                              WHERE NOT EXISTS (
//                                SELECT 1 FROM user_tasks
//                                WHERE user_id = :user_id AND task_id = :task_id)
//                              """;
//        MapSqlParameterSource params = new MapSqlParameterSource();
//        params.addValue("user_id", userId);
//        params.addValue("task_id", taskId);
//        try {
//            return jdbcTemplate.update(sql, params) > 0;
//        } catch (DataAccessException e){
//            logger.error("Failed to assign taskId={} to userId={}. SQL was: {}", taskId, userId, sql, e);
//        }
//        return false;
//    }
//
//    public Optional<Task> assignTaskToUsers(Integer taskId, Integer[] userIds) {
//        final List<Integer> userIdList = Arrays.stream(userIds).toList();
//        final String insertSql = """
//            INSERT INTO user_tasks (user_id, task_id)
//            SELECT u.id, :taskId
//            FROM users u
//            WHERE u.id = ANY(:userIds)
//            ON CONFLICT (user_id, task_id) DO NOTHING;
//            """;
//        MapSqlParameterSource insertParams = new MapSqlParameterSource();
//        insertParams.addValue("user_ids", userIdList);
//        insertParams.addValue("task_id", taskId);
//        try {
//            jdbcTemplate.update(insertSql, insertParams);
//        } catch (DataAccessException e){
//            logger.error("Failed to assign taskId={} to userIds={}. SQL was: {}", taskId, userIds, insertSql, e);
//        }
//        final String sql = """
//            SELECT
//                t.id AS task_id, t.title, t.description, t.status,
//                u.id AS user_id, u.name, u.email, u.phone_number
//            FROM tasks t
//            LEFT JOIN user_tasks ut ON t.id = ut.task_id
//            LEFT JOIN users u ON ut.user_id = u.id
//            WHERE t.id = :taskId
//            """;
//        MapSqlParameterSource params = new MapSqlParameterSource();
//        params.addValue("task_id", taskId);
//        try {
//            return Optional.ofNullable(jdbcTemplate.query(sql, params, new TaskUsersExtractor()));
//        } catch (Exception e){
//            logger.error("Failed to retrieve taskId={} with all assign users. SQL was: {}", taskId, sql, e);
//        }
//        return Optional.empty();
//    }
//
//    public boolean unassignTaskFromUser(Integer taskId, Integer userId) {
//        final String sql = """
//                              DELETE FROM user_tasks
//                              WHERE user_id = :user_id AND task_id = :task_id
//                              """;
//        MapSqlParameterSource params = new MapSqlParameterSource();
//        params.addValue("user_id", userId);
//        params.addValue("task_id", taskId);
//        try {
//            return jdbcTemplate.update(sql, params) > 0;
//        } catch (DataAccessException e){
//            logger.error("Failed to unassign taskId={} from userId={}. SQL was: {}", taskId, userId, sql, e);
//        }
//        return false;
//    }
//}
