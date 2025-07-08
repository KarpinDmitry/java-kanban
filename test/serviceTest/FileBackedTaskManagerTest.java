package serviceTest;

import org.junit.jupiter.api.Test;
import service.FileBackedTaskManager;
import service.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private Path tempFile;

    FileBackedTaskManager loaded = createManager();

    FileBackedTaskManagerTest() throws IOException {
    }


    @Override
    protected FileBackedTaskManager createManager() throws IOException {
        tempFile = Files.createTempFile("test_tasks", ".csv");
        return (FileBackedTaskManager) Managers.getFileBackTaskManager();
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

        Task task = new Task("Task1", "Description1", TaskStatus.NEW, Duration.ofMinutes(90),
                LocalDateTime.of(2025, 6, 30, 10, 0));
        manager.createTask(task);

        Epic epic = new Epic("Epic1", "EpicDescription");
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask1", "SubDesc", epic.getId(), TaskStatus.DONE, Duration.ofMinutes(90),
                LocalDateTime.of(2024, 6, 30, 10, 0));
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
            manager.createTask(new Task("Task" + i, "Desc" + i, TaskStatus.NEW, Duration.ofMinutes(90 + i),
                    LocalDateTime.of(2022, 6 + i, 30, 10, i)));
        }

        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);
        List<Task> tasks = loaded.getTaskList();

        assertEquals(5, tasks.size());
        assertEquals("Task0", tasks.get(0).getName());
        assertEquals("Task4", tasks.get(4).getName());
    }
}
