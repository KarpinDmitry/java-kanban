package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public TaskHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        try {
            if ("/tasks".equals(path)) {
                handleAllTasks(exchange, method);
            } else if (path.startsWith("/tasks/")) {
                handleTaskById(exchange, method);
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendServerError(exchange, "Server error: " + e.getMessage());
        }
    }

    private void handleAllTasks(HttpExchange exchange, String method) throws IOException {
        switch (method) {
            case "GET" -> sendText(exchange, gson.toJson(manager.getTaskList()));
            case "POST" -> {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                try {
                    Task task = gson.fromJson(body, Task.class);
                    if (task.getId() == 0) {
                        manager.createTask(task);
                    } else {
                        manager.updateTask(task);
                    }
                    exchange.sendResponseHeaders(201, 0);
                    exchange.close();
                } catch (JsonSyntaxException e) {
                    sendServerError(exchange, "Invalid JSON: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    sendConflict(exchange, e.getMessage());
                }
            }
            default -> sendNotFound(exchange);
        }
    }

    private void handleTaskById(HttpExchange exchange, String method) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String idStr = path.replace("/tasks/", "");
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            sendServerError(exchange, "Invalid ID format: " + idStr);
            return;
        }

        switch (method) {
            case "GET" -> {
                Task task = manager.getTask(id);
                if (task != null) {
                    sendText(exchange, gson.toJson(task));
                } else {
                    sendNotFound(exchange);
                }
            }
            case "DELETE" -> {
                Task task = manager.getTask(id);
                if (task != null) {
                    manager.deleteTaskById(id);
                    exchange.sendResponseHeaders(200, 0);
                    exchange.close();
                } else {
                    sendNotFound(exchange);
                }
            }
            default -> sendNotFound(exchange);
        }
    }
}
