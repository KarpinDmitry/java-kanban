import Tasks.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("gym","chest", taskManager.getId(), TaskStatus.NEW);
        Epic epic1 = new Epic("homework","comp grafic, algoritm", task1.getId());
        Subtask subtask11 = new Subtask("comp grafic","", taskManager.getId(), epic1.getId(), TaskStatus.DONE);
        Subtask subtask12 = new Subtask("algoritm","", taskManager.getId(), epic1.getId(), TaskStatus.DONE);


        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask11);
        taskManager.createSubtask(subtask12);


        ArrayList<Epic> arrayList = taskManager.getEpicMap();
        for (Epic epic: arrayList){
            System.out.println(epic);
        }
        ArrayList<Subtask> listSubtask = taskManager.getListSubtask(epic1);
        for (Subtask subtask: listSubtask){
            System.out.println(subtask);
        }
        System.out.println("Удаление");
        taskManager.deleteSubtaskById(subtask12.getId());
        ArrayList<Subtask> listSubtask2 = taskManager.getListSubtask(epic1);
        for (Subtask subtask: listSubtask2){
            System.out.println(subtask);
        }
        System.out.println("Изменение");
        Subtask newSubtask11 = new Subtask("comp grafic2",subtask11.getDescription(),subtask11.getId(),
                subtask11.getIdParentEpic(),TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(newSubtask11);
        ArrayList<Subtask> listSubtask3 = taskManager.getListSubtask(epic1);
        for (Subtask subtask: listSubtask3){
            System.out.println(subtask);
        }
        System.out.println(epic1);
//        System.out.println("Очистка substaskMap");
//        taskManager.clearSubtaskMap();
//        ArrayList<Subtask> listSubtask4 = taskManager.getListSubtask(epic1);
//        for (Subtask subtask: listSubtask4){
//            System.out.println(subtask);
//        }
//        System.out.println(epic1);
        System.out.println("Апдейт эпика");
        Epic epic2 = new Epic(epic1.getName(),"Проверка", epic1.getId(), taskManager.getListSubtask(epic1));
        taskManager.updateEpic(epic2);
        System.out.println(epic2);
        ArrayList<Subtask> listSubtask4 = taskManager.getListSubtask(epic2);
        for (Subtask subtask: listSubtask4){
            System.out.println(subtask);
        }
        System.out.println("getId");
        Epic epic3 = taskManager.getEpic(2);
        System.out.println(epic3);

    }
}
