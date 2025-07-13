package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public EpicHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");

            if (path.equals("/epics") || path.equals("/epics/")) {
                switch (method) {
                    case "GET" -> handleGetAll(exchange);
                    case "POST" -> handlePost(exchange);
                    default -> exchange.sendResponseHeaders(405, 0);
                }
                return;
            }

            if (pathParts.length == 3 && pathParts[1].equals("epics")) {
                int id = Integer.parseInt(pathParts[2]);
                switch (method) {
                    case "GET" -> handleGetById(exchange, id);
                    case "DELETE" -> handleDelete(exchange, id);
                    default -> exchange.sendResponseHeaders(405, 0);
                }
                return;
            }

            if (pathParts.length == 4 && pathParts[3].equals("subtasks")) {
                int id = Integer.parseInt(pathParts[2]);
                if ("GET".equals(method)) {
                    handleGetSubtasks(exchange, id);
                } else {
                    exchange.sendResponseHeaders(405, 0);
                }
                return;
            }

            sendNotFound(exchange);

        } catch (Exception e) {
            sendServerError(exchange, "Ошибка обработки запроса: " + e.getMessage());
        }
    }

    private void handleGetAll(HttpExchange exchange) throws IOException {
        List<Epic> epics = manager.getEpicList();
        sendText(exchange, gson.toJson(epics));
    }

    private void handleGetById(HttpExchange exchange, int id) throws IOException {
        Epic epic = manager.getEpic(id);
        if (epic != null) {
            sendText(exchange, gson.toJson(epic));
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(body, Epic.class);

        if (epic.getId() == 0 || manager.getEpic(epic.getId()) == null) {
            manager.createEpic(epic);
        } else {
            manager.updateEpic(epic);
        }
        sendText(exchange, gson.toJson(epic));
    }

    private void handleDelete(HttpExchange exchange, int id) throws IOException {
        manager.deleteEpicById(id);
        sendText(exchange, "Эпик удалён");
    }

    private void handleGetSubtasks(HttpExchange exchange, int epicId) throws IOException {
        List<Subtask> subtasks = manager.getListSubtask(manager.getEpic(epicId));
        if (subtasks != null) {
            sendText(exchange, gson.toJson(subtasks));
        } else {
            sendNotFound(exchange);
        }
    }
}
