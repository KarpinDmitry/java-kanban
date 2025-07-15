package service;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

//Интерфейс для управления задачами
public interface TaskManager {
    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

    // Таски
    List<Task> getTaskList();

    void clearTaskMap();

    Task getTask(int id);

    void createTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(int id);

    //Сабтаски
    List<Subtask> getSubtaskList();

    void clearSubtaskMap();

    Subtask getSubtask(int id);

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtaskById(int id);

    //Эпики
    List<Epic> getEpicList();

    void clearEpicMap();

    Epic getEpic(int id);

    void createEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpicById(int id);

    List<Subtask> getListSubtask(Epic epic);

}
