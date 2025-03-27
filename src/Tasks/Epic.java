package Tasks;

import java.util.ArrayList;

public class Epic extends Task{

    private ArrayList<Integer> childrenSubtaskId = new ArrayList<>();

    public Epic(String name, String description, int id, ArrayList<Subtask> childrenSubtask) {
        super(name, description, id);
        for(Subtask subtask: childrenSubtask){
            this.childrenSubtaskId.add(subtask.getId());
        }
    }
    public Epic(String name, String description, int id) {
        super(name, description, id);
    }

    public ArrayList<Integer> getChildrenSubtaskId() {
        return childrenSubtaskId;

    }
    protected void setChildrenSubtask(ArrayList<Integer> childrenSubtaskId) {
        this.childrenSubtaskId = childrenSubtaskId;
    }
    public void addSubtask(Subtask subtask){
        childrenSubtaskId.add(subtask.getId());
    }

    public void deleteSubTask(int id){
        childrenSubtaskId.remove(Integer.valueOf(id));
    }
    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", childrenSubtaskId=" + childrenSubtaskId + // Добавляем список childrenSubtaskId
                '}';
    }
}
