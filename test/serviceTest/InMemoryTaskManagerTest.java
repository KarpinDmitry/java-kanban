package serviceTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.Managers;
import service.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    InMemoryTaskManager taskManager;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    public void beforeEach() {
        taskManager = createManager();
        task = new Task("task1", "1", TaskStatus.NEW, Duration.ofMinutes(90),
                LocalDateTime.of(2024, 6, 2, 10, 0));
        taskManager.createTask(task);
        epic = new Epic("epic1", "1");
        taskManager.createEpic(epic);
        subtask = new Subtask("subtask1", "1", epic.getId(), TaskStatus.NEW,
                Duration.ofMinutes(90), LocalDateTime.of(2021, 6, 2, 10, 0));
        taskManager.createSubtask(subtask);
    }

    @Test
    public void shouldBeEqualsIdIfTaskEquals() {
        Task task1 = taskManager.getTask(task.getId());
        Task task2 = taskManager.getTask(task.getId());

        assertEquals(task1, task2);
    }

    @Test
    public void shouldBeEqualsIdIfEpicEquals() {
        Epic epic1 = taskManager.getEpic(epic.getId());
        Epic epic2 = taskManager.getEpic(epic.getId());

        assertEquals(epic1, epic2);
    }

    @Test
    public void shouldBeEqualsIdIfSubtaskEquals() {
        Subtask subtask1 = taskManager.getSubtask(subtask.getId());
        Subtask subtask2 = taskManager.getSubtask(subtask.getId());

        assertEquals(subtask1, subtask2);
    }

    @Test
    public void tasksNotChangeWhenAddingToManager() {
        assertEquals(task.getName(), taskManager.getTask(task.getId()).getName());
        assertEquals(task.getDescription(), taskManager.getTask(task.getId()).getDescription());
        assertEquals(task.getStatus(), taskManager.getTask(task.getId()).getStatus());
    }

    @Test
    public void workHistoryManager() {
        ArrayList<Task> tasks1 = new ArrayList<>();
        tasks1.add(taskManager.getTask(task.getId()));
        tasks1.add(taskManager.getEpic(epic.getId()));
        tasks1.add(taskManager.getSubtask(subtask.getId()));

        List<Task> tasks2 = taskManager.getHistory();
        assertEquals(tasks1, tasks2);
    }

    @Test
    public void shouldBeSaveAllVersionOfTheTask() {
        Task task1 = taskManager.getTask(task.getId());
        Task task2 = new Task("newTask1", task1.getDescription(), task1.getStatus(),
                Duration.ofMinutes(90), LocalDateTime.of(2023, 6,
                2, 10, 0));
        taskManager.createTask(task2);
        taskManager.getTask(task2.getId());
        taskManager.getTask(task1.getId());

        Assertions.assertNotEquals(taskManager.getHistory().get(0), taskManager.getHistory().get(1));
        assertEquals("task1", taskManager.getHistory().get(2).getName());
        assertEquals("newTask1", taskManager.getHistory().get(1).getName());
    }

    @Test
    public void testCreateAndGetTask() {
        assertEquals(task, taskManager.getTask(task.getId()));
    }

    @Test
    public void testCreateAndGetEpic() {
        assertEquals(epic, taskManager.getEpic(epic.getId()));
    }

    @Test
    public void testCreateAndGetSubtask() {
        assertEquals(subtask, taskManager.getSubtask(subtask.getId()));
    }

    @Test
    public void testGetListSubtask() {
        List<Subtask> subtasks = taskManager.getListSubtask(epic);
        assertTrue(subtasks.contains(subtask));
    }

    @Test
    public void testUpdateSubtask() {
        Subtask updatedSubtask = new Subtask("subtask1_updated", subtask.getDescription(),
                subtask.getIdParentEpic(),
                TaskStatus.DONE, subtask.getId(), Duration.ofMinutes(90), LocalDateTime.of(2025, 6,
                2, 10, 0));
        taskManager.updateSubtask(updatedSubtask);
        assertEquals(updatedSubtask, taskManager.getSubtask(subtask.getId()));
    }

    @Test
    public void testClearSubtaskMap() {
        taskManager.clearSubtaskMap();
        assertTrue(taskManager.getListSubtask(epic).isEmpty());
    }

    @Test
    public void testUpdateEpic() {
        Epic updatedEpic = new Epic(epic.getName(), "Updated description", taskManager.getListSubtask(epic), epic.getId());
        taskManager.updateEpic(updatedEpic);
        assertEquals(updatedEpic, taskManager.getEpic(epic.getId()));
    }

    @Test
    public void testDeleteSubtaskById() {
        taskManager.deleteSubtaskById(subtask.getId());
        assertNull(taskManager.getSubtask(subtask.getId()));
    }

    @Test
    public void testGetId() {
        assertNotNull(taskManager.getEpic(epic.getId()));
    }

    @Test
    public void directSetStatusShouldNotAffectManagerLogic() {
        subtask.setStatus(TaskStatus.DONE);
        assertNotEquals(TaskStatus.DONE, taskManager.getEpic(epic.getId()).getStatus(),
                "Эпик не должен сам обновляться без updateSubtask()");
    }

    @Test
    public void deletedSubtaskShouldNotBeReturned() {
        taskManager.deleteSubtaskById(subtask.getId());
        assertNull(taskManager.getSubtask(subtask.getId()));
    }

    @Test
    public void subtaskShouldBeRemovedFromEpicAfterDeletion() {
        taskManager.deleteSubtaskById(subtask.getId());
        List<Subtask> subtasks = taskManager.getListSubtask(epic);
        assertFalse(subtasks.contains(subtask));
    }

    @Test
    void shouldNotAllowExternalModificationOfTaskStatus() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Test", "Test description", Duration.ofMinutes(90),
                LocalDateTime.of(2024, 6,
                        2, 10, 0));
        manager.createTask(task);
        int id = task.getId();

        Task retrievedTask = manager.getTask(id);
        retrievedTask.setStatus(TaskStatus.DONE);

        Task again = manager.getTask(id);
        assertEquals(TaskStatus.NEW, again.getStatus(), "Статус задачи изменён без ведома менеджера!");
    }

    @Test
    void shouldThrowExceptionWhenTaskTimesOverlap() {
        Task task1 = new Task("Task1", "desc", TaskStatus.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2023, 1, 1, 10, 0));
        Task task2 = new Task("Task2", "desc", TaskStatus.NEW, Duration.ofMinutes(30),
                LocalDateTime.of(2023, 1, 1, 10, 30));

        taskManager.createTask(task1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskManager.createTask(task2));
        assertEquals("Задача пересекается по времени с уже существующей", exception.getMessage());
    }

    @Test
    void shouldUpdateEpicStatusAndTimeFields() {
        Epic epic = new Epic("Epic", "desc");
        taskManager.createEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "desc", epic.getId(), TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2023, 1, 1, 9, 0));
        Subtask sub2 = new Subtask("Sub2", "desc", epic.getId(), TaskStatus.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2023, 1, 1, 11, 0));

        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);

        Epic updatedEpic = taskManager.getEpic(epic.getId());

        assertEquals(TaskStatus.NEW, updatedEpic.getStatus());
        assertEquals(LocalDateTime.of(2023, 1, 1, 9, 0), updatedEpic.getStartTime());
        assertEquals(LocalDateTime.of(2023, 1, 1, 12, 0), updatedEpic.getEndTime().get());
        assertEquals(Duration.ofMinutes(90), updatedEpic.getDuration());

        sub1.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(sub1);

        updatedEpic = taskManager.getEpic(epic.getId());
        assertEquals(TaskStatus.IN_PROGRESS, updatedEpic.getStatus());
    }

    @Test
    void shouldNotFailWhenDeletingNonExistentSubtask() {
        assertDoesNotThrow(() -> taskManager.deleteSubtaskById(999));
    }

    @Test
    void shouldNotFailWhenDeletingNonExistentEpic() {
        assertDoesNotThrow(() -> taskManager.deleteEpicById(999));
    }

    @Test
    void clearSubtaskMapShouldRemoveAllSubtasksAndUpdateEpics() {
        Epic epic = new Epic("Epic", "desc");
        taskManager.createEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "desc", epic.getId(), TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2023, 1, 1, 9, 0));
        taskManager.createSubtask(sub1);

        taskManager.clearSubtaskMap();

        assertTrue(taskManager.getSubtaskList().isEmpty());
        assertEquals(0, taskManager.getListSubtask(epic).size());

        Epic updatedEpic = taskManager.getEpic(epic.getId());
        assertEquals(TaskStatus.NEW, updatedEpic.getStatus());
        assertNull(updatedEpic.getStartTime());
        assertTrue(updatedEpic.getEndTime().isEmpty());
        assertEquals(Duration.ZERO, updatedEpic.getDuration());
    }

    @Override
    protected InMemoryTaskManager createManager() {
        return (InMemoryTaskManager) Managers.getDefault();
    }
}
