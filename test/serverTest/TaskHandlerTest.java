package serverTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.DurationAdapter;
import server.HttpTaskServer;
import server.LocalDateTimeAdapter;
import service.Managers;
import service.TaskManager;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskHandlerTest {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private final HttpClient client = HttpClient.newHttpClient();
    private HttpTaskServer taskServer;
    private TaskManager manager;

    @BeforeEach
    public void setUp() throws IOException {
        manager = Managers.getDefault();
        taskServer = new HttpTaskServer(manager);
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Description", TaskStatus.NEW,
                Duration.ofMinutes(10), LocalDateTime.now());
        String json = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasks = manager.getTaskList();
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getName());
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        Task task = new Task("Task for Get", "Desc", TaskStatus.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        manager.createTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertTrue(tasks.length > 0);
        assertEquals("Task for Get", tasks[0].getName());
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task("Single Task", "Desc", TaskStatus.NEW,
                Duration.ofMinutes(7), LocalDateTime.now());
        manager.createTask(task);
        List<Task> tasks = manager.getTaskList();
        assertFalse(tasks.isEmpty());
        int id = tasks.getLast().getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + id))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task returnedTask = gson.fromJson(response.body(), Task.class);
        assertEquals(task.getName(), returnedTask.getName());
        assertEquals(task.getDescription(), returnedTask.getDescription());
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Old Task", "Desc", TaskStatus.NEW,
                Duration.ofMinutes(8), LocalDateTime.now());
        manager.createTask(task);
        List<Task> tasks = manager.getTaskList();
        assertFalse(tasks.isEmpty());
        int id = tasks.getLast().getId();

        Task updatedTask = new Task("Updated Task", "New Desc", TaskStatus.IN_PROGRESS,
                Duration.ofMinutes(8), LocalDateTime.now());
        updatedTask.setId(id);
        String json = gson.toJson(updatedTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Task taskFromManager = manager.getTask(id);
        assertEquals("Updated Task", taskFromManager.getName());
        assertEquals(TaskStatus.IN_PROGRESS, taskFromManager.getStatus());
    }

    @Test
    public void testDeleteTaskById() throws IOException, InterruptedException {
        Task task = new Task("Task to Delete", "Desc", TaskStatus.NEW,
                Duration.ofMinutes(10), LocalDateTime.now());
        manager.createTask(task);
        List<Task> tasks = manager.getTaskList();
        assertFalse(tasks.isEmpty());
        int id = tasks.getLast().getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertNull(manager.getTask(id));
    }
}
