package skillclan.taskmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import skillclan.taskmanager.dto.TaskDto;
import skillclan.taskmanager.model.Task;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TaskMapper {

    TaskDto taskToTaskDto(Task task);

    @Mapping(target = "id", ignore = true)
    Task taskDtoToTask(TaskDto taskDto);

}
