package service;

import tasks.Task;

import java.util.List;

//Интерфейс для управления историей задач
public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    List<Task> getHistory();
}
