package serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    protected abstract T createManager() throws IOException;

    @BeforeEach
    public void setup() throws IOException {
        taskManager = createManager();
        task = new Task("Task", "Desc", Duration.ofMinutes(60), LocalDateTime.of(2024, 6, 2, 10, 0));
        epic = new Epic("Epic", "EpicDesc");
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        subtask = new Subtask("Subtask", "SubDesc", epic.getId(), TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.of(2024, 6, 2, 12, 0));
        taskManager.createSubtask(subtask);
    }

    @Test
    void testGetTaskById() {
        assertEquals(task, taskManager.getTask(task.getId()));
    }

    @Test
    void testGetEpicById() {
        assertEquals(epic, taskManager.getEpic(epic.getId()));
    }

    @Test
    void testGetSubtaskById() {
        assertEquals(subtask, taskManager.getSubtask(subtask.getId()));
    }

    @Test
    void testHistoryAfterGet() {
        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getSubtask(subtask.getId());
        assertEquals(3, taskManager.getHistory().size());
    }
}
