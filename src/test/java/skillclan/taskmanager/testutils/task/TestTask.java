package skillclan.taskmanager.testutils.task;

import skillclan.taskmanager.model.Task;
import skillclan.taskmanager.model.TaskStatus;

public final class TestTask {

    private TestTask(){
    }

    public static Task getTask(){
        Task task = new Task();
        task.setId(10);
        task.setTitle("TestTaskTitle");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setDescription("This is a description of the test task");
        return task;
    }

    public static Task getTaskWithoutID(){
        Task task = new Task();
        task.setTitle("TestTaskTitle");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setDescription("This is a description of the test task");
        return task;
    }
}
