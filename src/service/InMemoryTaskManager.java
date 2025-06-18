package service;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Класс менеджер тасков, реализует интерфейс TaskManager

class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private final Map<Integer, Task> taskMap = new HashMap<>();
    private final Map<Integer, Subtask> subtaskMap = new HashMap<>();
    private final Map<Integer, Epic> epicMap = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    protected InMemoryTaskManager() {
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Таски
    @Override
    public List<Task> getTaskList() {
        List<Task> result = new ArrayList<>();
        for (Task task : taskMap.values()) {
            result.add(new Task(task));
        }
        return result;
    }

    @Override
    public void clearTaskMap() {
        taskMap.clear();
    }

    @Override
    public Task getTask(int id) {
        Task task = taskMap.get(id);
        if (task != null) {
            historyManager.add(task);
            return new Task(task);
        }
        return null;
    }

    @Override
    public void createTask(Task task) {
        task.setId(getId());
        taskMap.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task task) {
        taskMap.replace(task.getId(), task);
    }

    @Override
    public void deleteTaskById(int id) {
        taskMap.remove(id);
        historyManager.remove(id);

    }

    protected void putTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    //Сабтаски
    @Override
    public List<Subtask> getSubtaskList() {
        List<Subtask> result = new ArrayList<>();
        for (Subtask subtask : subtaskMap.values()) {
            result.add(new Subtask(subtask));
        }
        return result;
    }

    @Override
    public void clearSubtaskMap() {
        subtaskMap.clear();
        for (Epic epic : epicMap.values()) {
            epic.setChildrenSubtask(new ArrayList<>());
            updateEpicStatus(epic);
        }
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtaskMap.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
            return new Subtask(subtask);
        }
        return null;
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(getId());
        subtaskMap.put(subtask.getId(), subtask);
        int idParentEpic = subtaskMap.get(subtask.getId()).getIdParentEpic();
        epicMap.get(idParentEpic).addSubtask(subtask);
        updateEpicStatus(epicMap.get(idParentEpic));

    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtaskMap.replace(subtask.getId(), subtask);
        updateEpicStatus(epicMap.get(subtask.getIdParentEpic()));
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtaskMap.get(id);
        if (subtask == null) {
            System.out.println("Подзадача с id=" + id + " не найдена.");
            return;
        }

        int idParentEpic = subtask.getIdParentEpic();
        subtaskMap.remove(id);
        epicMap.get(idParentEpic).deleteSubTask(id);
        updateEpicStatus(epicMap.get(idParentEpic));
        historyManager.remove(id);
    }

    protected void putSubtask(Subtask subtask) {
        subtaskMap.put(subtask.getId(), subtask);
        int idParentEpic = subtask.getIdParentEpic();
        epicMap.get(idParentEpic).addSubtask(subtask);
        updateEpicStatus(epicMap.get(idParentEpic));
    }

    //Эпики
    @Override
    public List<Epic> getEpicList() {
        List<Epic> result = new ArrayList<>();
        for (Epic epic : epicMap.values()) {
            result.add(new Epic(epic));
        }
        return result;
    }

    @Override
    public void clearEpicMap() {
        epicMap.clear();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epicMap.get(id);
        if (epic != null) {
            historyManager.add(epic);
            return new Epic(epic);
        }
        return null;
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(getId());
        epicMap.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        epicMap.replace(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    @Override
    public void deleteEpicById(int id) {
        epicMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<Subtask> getListSubtask(Epic epic) {
        List<Integer> childrenSubtaskId = epic.getChildrenSubtaskId();
        List<Subtask> resultList = new ArrayList<>();
        if (childrenSubtaskId == null) {
            return resultList;
        }
        for (int id : childrenSubtaskId) {
            resultList.add(getSubtask(id));
        }
        return resultList;
    }

    protected void putEpic(Epic epic) {
        epicMap.put(epic.getId(), epic);
    }

    private int getId() {
        id++;
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    private void updateEpicStatus(Epic epic) {
        List<Integer> childrenSubtaskId = epic.getChildrenSubtaskId();
        boolean flagNew = true;
        boolean flagDone = true;

        if (childrenSubtaskId == null) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        for (int id : childrenSubtaskId) {
            Subtask subtask = subtaskMap.get(id);
            if (subtask.getStatus() != TaskStatus.NEW) {
                flagNew = false;
            }
            if (subtask.getStatus() != TaskStatus.DONE) {
                flagDone = false;
            }
        }
        if (flagNew) {
            epic.setStatus(TaskStatus.NEW);
        } else if (flagDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

}