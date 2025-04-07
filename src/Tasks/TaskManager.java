package Tasks;

import java.util.ArrayList;
import java.util.Deque;

public interface TaskManager {
    int getId();

    Deque<? extends Task> getHistory();
    // Таски
    ArrayList<Task> getTaskList();

    void clearTaskMap();

    Task getTask(int id);

    void createTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(int id);

    //Сабтаски
    ArrayList<Subtask> getSubtaskList();

    void clearSubtaskMap();

    Subtask getSubtask(int id);

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtaskById(int id);

    //Эпики
    ArrayList<Epic> getEpicList();

    void clearEpicMap();

    Epic getEpic(int id);

    void createEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpicById(int id);

    ArrayList<Subtask> getListSubtask(Epic epic);

    void updateEpicStatus(Epic epic);
}
