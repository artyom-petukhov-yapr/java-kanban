package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Смоук-тесты для предзаполненного менеджера задач
 */
class PrefilledTaskManagerTest {
    TaskManager taskManager;
    /**
     * Эпик с 2ся подзадачами
     */
    Epic epicWithTwoSubtasks;
    /**
     * Эпик без подзадач
     */
    Epic emptyEpic;

    @BeforeEach
    void beforeEach() {
        // перед каждым тестом создаем новый менеджер задач и заполняем его данными
        taskManager = new TaskManager();

        // добавляем 2 задачи
        taskManager.addTask(createSampleTask(0));
        taskManager.addTask(createSampleTask(1));

        // добавляем пустой эпик
        emptyEpic = createSampleEpic(0);
        taskManager.addEpic(emptyEpic);

        // добавляем эпик с 2мя подзадачами
        epicWithTwoSubtasks = createSampleEpic(1);
        taskManager.addEpic(epicWithTwoSubtasks);
        // добавление подзадач для эпика
        taskManager.addSubtask(createSampleSubtask(0, epicWithTwoSubtasks.getId()));
        taskManager.addSubtask(createSampleSubtask(1, epicWithTwoSubtasks.getId()));
    }

    /**
     * Проверка, что изначально "заполненный" менеджер содержит 2 задачи
     */
    @Test
    void initialDataValidation_taskManagerContainsTwoTasks() {
        Assertions.assertEquals(taskManager.getTasks().length, 2);
    }

    /**
     * Проверка, что изначально "заполненный" менеджер содержит 2 эпика
     */
    @Test
    void initialDataValidation_taskManagerContainsTwoEpics() {
        Assertions.assertEquals(taskManager.getEpics().size(), 2);
    }

    /**
     * Проверка, что изначально "заполненный" менеджер содержит 2 подзадачи
     */
    @Test
    void initialDataValidation_taskManagerContainsTwoSubtasks() {
        Assertions.assertEquals(taskManager.getSubtasks().size(), 2);
    }

    /**
     * Удаление существующей задачи - менеджер возвращает true
     */
    @Test
    void removeExistingTask_returnsTrue() {
        Task taskForRemove = taskManager.getTasks()[0];
        Assertions.assertEquals(taskManager.removeTask(taskForRemove.getId()), true);
    }

    /**
     * Удаление несуществующей задачи - менеджер возвращает false
     */
    @Test
    void removeNonExistingTask_returnsFalse() {
        Assertions.assertEquals(taskManager.removeTask(Task.DEFAULT_ID), false);
    }

    /**
     * Обновление существующей задачи - менеджер возвращает true
     */
    @Test
    void updateExistingTask_returnsTrue() {
        Task taskForUpdate = taskManager.getTasks()[0];
        Assertions.assertEquals(taskManager.updateTask(taskForUpdate), true);
    }

    /**
     * Обновление существующей задачи со сменой статуса - в менеджере сохранена задача с обновленным статусом
     */
    @Test
    void updateExistingTaskStatus_taskManagerContainsTaskWithCorrectState() throws Exception {
        // создаем клон задачи
        Task taskForUpdate = new Task(taskManager.getTasks()[0]);
        // меняем статус
        taskForUpdate.setState(TaskState.DONE);
        // обновляем задачу через менеджер
        taskManager.updateTask(taskForUpdate);
        // проверяем статус задачи после обновления
        Assertions.assertEquals(taskManager.getTask(taskForUpdate.getId()).getState(), TaskState.DONE);
    }

    /**
     * Проверка, что у пустого эпика статус = {@link TaskState#NEW}
     */
    @Test
    void validateInitialEmptyEpicState_equalsNEW() {
        Epic epic = taskManager.getEpic(emptyEpic.getId());
        Assertions.assertEquals(epic.getState(), TaskState.NEW);
    }

    /**
     * Проверка, что у заполненного эпика статус = {@link TaskState#NEW}
     */
    @Test
    void validateInitialNonEmptyEpicState_equalsNEW() {
        Epic epic = taskManager.getEpic(epicWithTwoSubtasks.getId());
        Assertions.assertEquals(epic.getState(), TaskState.NEW);
    }

    /**
     * Перевод подзадачи эпика в {@link TaskState#IN_PROGRESS} приводит к возврату статуса {@link TaskState#IN_PROGRESS}
     * для эпика
     */
    @Test
    void updateSubtaskStateToInProgress_epicStateEqualsInProgress() {
        // получение первой подзадачи эпика
        Subtask epicSubtask = taskManager.getEpicSubtasks(epicWithTwoSubtasks.getId()).get(0);
        // клонирование подзадачи
        epicSubtask = new Subtask(epicSubtask);
        // смена статуса для подзадачи
        epicSubtask.setState(TaskState.IN_PROGRESS);
        taskManager.updateSubtask(epicSubtask);
        // проверка, что для эпика статус сменился на IN_PROGRESS
        Assertions.assertEquals(taskManager.getEpic(epicWithTwoSubtasks.getId()).getState(), TaskState.IN_PROGRESS);
    }

    /**
     * Перевод всех подзадач эпика в {@link TaskState#DONE} приводит к возврату статуса {@link TaskState#DONE} для эпика
     */
    @Test
    void updateAllSubtasksStateToDone_epicStateEqualsDone() {
        // получение первой подзадачи эпика
        List<Subtask> subtasks = taskManager.getEpicSubtasks(epicWithTwoSubtasks.getId());
        for (Subtask subtask : subtasks) {

            // клонирование подзадачи
            subtask = new Subtask(subtask);
            // смена статуса для подзадачи
            subtask.setState(TaskState.DONE);
            taskManager.updateSubtask(subtask);
        }

        // проверка, что для эпика статус сменился на DONE
        Assertions.assertEquals(taskManager.getEpic(epicWithTwoSubtasks.getId()).getState(), TaskState.DONE);
    }

    /**
     * Обновление эпика с установленным извне статусом DONE не приводит к смене статуса у эпика в менеджере
     */
    @Test
    void updateEpicStateToDone_epicStateEqualsToNew() {
        Epic epic = new Epic(epicWithTwoSubtasks);
        // установка статуса в DONE
        epic.setState(TaskState.DONE);
        // пытаемся обновить эпик
        taskManager.updateEpic(epic);
        // проверяем, что у эпика в менеджере статус остался прежним
        Assertions.assertEquals(taskManager.getEpic(epicWithTwoSubtasks.getId()).getState(), TaskState.NEW);
    }

    private Task createSampleTask(int index) {
        return new Task("Task name " + index, "Task description " + index);
    }

    private Epic createSampleEpic(int index) {
        return new Epic("Epic name " + index, "Epic description " + index);
    }

    private Subtask createSampleSubtask(int index, int epicId) {
        return new Subtask("Subtask name " + index, "Subtask description " + index, epicId);
    }
}