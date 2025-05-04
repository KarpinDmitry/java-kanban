package tasks;

import service.TaskStatus;

import java.util.Objects;

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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subtask)) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return idParentEpic == subtask.idParentEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idParentEpic);
    }

}
