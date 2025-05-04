package ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Service.InMemoryHistoryManager;
import Tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Task 1", "desc1");
        task1.setId(1);
        task2 = new Task("Task 2", "desc2");
        task2.setId(2);
        task3 = new Task("Task 3", "desc3");
        task3.setId(3);
    }

    @Test
    void addShouldAddTasksInOrder() {
        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    void addShouldMoveTaskToEndIfAlreadyExists() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task1, history.get(1));
    }

    @Test
    void removeShouldDeleteFromStartMiddleEnd() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(1);
        List<Task> afterFirstRemoval = historyManager.getHistory();
        assertEquals(List.of(task2, task3), afterFirstRemoval);

        historyManager.remove(2);
        List<Task> afterSecondRemoval = historyManager.getHistory();
        assertEquals(List.of(task3), afterSecondRemoval);

        historyManager.remove(3);
        List<Task> afterThirdRemoval = historyManager.getHistory();
        assertTrue(afterThirdRemoval.isEmpty());
    }

    @Test
    void getHistoryShouldReturnEmptyListWhenNoTasks() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void addNullTaskShouldNotAffectHistory() {
        historyManager.add(null);
        assertTrue(historyManager.getHistory().isEmpty());
    }
}
