package server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        String msg = "Not Found";
        h.sendResponseHeaders(404, msg.length());
        h.getResponseBody().write(msg.getBytes(StandardCharsets.UTF_8));
        h.close();
    }

    protected void sendServerError(HttpExchange h, String error) throws IOException {
        h.sendResponseHeaders(500, error.length());
        h.getResponseBody().write(error.getBytes(StandardCharsets.UTF_8));
        h.close();
    }

    protected void sendConflict(HttpExchange h, String message) throws IOException {
        h.sendResponseHeaders(406, message.length());
        h.getResponseBody().write(message.getBytes(StandardCharsets.UTF_8));
        h.close();
    }
}
