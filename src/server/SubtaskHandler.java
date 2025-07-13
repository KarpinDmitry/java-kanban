package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;

    public SubtaskHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        try {
            if ("/subtasks".equals(path)) {
                handleAllSubtask(exchange, method);
            } else if (path.startsWith("/subtasks/")) {
                handleSubtaskById(exchange, method);
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendServerError(exchange, "Server error: " + e.getMessage());
        }
    }

    private void handleAllSubtask(HttpExchange exchange, String method) throws IOException {
        switch (method) {
            case "GET" -> sendText(exchange, gson.toJson(manager.getSubtaskList()));
            case "POST" -> {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                try {
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    if (subtask.getId() == 0) {
                        manager.createSubtask(subtask);
                    } else {
                        manager.updateSubtask(subtask);
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

    private void handleSubtaskById(HttpExchange exchange, String method) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String idStr = path.replace("/subtasks/", "");
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            sendServerError(exchange, "Invalid ID format: " + idStr);
            return;
        }

        switch (method) {
            case "GET" -> {
                Subtask subtask = manager.getSubtask(id);
                if (subtask != null) {
                    sendText(exchange, gson.toJson(subtask));
                } else {
                    sendNotFound(exchange);
                }
            }
            case "DELETE" -> {
                Subtask subtask = manager.getSubtask(id);
                if (subtask != null) {
                    manager.deleteSubtaskById(id);
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
