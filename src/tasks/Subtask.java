package tasks;

import service.TaskStatus;

public class Subtask extends Task {
    private final int idParentEpic;

    public Subtask(String name, String description, int idParentEpic, TaskStatus status) {
        super(name, description, status);
        this.idParentEpic = idParentEpic;
    }

    public Subtask(String name, String description, int idParentEpic, TaskStatus status, int id) {
        super(name, description, status, id);
        this.idParentEpic = idParentEpic;
    }

    public Subtask(Subtask other) {
        super(other);
        this.idParentEpic = other.idParentEpic;
    }

    public int getIdParentEpic() {
        return idParentEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", idParentEpic=" + idParentEpic +
                '}';
    }
}
