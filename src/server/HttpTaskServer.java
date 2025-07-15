package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import service.Managers;
import service.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager manager;

    private final Gson gson;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        server = HttpServer.create(new InetSocketAddress(PORT), 0);

        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        server.createContext("/tasks", new TaskHandler(manager, gson));
        server.createContext("/subtasks", new SubtaskHandler(manager, gson));
        server.createContext("/epics", new EpicHandler(manager, gson));
        server.createContext("/history", new HistoryHandler(manager, gson));
        server.createContext("/prioritized", new PrioritizedHandler(manager, gson));
    }

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getFileBackTaskManager();
        Task task1 = new Task("task1", "test");
        manager.createTask(task1);
        Task task = new Task("Покупка продуктов", "Купить молоко и хлеб", TaskStatus.NEW,
                1, Duration.ofMinutes(30), LocalDateTime.of(2024, 6, 1, 12, 0));
        manager.createTask(task);

        Epic epic = new Epic("Подготовка к экзамену", "Сдать всё!");
        manager.createEpic(epic);
        manager.getTask(1);

        Subtask subtask1 = new Subtask("Решить задачи", "Теория + практика", epic.getId(),
                TaskStatus.IN_PROGRESS, 3, Duration.ofMinutes(9), LocalDateTime.of(2022, 6,
                1, 12, 0));
        manager.createSubtask(subtask1);
        Subtask subtask = new Subtask("Решить задачи", "Теория + практика", epic.getId(),
                TaskStatus.IN_PROGRESS, 3, Duration.ofMinutes(50), LocalDateTime.of(2024, 7,
                2, 10, 0));
        manager.createSubtask(subtask);

        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на порту " + PORT);
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер остановлен.");
    }
}
