package Tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TaskManager {
    private int id = 1;
    private Map<Integer,Task> taskMap = new HashMap<>();
    private Map<Integer,Subtask> subtaskMap = new HashMap<>();
    private Map<Integer,Epic> epicMap = new HashMap<>();


    private int getId() {
        id++;
        return id;
    }

    // Таски
    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(taskMap.values());
    }
    public void clearTaskMap(){
        taskMap.clear();
    }
    public Task getTask(int id){
        return taskMap.get(id);

    }
    public void createTask(Task task){
        task.setId(getId());
        taskMap.put(task.getId(),task);
    }
    public void updateTask(Task task){
        taskMap.replace(task.getId(),task);
    }
    public void deleteTaskById(int id){
        taskMap.remove(id);
    }

    //Сабтаски
    public ArrayList<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskMap.values());
    }
    public void clearSubtaskMap(){
        subtaskMap.clear();
        for (Epic epic: epicMap.values()){
            epic.setChildrenSubtask(new ArrayList<>());
            updateEpicStatus(epic);
        }
    }
    public Subtask getSubtask(int id){
        return subtaskMap.get(id);

    }
    public void createSubtask(Subtask subtask){
        subtask.setId(getId());
        subtaskMap.put(subtask.getId(), subtask);
        int idParentEpic = subtaskMap.get(subtask.getId()).getIdParentEpic();
        epicMap.get(idParentEpic).addSubtask(subtask);
        updateEpicStatus(epicMap.get(idParentEpic));

    }
    public void updateSubtask(Subtask subtask){
        subtaskMap.replace(subtask.getId(),subtask);
        updateEpicStatus(epicMap.get(subtask.getIdParentEpic()));
    }
    public void deleteSubtaskById(int id){
        int idParentEpic = subtaskMap.get(id).getIdParentEpic();
        subtaskMap.remove(id);
        epicMap.get(idParentEpic).deleteSubTask(id);
        updateEpicStatus(epicMap.get(idParentEpic));
    }


    //Эпики
    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epicMap.values());
    }
    public void clearEpicMap(){
        epicMap.clear();
    }
    public Epic getEpic(int id){
        return epicMap.get(id);
    }
    public void createEpic(Epic epic){
        epic.setId(getId());
        epicMap.put(epic.getId(),epic);
    }
    public void updateEpic(Epic epic){
        epicMap.replace(epic.getId(), epic);
        updateEpicStatus(epic);
    }
    public void deleteEpicById(int id){
        epicMap.remove(id);
    }
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
    private void updateEpicStatus(Epic epic){
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
