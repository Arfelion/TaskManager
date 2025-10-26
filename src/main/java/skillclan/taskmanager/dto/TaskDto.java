package skillclan.taskmanager.dto;

import jakarta.validation.constraints.*;
import skillclan.taskmanager.model.TaskStatus;

import java.util.List;

public class TaskDto {
    @Null(message = "Id must be null")
    private Integer id;

    @NotBlank(message = "Tittle cannot be empty")
    private String title;

    @Size(max = 500, message = "Description cannot be more than 500 symbols")
    private String description;

    @NotNull(message = "Status cannot be null")
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
