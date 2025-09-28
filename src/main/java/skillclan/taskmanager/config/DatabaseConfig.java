package skillclan.taskmanager.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {

    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final int poolSize;

    public DatabaseConfig(
            @Value("${spring.datasource.url}") String dbUrl,
            @Value("${spring.datasource.username}") String dbUsername,
            @Value("${spring.datasource.password}") String dbPassword,
            @Value("${spring.datasource.hikari.maximum-pool-size:10}") int poolSize
    ) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.poolSize = poolSize;
    }
    @Bean
    public DataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        config.setMaximumPoolSize(poolSize);
        config.setMinimumIdle(5);
        config.setPoolName("TaskManager-Hikari-Pool");

        return new HikariDataSource(config);
    }
    public static Connection getConnection() throws SQLException {
        throw new UnsupportedOperationException("Використовуємо DataSource замість getConnection");
    }
}
