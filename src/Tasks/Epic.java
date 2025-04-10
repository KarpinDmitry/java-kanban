package Tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{

    private List<Integer> childrenSubtaskId = new ArrayList<>();

    public Epic(String name, String description, List<Subtask> childrenSubtask) {
        super(name, description);
        for(Subtask subtask: childrenSubtask){
            this.childrenSubtaskId.add(subtask.getId());
        }
    }
    public Epic(String name, String description) {
        super(name, description);
    }
    public Epic(String name, String description, List<Subtask> childrenSubtask, int id) {
        super(name, description, id);
        for(Subtask subtask: childrenSubtask){
            this.childrenSubtaskId.add(subtask.getId());
        }
    }
    public List<Integer> getChildrenSubtaskId() {
        return childrenSubtaskId;

    }
    protected void setChildrenSubtask(List<Integer> childrenSubtaskId) {
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
