package Tasks;

import Service.HistoryManager;

import java.util.ArrayDeque;
import java.util.Deque;

public class InMemoryHistoryManager implements HistoryManager {

    private Deque<Task> taskHistory = new ArrayDeque<>();
    @Override
    public void add(Task task) {
        if (task == null){
            return;
        }
        int maxSize = 10;
        if (taskHistory.size() == maxSize){
            taskHistory.pollFirst();
        }
        taskHistory.addLast(task);
    }

    @Override
    public Deque<Task> getHistory() {
        return taskHistory;
    }
}
