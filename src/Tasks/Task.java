package Tasks;


public class Task {
    private final String name;
    private String description;
    private final int id;
    private TaskStatus status;

    public Task(String name, String description, int id, TaskStatus status){
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
