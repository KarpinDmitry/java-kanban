package Service;

import Service.HistoryManager;
import Service.InMemoryHistoryManager;
import Service.TaskManager;

//Утилитарный класс Managers
public class Managers {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
