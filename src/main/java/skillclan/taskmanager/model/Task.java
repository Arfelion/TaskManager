package skillclan.taskmanager.model;

import java.util.List;
import java.util.Objects;

public class Task {
    private Integer id;
    private String title;
    private String description;
    private TaskStatus status;
    private List<User> assignUsers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public List<User> getAssignUsers() {
        return assignUsers;
    }

    public void setAssignUsers(List<User> assignUsers) {
        this.assignUsers = assignUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title) && Objects.equals(description, task.description) && status == task.status && Objects.equals(assignUsers, task.assignUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status, assignUsers);
    }
}
