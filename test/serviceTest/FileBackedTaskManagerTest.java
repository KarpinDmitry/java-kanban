package serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    private Path tempFile;

    @BeforeEach
    void setup() throws IOException {
        tempFile = Files.createTempFile("test_tasks", ".csv");
    }

    @Test
    void testSaveAndLoadEmptyFile() throws IOException {
        // только заголовок
        Files.write(tempFile, List.of("id,type,name,status,description,epic", ""));

        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loaded.getTaskList().isEmpty());
        assertTrue(loaded.getEpicList().isEmpty());
        assertTrue(loaded.getSubtaskList().isEmpty());
    }

    @Test
    void testSaveAndLoadOneTaskEach() {
        // по одной задаче
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        Task task = new Task("Task1", "Description1", TaskStatus.NEW);
        manager.createTask(task);

        Epic epic = new Epic("Epic1", "EpicDescription");
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask1", "SubDesc", epic.getId(), TaskStatus.DONE);
        manager.createSubtask(subtask);
        System.out.println(subtask);
        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(1, loaded.getTaskList().size());
        assertEquals(1, loaded.getEpicList().size());
        assertEquals(1, loaded.getSubtaskList().size());

        assertEquals("Task1", loaded.getTaskList().get(0).getName());
        assertEquals("Epic1", loaded.getEpicList().get(0).getName());
        assertEquals("Subtask1", loaded.getSubtaskList().get(0).getName());

        assertEquals(epic.getId(), loaded.getSubtaskList().get(0).getIdParentEpic());
    }

    @Test
    void testSaveAndLoadMultipleTasks() {
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        for (int i = 0; i < 5; i++) {
            manager.createTask(new Task("Task" + i, "Desc" + i, TaskStatus.NEW));
        }

        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);
        List<Task> tasks = loaded.getTaskList();

        assertEquals(5, tasks.size());
        assertEquals("Task0", tasks.get(0).getName());
        assertEquals("Task4", tasks.get(4).getName());
    }
}
