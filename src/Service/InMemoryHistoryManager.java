package Service;

import Service.HistoryManager;
import Tasks.Task;

import java.util.ArrayList;
import java.util.List;
//Класс менеджер истории, реализует интерфейс HistoryManager
public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> taskHistory = new ArrayList<>();

    protected InMemoryHistoryManager() {}

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        taskHistory.add(task);

        int maxSize = 10;
        if (taskHistory.size() > maxSize) {
            taskHistory.remove(0); // Удаляем первый элемент
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskHistory; // Возвращаем историю задач
    }
}