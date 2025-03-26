package Tasks;


public class Task {
    private String name;
    private String description;
    private final int id;
    private TaskStatus status;

    public Task(String name, String description, int id){
        this.name = name;
        this.description = description;
        this.id = id;
        status = TaskStatus.NEW;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
