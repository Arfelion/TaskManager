package skillclan.taskmanager.repository;

import org.springframework.jdbc.core.ResultSetExtractor;
import skillclan.taskmanager.model.Task;
import skillclan.taskmanager.model.TaskStatus;
import skillclan.taskmanager.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class TaskUsersExtractor implements ResultSetExtractor<Task> {

    @Override
    public Task extractData(ResultSet rs) throws SQLException {
        Task task = new Task();
        List<User> assignedUsers = new ArrayList<>();
        boolean firstRow = true;

        while (rs.next()) {
            if (firstRow) {
                task.setId(rs.getInt("task_id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setStatus(TaskStatus.valueOf(rs.getString("status")));
                firstRow = false;
            }

            if (rs.getObject("user_id") != null) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phone_number"));
                assignedUsers.add(user);
            }
        }
        task.setAssignUsers(assignedUsers);
        return task;
    }
}
