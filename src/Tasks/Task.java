package Tasks;


public class Task {
    private String name;
    private String description;
    private int id;
    private TaskStatus status;

    public Task(String name, String description){
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;

    }
    public Task(String name, String description, TaskStatus status, int id){
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;

    }
    public Task(String name, String description, int id){
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
        this.id = id;

    }
    public Task(String name, String description, TaskStatus status){
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    protected void setDescription(String description){
        this.description = description;
    }

    public int getId() {
        return id;
    }
    protected void setId(int id){
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }
    protected void setStatus(TaskStatus status){
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
