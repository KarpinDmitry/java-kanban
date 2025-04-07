import Tasks.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("gym","chest", TaskStatus.NEW);
        inMemoryTaskManager.createTask(task1);
        Epic epic1 = new Epic("homework","comp grafic, algoritm");
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask11 = new Subtask("comp grafic","", epic1.getId(), TaskStatus.DONE);
        Subtask subtask12 = new Subtask("algoritm","", epic1.getId(), TaskStatus.DONE);


        inMemoryTaskManager.createSubtask(subtask11);
        inMemoryTaskManager.createSubtask(subtask12);


        ArrayList<Epic> arrayList = inMemoryTaskManager.getEpicList();
        for (Epic epic: arrayList){
            System.out.println(epic);
        }
        ArrayList<Subtask> listSubtask = inMemoryTaskManager.getListSubtask(epic1);
        for (Subtask subtask: listSubtask){
            System.out.println(subtask);
        }
//        System.out.println("Удаление");
//        taskManager.deleteSubtaskById(subtask12.getId());
//        ArrayList<Subtask> listSubtask2 = taskManager.getListSubtask(epic1);
//        for (Subtask subtask: listSubtask2){
//            System.out.println(subtask);
//        }
        System.out.println("Изменение");
        Subtask newSubtask11 = new Subtask("comp grafic2",subtask11.getDescription(),subtask11.getIdParentEpic(), TaskStatus.IN_PROGRESS, subtask11.getId());
        inMemoryTaskManager.updateSubtask(newSubtask11);
        ArrayList<Subtask> listSubtask3 = inMemoryTaskManager.getListSubtask(epic1);
        for (Subtask subtask: listSubtask3){
            System.out.println(subtask);
        }
        System.out.println(epic1);
        System.out.println("Очистка substaskMap");
        inMemoryTaskManager.clearSubtaskMap();
        ArrayList<Subtask> listSubtask4 = inMemoryTaskManager.getListSubtask(epic1);
        for (Subtask subtask: listSubtask4){
            System.out.println(subtask);
        }
        System.out.println(epic1);
        System.out.println("Апдейт эпика");
        Epic epic2 = new Epic(epic1.getName(),"Проверка", inMemoryTaskManager.getListSubtask(epic1), epic1.getId());
        inMemoryTaskManager.updateEpic(epic2);
        System.out.println(epic2);
        ArrayList<Subtask> listSubtask5 = inMemoryTaskManager.getListSubtask(epic2);
        for (Subtask subtask: listSubtask5){
            System.out.println(subtask);
        }
        System.out.println("getId");
        Epic epic3 = inMemoryTaskManager.getEpic(3);
        System.out.println(epic3);

    }
}
