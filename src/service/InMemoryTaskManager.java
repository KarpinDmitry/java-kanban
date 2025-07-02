package service;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

//Класс менеджер тасков, реализует интерфейс TaskManager

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private final Map<Integer, Task> taskMap = new HashMap<>();
    private final Map<Integer, Subtask> subtaskMap = new HashMap<>();
    private final Map<Integer, Epic> epicMap = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final Set<Task> prioritizedTasks = new TreeSet<>((task1, task2) -> {
        int cmp = task1.getStartTime().compareTo(task2.getStartTime());
        if (cmp == 0) {
            return Integer.compare(task1.getId(), task2.getId());
        }
        return cmp;
    });

    protected InMemoryTaskManager() {
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<Task>(prioritizedTasks);
    }

    // Таски
    @Override
    public List<Task> getTaskList() {
        return taskMap.values().stream()
                .map(Task::new)
                .toList();
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
        if (hasTimeIntersection(task)) {
            throw new IllegalArgumentException("Задача пересекается по времени с уже существующей");
        }
        task.setId(getId());
        taskMap.put(task.getId(), task);
        updatePrioritizedTask(task);
    }

    @Override
    public void updateTask(Task task) {
        if (hasTimeIntersection(task)) {
            throw new IllegalArgumentException("Задача пересекается по времени с уже существующей");
        }
        taskMap.replace(task.getId(), task);
        updatePrioritizedTask(task);

    }

    @Override
    public void deleteTaskById(int id) {
        removePrioritizedTasks(taskMap.get(id));
        taskMap.remove(id);
        historyManager.remove(id);
    }

    protected void putTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    //Сабтаски
    @Override
    public List<Subtask> getSubtaskList() {
        return subtaskMap.values().stream()
                .map(Subtask::new)
                .toList();
    }

    @Override
    public void clearSubtaskMap() {
        subtaskMap.clear();
        prioritizedTasks.removeIf(task -> task instanceof Subtask);
        for (Epic epic : epicMap.values()) {
            epic.setChildrenSubtask(new ArrayList<>());
            updateEpicAllFields(epic);
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
        if (hasTimeIntersection(subtask)) {
            throw new IllegalArgumentException("Задача пересекается по времени с уже существующей");
        }
        subtask.setId(getId());
        subtaskMap.put(subtask.getId(), subtask);
        int idParentEpic = subtaskMap.get(subtask.getId()).getIdParentEpic();
        epicMap.get(idParentEpic).addSubtask(subtask);
        updateEpicAllFields(epicMap.get(idParentEpic));
        updatePrioritizedTask(subtask);

    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (hasTimeIntersection(subtask)) {
            throw new IllegalArgumentException("Задача пересекается по времени с уже существующей");
        }
        subtaskMap.replace(subtask.getId(), subtask);
        updateEpicAllFields(epicMap.get(subtask.getIdParentEpic()));
        updatePrioritizedTask(subtask);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtaskMap.get(id);
        if (subtask == null) {
            System.out.println("Подзадача с id=" + id + " не найдена.");
            return;
        }
        removePrioritizedTasks(subtask);
        int idParentEpic = subtask.getIdParentEpic();
        subtaskMap.remove(id);
        epicMap.get(idParentEpic).deleteSubTask(id);
        updateEpicAllFields(epicMap.get(idParentEpic));
        historyManager.remove(id);
    }

    protected void putSubtask(Subtask subtask) {
        subtaskMap.put(subtask.getId(), subtask);
        int idParentEpic = subtask.getIdParentEpic();
        epicMap.get(idParentEpic).addSubtask(subtask);
        updateEpicAllFields(epicMap.get(idParentEpic));
    }

    //Эпики
    @Override
    public List<Epic> getEpicList() {
        return epicMap.values().stream()
                .map(Epic::new)
                .toList();
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
        updateEpicAllFields(epic);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epicMap.get(id);
        if (epic != null) {
            List<Integer> childrenSubtaskId = epic.getChildrenSubtaskId();
            if (childrenSubtaskId != null) {
                for (Integer subtaskId : childrenSubtaskId) {
                    Subtask subtask = subtaskMap.get(subtaskId);
                    if (subtask != null) {
                        prioritizedTasks.remove(subtask);
                        subtaskMap.remove(subtaskId);
                        historyManager.remove(subtaskId);
                    }
                }
            }
            epicMap.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Эпик с id=" + id + " не найден.");
        }
    }

    @Override
    public List<Subtask> getListSubtask(Epic epic) {
        if (epic.getChildrenSubtaskId() == null) {
            return Collections.emptyList();
        }
        return epic.getChildrenSubtaskId().stream()
                .map(this::getSubtask)
                .filter(Objects::nonNull)
                .toList();
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

    protected void updatePrioritizedTask(Task task) {
        prioritizedTasks.remove(task);

        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    private void removePrioritizedTasks(Task task) {
        prioritizedTasks.remove(task);
    }

    private boolean hasTimeIntersection(Task newTask) {
        return prioritizedTasks.stream()
                .filter(existingTask -> existingTask.getId() != newTask.getId()) // не сравниваем с самим собой
                .anyMatch(existingTask -> isOverlapping(existingTask, newTask));
    }

    private boolean isOverlapping(Task t1, Task t2) {
        if (t1.getStartTime() == null || t2.getStartTime() == null) {
            return false; // если у какой-то задачи нет времени — считаем, что она не конфликтует
        }

        LocalDateTime start1 = t1.getStartTime();
        LocalDateTime end1 = t1.getEndTime().get();
        LocalDateTime start2 = t2.getStartTime();
        LocalDateTime end2 = t2.getEndTime().get();

        return start1.isBefore(end2) && start2.isBefore(end1);
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

    private void updateEpicDuration(Epic epic) {
        List<Integer> childrenSubtaskId = epic.getChildrenSubtaskId();
        Duration sumDuration;

        if (childrenSubtaskId == null || childrenSubtaskId.isEmpty()) {
            epic.setDuration(Duration.ZERO);
            return;
        }

        sumDuration = childrenSubtaskId.stream()
                .map(id -> getSubtask(id))
                .filter(subtask -> subtask != null)
                .map(subtask -> subtask.getDuration())
                .filter(duration -> duration != null)
                .reduce(Duration.ZERO, (d1, d2) -> d1.plus(d2));

        epic.setDuration(sumDuration);
    }

    private void updateEpicStartTime(Epic epic) {
        List<Integer> childrenSubtaskId = epic.getChildrenSubtaskId();
        LocalDateTime minTime;

        if (childrenSubtaskId == null || childrenSubtaskId.isEmpty()) {
            epic.setStartTime(null);
            return;
        }

        minTime = childrenSubtaskId.stream()
                .map(id -> getSubtask(id))
                .filter(subtask -> subtask != null)
                .map(subtask -> subtask.getStartTime())
                .filter(startTime -> startTime != null)
                .min((st1, st2) -> st1.compareTo(st2))
                .orElse(null);

        epic.setStartTime(minTime);
    }

    private void updateEpicEndTime(Epic epic) {
        List<Integer> childrenSubtaskId = epic.getChildrenSubtaskId();
        LocalDateTime maxTime;

        if (childrenSubtaskId == null || childrenSubtaskId.isEmpty()) {
            epic.setEndTime(null);
            return;
        }

        maxTime = childrenSubtaskId.stream()
                .map(id -> getSubtask(id))
                .filter(subtask -> subtask != null)
                .map(subtask -> subtask.getEndTime().orElse(null))
                .filter(endTime -> endTime != null)
                .max((mt1, mt2) -> mt1.compareTo(mt2))
                .orElse(null);

        epic.setEndTime(maxTime);
    }

    private void updateEpicAllFields(Epic epic) {
        updateEpicStatus(epic);
        updateEpicDuration(epic);
        updateEpicStartTime(epic);
        updateEpicEndTime(epic);
    }
}