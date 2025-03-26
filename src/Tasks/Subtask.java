package Tasks;

public class Subtask extends Task{
    private final int parentEpic;
    public Subtask(String name, String description, int id, int parentEpic) {
        super(name, description, id);
        this.parentEpic = parentEpic;
    }

    public int getParentEpic() {
        return parentEpic;
    }
}
