package Tasks;

import Service.HistoryManager;
import Service.TaskManager;
import Tasks.InMemoryTaskManager;

public class Managers {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
