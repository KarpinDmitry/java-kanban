package Tasks;

import Service.HistoryManager;

import java.util.ArrayList;
import java.util.List;

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