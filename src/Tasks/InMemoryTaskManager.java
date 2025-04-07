package Tasks;

import Service.HistoryManager;
import Service.TaskManager;
import Tasks.*;

import java.util.*;


class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private Map<Integer, Task> taskMap = new HashMap<>();
    private Map<Integer, Subtask> subtaskMap = new HashMap<>();
    private Map<Integer, Epic> epicMap = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();
    @Override
    public int getId() {
        id++;
        return id;
    }
    @Override
    public Deque<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Таски
    @Override
    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(taskMap.values());
    }
    @Override
    public void clearTaskMap(){
        taskMap.clear();
    }
    @Override
    public Task getTask(int id){
        historyManager.add(taskMap.get(id));
        return taskMap.get(id);
    }
    @Override
    public void createTask(Task task){
        task.setId(getId());
        taskMap.put(task.getId(),task);
    }
    @Override
    public void updateTask(Task task){
        taskMap.replace(task.getId(),task);
    }
    @Override
    public void deleteTaskById(int id){
        taskMap.remove(id);
    }

    //Сабтаски
    @Override
    public ArrayList<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskMap.values());
    }
    @Override
    public void clearSubtaskMap(){
        subtaskMap.clear();
        for (Epic epic: epicMap.values()){
            epic.setChildrenSubtask(new ArrayList<>());
            updateEpicStatus(epic);
        }
    }
    @Override
    public Subtask getSubtask(int id){
        historyManager.add(subtaskMap.get(id));
        return subtaskMap.get(id);
    }
    @Override
    public void createSubtask(Subtask subtask){
        subtask.setId(getId());
        subtaskMap.put(subtask.getId(), subtask);
        int idParentEpic = subtaskMap.get(subtask.getId()).getIdParentEpic();
        epicMap.get(idParentEpic).addSubtask(subtask);
        updateEpicStatus(epicMap.get(idParentEpic));

    }
    @Override
    public void updateSubtask(Subtask subtask){
        subtaskMap.replace(subtask.getId(),subtask);
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
    }


    //Эпики
    @Override
    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epicMap.values());
    }
    @Override
    public void clearEpicMap(){
        epicMap.clear();
    }
    @Override
    public Epic getEpic(int id){
        historyManager.add(epicMap.get(id));
        return epicMap.get(id);
    }
    @Override
    public void createEpic(Epic epic){
        epic.setId(getId());
        epicMap.put(epic.getId(),epic);
    }
    @Override
    public void updateEpic(Epic epic){
        epicMap.replace(epic.getId(), epic);
        updateEpicStatus(epic);
    }
    @Override
    public void deleteEpicById(int id){
        epicMap.remove(id);
    }
    @Override
    public ArrayList<Subtask> getListSubtask(Epic epic){
        ArrayList<Integer> childrenSubtaskId = epic.getChildrenSubtaskId();
        ArrayList<Subtask> resultList = new ArrayList<>();
        if (epic.getChildrenSubtaskId() == null){
            return resultList;
        }
        for (int id: childrenSubtaskId){
            resultList.add(getSubtask(id));
        }
        return resultList;
    }
    @Override
    public void updateEpicStatus(Epic epic){
        ArrayList<Integer> childrenSubtaskId = epic.getChildrenSubtaskId();
        boolean flagNew = true;
        boolean flagDone = true;

        if (childrenSubtaskId == null){
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        for (int id: childrenSubtaskId){
            Subtask subtask = subtaskMap.get(id);
            if (subtask.getStatus() != TaskStatus.NEW){
                flagNew = false;
            }
            if (subtask.getStatus() != TaskStatus.DONE){
                flagDone = false;
            }
        }
        if (flagNew){
            epic.setStatus(TaskStatus.NEW);
        } else if (flagDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

}