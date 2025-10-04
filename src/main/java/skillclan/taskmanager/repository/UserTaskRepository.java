package skillclan.taskmanager.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserTaskRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserTaskRepository(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean assignTaskToUser(Integer taskId, Integer userId) {
        final String INSERT = "INSERT INTO user_tasks (user_id, task_id) " +
                              "VALUES (:user_id, :task_id)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        params.addValue("task_id", taskId);
        try {
            if(!isTaskAssignedToUser(taskId, userId)){
                return jdbcTemplate.update(INSERT, params) > 0;
            }
        } catch (Exception e){
            System.out.println("Щось пішло не так під час підключення або виконання запиту назначення таски користувачу в БД: " + e);
        }
        return false;
    }

    public boolean unassignTaskFromUser(Integer taskId, Integer userId) {
        final String DELETE = "DELETE FROM tasks WHERE " +
                              "user_id = :user_id AND task_id = :task_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        params.addValue("task_id", taskId);
        try {
            return jdbcTemplate.update(DELETE, params) > 0;
        } catch (Exception e){
            System.out.println("Щось пішло не так під час підключення або виконання запиту звільнення користувача від таски в БД: " + e);
        }
        return false;
    }

    public boolean isTaskAssignedToUser(Integer taskId, Integer userId) {
        final String SELECT = "SELECT COUNT(1) FROM user_tasks WHERE " + // Або просто SELECT 1 FROM
                              "user_id = :user_id AND task_id = :task_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        params.addValue("task_id", taskId);
        try {
            return jdbcTemplate.update(SELECT, params) > 0;
        } catch (Exception e){
            System.out.println("Щось пішло не так під час підключення або виконання запиту перевірки назначення таски користувачу в БД: " + e);
        }
        return false;
    }
}
