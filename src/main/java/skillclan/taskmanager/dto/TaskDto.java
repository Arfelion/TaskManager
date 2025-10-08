package skillclan.taskmanager.dto;

import skillclan.taskmanager.model.TaskStatus;

import java.util.List;

public class TaskDto {
    private Integer id;
    private String title;
    private String description;
    private TaskStatus status;
    private List<UserDto> assignUsers;

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

    public List<UserDto> getAssignUsers() {
        return assignUsers;
    }

    public void setAssignUsers(List<UserDto> assignUsers) {
        this.assignUsers = assignUsers;
    }
}
