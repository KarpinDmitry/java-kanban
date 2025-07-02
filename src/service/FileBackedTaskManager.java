package service;

import tasks.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path file;

    public FileBackedTaskManager(Path file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(Path file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try {
            List<String> lines = Files.readAllLines(file);
            int maxId = 0;
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.isBlank()) continue;

                Task task = fromString(line);
                switch (task.getType()) {
                    case TASK -> {
                        manager.putTask(task);
                        manager.updatePrioritizedTask(task);
                    }
                    case EPIC -> manager.putEpic((Epic) task);
                    case SUBTASK -> {
                        manager.putSubtask((Subtask) task);
                        manager.updatePrioritizedTask(task);
                    }
                }
                maxId = Math.max(maxId, task.getId());
            }
            manager.setId(maxId + 1);
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при чтении данных из файла: " + file);
        }
        return manager;
    }

    private static Task fromString(String str) {
        String[] taskElements = str.split(",");
        int id = Integer.parseInt(taskElements[0]);
        TaskType type = TaskType.valueOf(taskElements[1]);
        String name = taskElements[2];
        TaskStatus status = TaskStatus.valueOf(taskElements[3]);
        String description = taskElements[4];

        Duration duration = "null".equals(taskElements[5]) ? null : Duration.ofMinutes(Long.parseLong(taskElements[5]));
        LocalDateTime startTime = "null".equals(taskElements[6]) ? null : LocalDateTime.parse(taskElements[6]);

        switch (type) {
            case TASK -> {
                return new Task(name, description, status, id, duration, startTime);
            }
            case EPIC -> {
                LocalDateTime endTime = "null".equals(taskElements[7]) ? null : LocalDateTime.parse(taskElements[7]);
                return new Epic(name, description, id, duration, startTime, endTime);
            }
            case SUBTASK -> {
                int idParentEpic = Integer.parseInt(taskElements[7]);
                return new Subtask(name, description, idParentEpic, status, id, duration, startTime);
            }
            default -> throw new ManagerSaveException("Неизвестный тип задачи: " + type);
        }
    }

    @Override
    public void clearTaskMap() {
        super.clearTaskMap();
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();

    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();

    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();

    }

    @Override
    public void clearSubtaskMap() {
        super.clearSubtaskMap();
        save();

    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();

    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();

    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();

    }

    @Override
    public void clearEpicMap() {
        super.clearEpicMap();
        save();

    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();

    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();

    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();

    }

    private void save() {
        List<Task> taskList = getTaskList();
        List<Epic> epicList = getEpicList();
        List<Subtask> subtasksList = getSubtaskList();
        StringBuilder resultString = new StringBuilder();

        for (Task task : taskList) {
            resultString.append(task).append("\n");
        }
        for (Epic epic : epicList) {
            resultString.append(epic).append("\n");
        }
        for (Subtask subtask : subtasksList) {
            resultString.append(subtask).append("\n");
        }

        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,duration,startTime,endTime,epic\n");
            writer.write(resultString.toString());
            System.out.println(resultString);
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл: " + file);
        }
    }
}
