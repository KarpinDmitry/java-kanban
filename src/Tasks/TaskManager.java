package Tasks;

import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private int id = 1;
    private Map<Integer,Task> taskMap = new HashMap<>();
    private Map<Integer,Subtask> subtaskMap = new HashMap<>();
    private Map<Integer,Epic> epicMap = new HashMap<>();

    private void updatingId(){
        id++;
    }

    public Map<Integer, Task> getTaskMap() {
        return taskMap;
    }
    public void clearTaskMap(){
        taskMap.clear();
    }
    public Task getTask(int id){
        return taskMap.get(id);

    }
    public void createTask(Task task){
        updatingId();
        taskMap.put(id,task);
    }


    public Map<Integer, Subtask> getSubtaskMap() {
        return subtaskMap;
    }
    public void clearSubtaskMap(){

    }
    public Subtask getSubtask(int id){
        return subtaskMap.get(id);

    }
    public void createSubtask(Subtask subtask){
        updatingId();
        subtaskMap.put(id,subtask);
    }



    public Map<Integer, Epic> getEpicMap() {
        return epicMap;
    }
    public void clearEpicMap(){
        epicMap.clear();
    }
    public Epic getEpic(int id){
        return epicMap.get(id);
    }
    public void createEpic(Epic epic){
        updatingId();
        epicMap.put(id,epic);
    }

}
