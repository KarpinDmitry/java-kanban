package Tasks;

import java.util.ArrayList;

public class Epic extends Task{

    private ArrayList<Integer> childrenSubtask;

    public Epic(String name, String description, int id, ArrayList<Subtask> childrenSubtask) {
        super(name, description, id);
        for(Subtask subtask: childrenSubtask){
            this.childrenSubtask.add(subtask.getId());
        }
    }
    public Epic(String name, String description, int id) {
        super(name, description, id);
    }

    public void addSubtask(Subtask subtask){
        childrenSubtask.add(subtask.getId());
    }

    public void deleteSubTask(Subtask subtask){
        childrenSubtask.remove(subtask.getId());
    }

    @Override
    public void setStatus(TaskStatus status) {
        throw new UnsupportedOperationException("Метод setStatus не поддерживается в классе Epic.");
    }


}
