package skillclan.taskmanager.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import skillclan.taskmanager.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> create(User user) {
        final String sql = """
             INSERT INTO users (name, email, phone_number)
             VALUES (:name, :email, :phone_number)
             RETURNING id
             """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", user.getName());
        params.addValue("email", user.getEmail());
        params.addValue("phone_number", user.getPhoneNumber());
        try {
            user.setId(jdbcTemplate.queryForObject(sql, params, Integer.class));
            return Optional.of(user);
        } catch (DataAccessException e){
            logger.error("Failed to create user. SQL was: {}", sql, e);
        }
        return Optional.empty();
    }

    public Optional<User> findById(int id){
        final String sql = """
             SELECT id, email, name, phone_number FROM users
             WHERE id = :id
             """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        try {
            User user = jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(User.class));
            return Optional.ofNullable(user);
        } catch (DataAccessException e){
            logger.error("Failed to retrieve user by id={}. SQL was: {}", id, sql, e);
        }
        return Optional.empty();
    }

    public List<User> findAll(){
        final String sql = "SELECT id, email, name, phone_number FROM users";
        try {
            return jdbcTemplate.query(sql, Collections.emptyMap(), new BeanPropertyRowMapper<>(User.class));
        } catch (DataAccessException e){
            logger.error("Failed to retrieve all users. SQL was: {}", sql, e);
        }
        return Collections.emptyList();
    }

    public boolean update(User user, int id){
        final String sql = """
            UPDATE users SET name = :name, email = :email, phone_number = :phone_number
            WHERE id = :id
            """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("name", user.getName());
        params.addValue("email", user.getEmail());
        params.addValue("phone_number", user.getPhoneNumber());
        try {
           return jdbcTemplate.update(sql, params) > 0;
        } catch (DataAccessException e){
            logger.error("Failed to update user by id={}. SQL was: {}", id, sql, e);
        }
        return false;
    }
    public boolean delete(int id){
        final String sql = """
            DELETE FROM users
            WHERE id = :id
            """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        try{
            return jdbcTemplate.update(sql, params) > 0;
        } catch (DataAccessException e){
            logger.error("Failed to delete user by id={}. SQL was: {}", id, sql, e);
        }
        return false;
    }
}
