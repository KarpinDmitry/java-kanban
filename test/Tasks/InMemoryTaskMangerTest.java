package Tasks;

import Service.TaskManager;
import Service.TaskStatus;
import Tasks.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskMangerTest {
    TaskManager taskManager;
    Task task;
    Epic epic;
    Subtask subtask;
    @BeforeEach
    public void beforeEach(){
        taskManager = Managers.getDefault();
        task = new Task("task1","1", TaskStatus.NEW);
        taskManager.createTask(task);
        epic = new Epic("epic1","1");
        taskManager.createEpic(epic);
        subtask = new Subtask("subtask1","1",epic.getId(),TaskStatus.NEW);
        taskManager.createSubtask(subtask);
    }
    @Test
    public void shouldBeEqualsIdIfTaskEquals(){
        Task task1 = taskManager.getTask(task.getId());
        Task task2 = taskManager.getTask(task.getId());

        assertEquals(task1, task2);
    }
    @Test
    public void shouldBeEqualsIdIfEpicEquals(){
        Epic epic1 = taskManager.getEpic(epic.getId());
        Epic epic2 = taskManager.getEpic(epic.getId());

        assertEquals(epic1,epic2);
    }
    @Test
    public void shouldBeEqualsIdIfSubtaskEquals(){
        Subtask subtask1 = taskManager.getSubtask(subtask.getId());
        Subtask subtask2 = taskManager.getSubtask(subtask.getId());

        assertEquals(subtask1,subtask2);
    }
    @Test
    public void tasksNotChangeWhenAddingToManager(){
        assertEquals(task.getName(),taskManager.getTask(task.getId()).getName());
        assertEquals(task.getDescription(),taskManager.getTask(task.getId()).getDescription());
        assertEquals(task.getStatus(),taskManager.getTask(task.getId()).getStatus());
    }
    @Test
    public void workHistoryManager(){
        ArrayList<Task> tasks1 = new ArrayList<>();
        tasks1.add(taskManager.getTask(task.getId()));
        tasks1.add(taskManager.getEpic(epic.getId()));
        tasks1.add(taskManager.getSubtask(subtask.getId()));

        List<Task> tasks2 = taskManager.getHistory();
        assertEquals(tasks1,tasks2);
    }
    @Test
    public void shouldBeSaveAllVersionOfTheTask(){
        Task task1 = taskManager.getTask(task.getId());
        Task task2 = new Task("newTask1",task1.getDescription(),task1.getStatus());
        taskManager.createTask(task2);
        taskManager.getTask(task2.getId());

        Assertions.assertNotEquals(taskManager.getHistory().get(0),taskManager.getHistory().get(1));
        assertEquals("task1",taskManager.getHistory().get(0).getName());
        assertEquals("newTask1",taskManager.getHistory().get(1).getName());
    }
    @Test
    public void testHistorySizeLimit() {
        for (int i = 1; i <= 15; i++) {
            Task task = new Task("Task " + i, "Description " + i);
            taskManager.createTask(task);
            taskManager.getTask(task.getId());
        }

        List<Task> history = taskManager.getHistory();
        assertEquals(10, history.size());
        assertEquals("Task 6", history.get(0).getName());
        assertEquals("Task 15", history.get(9).getName());
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
        Subtask updatedSubtask = new Subtask("subtask1_updated", subtask.getDescription(), subtask.getIdParentEpic(), TaskStatus.DONE, subtask.getId());
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
}
