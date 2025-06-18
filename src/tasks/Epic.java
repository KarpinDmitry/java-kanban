package tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private List<Integer> childrenSubtaskId = new ArrayList<>();

    public Epic(String name, String description, List<Subtask> childrenSubtask) {
        super(name, description);
        for (Subtask subtask : childrenSubtask) {
            this.childrenSubtaskId.add(subtask.getId());
        }
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, int id) {
        super(name, description, id);
    }

    public Epic(String name, String description, List<Subtask> childrenSubtask, int id) {
        super(name, description, id);
        for (Subtask subtask : childrenSubtask) {
            this.childrenSubtaskId.add(subtask.getId());
        }
    }

    public Epic(Epic other) {
        super(other.getName(), other.getDescription(), other.getStatus(), other.getId());
        this.childrenSubtaskId = new ArrayList<>(other.getChildrenSubtaskId());
    }

    public List<Integer> getChildrenSubtaskId() {
        return new ArrayList<>(childrenSubtaskId);

    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    public void setChildrenSubtask(List<Integer> childrenSubtaskId) {
        this.childrenSubtaskId = new ArrayList<>(childrenSubtaskId);
    }

    public void addSubtask(Subtask subtask) {
        childrenSubtaskId.add(subtask.getId());
    }

    public void deleteSubTask(int id) {
        childrenSubtaskId.remove(Integer.valueOf(id));
    }

    @Override
    public String toString() {
        return getId() + "," + getType().toString() + "," + getName() + "," + getStatus() + "," + getDescription();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(childrenSubtaskId, epic.childrenSubtaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), childrenSubtaskId);
    }

}
