package service;

import java.nio.file.Path;

//Утилитарный класс Managers
public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getFileBackTaskManager() {
        return new FileBackedTaskManager(Path.of("test.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
