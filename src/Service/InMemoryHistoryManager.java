package Service;

import Service.HistoryManager;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Класс менеджер истории, реализует интерфейс HistoryManager
public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Node> idNodeMap = new HashMap<>();
    private Node head;
    private Node tail;

    public InMemoryHistoryManager() {
    }

    @Override
    public void add(Task task) {
        if (task == null) return;

        remove(task.getId());

        Node newNode = linkTask(task);
        idNodeMap.put(task.getId(), newNode);
    }

    public void remove(int id) {
        Node node = idNodeMap.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks(); // Возвращаем историю задач
    }

    public Node linkTask(Task task) {
        Node newNode;
        if (head == null) {
            newNode = new Node(null, task, null);
            head = newNode;
            tail = head;
        } else {
            newNode = new Node(tail, task, null);
            tail.nextTask = newNode;
            tail = newNode;
        }
        return newNode;
    }

    private void removeNode(Node node) {
        if (node.prevTask != null) {
            node.prevTask.nextTask = node.nextTask;
        } else {
            head = node.nextTask;
        }

        if (node.nextTask != null) {
            node.nextTask.prevTask = node.prevTask;
        } else {
            tail = node.prevTask;
        }
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null) {
            tasks.add(currentNode.task);
            currentNode = currentNode.nextTask;
        }
        return tasks;
    }

    private static class Node {
        Task task;
        Node nextTask;
        Node prevTask;

        public Node(Node prevTask, Task task, Node nextTask) {
            this.task = task;
            this.nextTask = nextTask;
            this.prevTask = prevTask;
        }
    }
}