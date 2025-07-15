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
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HistoryHandlerTest {

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
    public void testGetHistory() throws IOException, InterruptedException {
        Task task = new Task("Task for History", "Desc", TaskStatus.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        manager.createTask(task);
        Epic epic = new Epic("Epic for History", "Desc");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask for History", "Desc", epic.getId(), TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 6, 1, 12, 0));
        manager.createSubtask(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertTrue(response.body().startsWith("["));
    }
}
