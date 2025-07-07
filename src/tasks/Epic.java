package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Epic extends Task {

    private List<Integer> childrenSubtaskId = new ArrayList<>();
    private LocalDateTime endTime;

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
        super(name, description, id, null, null);
    }

    public Epic(String name, String description, List<Subtask> childrenSubtask, int id) {
        super(name, description, id, null, null);
        for (Subtask subtask : childrenSubtask) {
            this.childrenSubtaskId.add(subtask.getId());
        }
    }

    public Epic(Epic other) {
        super(other.getName(), other.getDescription(), other.getStatus(), other.getId(), other.getDuration(),
                other.getStartTime());
        endTime = other.endTime;
        this.childrenSubtaskId = new ArrayList<>(other.getChildrenSubtaskId());
    }

    public Epic(String name, String description, int id, Duration duration, LocalDateTime startTime,
                LocalDateTime endTime) {
        super(name, description, id, duration, startTime);
        this.endTime = endTime;
    }

    public List<Integer> getChildrenSubtaskId() {
        return new ArrayList<>(childrenSubtaskId);

    }

    @Override
    public Optional<LocalDateTime> getEndTime() {
        if (endTime == null) {
            return Optional.empty();
        } else {
            return Optional.of(endTime);
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
        String durationStr = getDuration() != null ? String.valueOf(getDuration().toMinutes()) : "null";
        String startTimeStr = getStartTime() != null ? getStartTime().toString() : "null";
        String endTimeStr = endTime != null ? endTime.toString() : "null";

        return String.join(",", String.valueOf(getId()), getType().toString(), getName(), getStatus().toString(),
                getDescription(), durationStr, startTimeStr, endTimeStr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic epic)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(childrenSubtaskId, epic.childrenSubtaskId) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), childrenSubtaskId, endTime);
    }

}
