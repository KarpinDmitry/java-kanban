package Service;
import Tasks.Task;
import java.util.Deque;
import java.util.List;
//Интерфейс для управления историей задач
public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    List<Task> getHistory();
}
