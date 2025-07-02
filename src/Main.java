import service.FileBackedTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Path path = Path.of("tasks.csv");

        // 1. Создаём менеджер
        FileBackedTaskManager manager = new FileBackedTaskManager(path);

        // 2. Создаём задачи
        Task task = new Task("Покупка продуктов", "Купить молоко и хлеб", TaskStatus.NEW,
                1, Duration.ofMinutes(30), LocalDateTime.of(2024, 6, 1, 12, 0));
        manager.createTask(task);

        Epic epic = new Epic("Подготовка к экзамену", "Сдать всё!");
        manager.createEpic(epic);

        Subtask subtask1 = new Subtask("Решить задачи", "Теория + практика", epic.getId(),
                TaskStatus.IN_PROGRESS, 3, Duration.ofMinutes(9), LocalDateTime.of(2022, 6,
                1, 12, 0));
        manager.createSubtask(subtask1);
        Subtask subtask = new Subtask("Решить задачи", "Теория + практика", epic.getId(),
                TaskStatus.IN_PROGRESS, 3, Duration.ofMinutes(50), LocalDateTime.of(2024, 7,
                2, 10, 0));
        manager.createSubtask(subtask);
        System.out.println(epic.getEndTime());
        // 3. Загружаем менеджер из файла
        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(path);

        // 4. Проверяем результат
        System.out.println("Оригинальные задачи:");
        System.out.println(manager.getTaskList().getFirst());
        System.out.println(manager.getEpicList().getFirst());
        System.out.println(manager.getSubtaskList().getFirst());

        System.out.println("\nЗагруженные задачи:");
        System.out.println(loaded.getTaskList().get(0));
        System.out.println(loaded.getEpicList().get(0));
        System.out.println(loaded.getSubtaskList().get(0));

        // 5. Сравнение
        System.out.println("\nСравнение:");
        System.out.println("Task равны? " + task.equals(loaded.getTaskList().get(0)));
        System.out.println("Epic равны? " + manager.getEpicList().getFirst().equals(loaded.getEpicList().get(0)));
        System.out.println("Subtask равны? " + subtask.equals(loaded.getSubtaskList().get(0)));

        System.out.println(manager.getPrioritizedTasks());
    }
}
