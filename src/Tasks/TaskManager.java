package Tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TaskManager {
    private int id = 1;
    private Map<Integer,Task> taskMap = new HashMap<>();
    private Map<Integer,Subtask> subtaskMap = new HashMap<>();
    private Map<Integer,Epic> epicMap = new HashMap<>();


    public int getId() {
        id++;
        return id;
    }

    public ArrayList<Task> getTaskMap() {
        return new ArrayList<>(taskMap.values());
    }
    public void clearTaskMap(){
        taskMap.clear();
    }
    public Task getTask(int id){
        return taskMap.get(id);

    }
    public void createTask(Task task){
        taskMap.put(task.getId(),task);
    }
    public void updateTask(Task task){
        taskMap.replace(task.getId(),task);
    }
    public void deleteTaskById(int id){
        taskMap.remove(id);
    }


    public ArrayList<Subtask> getSubtaskMap() {
        return new ArrayList<>(subtaskMap.values());
    }
    public void clearSubtaskMap(){
        subtaskMap.clear();
    }
    public Subtask getSubtask(int id){
        return subtaskMap.get(id);

    }
    public void createSubtask(Subtask subtask){
        subtaskMap.put(subtask.getId(), subtask);
    }
    public void updateSubtask(Subtask subtask){
        subtaskMap.replace(subtask.getId(),subtask);
    }
    public void deleteSubtaskById(int id){
        subtaskMap.remove(id);
    }



    public ArrayList<Epic> getEpicMap() {
        return new ArrayList<>(epicMap.values());
    }
    public void clearEpicMap(){
        epicMap.clear();
    }
    public Epic getEpic(int id){
        return epicMap.get(id);
    }
    public void createEpic(Epic epic){
        epicMap.put(epic.getId(),epic);
    }
    public void updateEpic(Epic epic){
        epicMap.replace(epic.getId(), epic);
    }
    public void deleteEpicById(int id){
        epicMap.remove(id);
    }

}
