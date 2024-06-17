package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Тесты для предзаполненного менеджера задач
 */
class PrefilledDefaultTaskManagerTest {
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
        taskManager = Managers.getDefault();

        // добавляем 2 задачи
        taskManager.addTask(TestTaskFactory.createSampleTask(0));
        taskManager.addTask(TestTaskFactory.createSampleTask(1));

        // добавляем пустой эпик
        emptyEpic = TestTaskFactory.createSampleEpic(0);
        taskManager.addEpic(emptyEpic);

        // добавляем эпик с 2мя подзадачами
        epicWithTwoSubtasks = TestTaskFactory.createSampleEpic(1);
        taskManager.addEpic(epicWithTwoSubtasks);
        // добавление подзадач для эпика
        taskManager.addSubtask(TestTaskFactory.createSampleSubtask(0, epicWithTwoSubtasks.getId()));
        taskManager.addSubtask(TestTaskFactory.createSampleSubtask(1, epicWithTwoSubtasks.getId()));
    }

    /**
     * Проверка, что изначально "заполненный" менеджер содержит 2 задачи
     */
    @Test
    void initialDataValidation_taskManagerContainsTwoTasks() {
        Assertions.assertEquals(2, taskManager.getTasks().size());
    }

    /**
     * Проверка, что изначально "заполненный" менеджер содержит 2 эпика
     */
    @Test
    void initialDataValidation_taskManagerContainsTwoEpics() {
        Assertions.assertEquals(2, taskManager.getEpics().size());
    }

    /**
     * Проверка, что изначально "заполненный" менеджер содержит 2 подзадачи
     */
    @Test
    void initialDataValidation_taskManagerContainsTwoSubtasks() {
        Assertions.assertEquals(2, taskManager.getSubtasks().size());
    }

    /**
     * Удаление существующей задачи - менеджер возвращает true
     */
    @Test
    void removeExistingTask_returnsTrue() {
        Task taskForRemove = taskManager.getTasks().get(0);
        Assertions.assertTrue(taskManager.removeTask(taskForRemove.getId()));
    }

    /**
     * Удаление несуществующей задачи - менеджер возвращает false
     */
    @Test
    void removeNonExistingTask_returnsFalse() {
        Assertions.assertFalse(taskManager.removeTask(Task.DEFAULT_ID));
    }

    /**
     * Обновление существующей задачи - менеджер возвращает true
     */
    @Test
    void updateExistingTask_returnsTrue() {
        Task taskForUpdate = taskManager.getTasks().get(0);
        Assertions.assertTrue(taskManager.updateTask(taskForUpdate));
    }

    /**
     * Обновление существующей задачи со сменой статуса - в менеджере сохранена задача с обновленным статусом
     */
    @Test
    void updateExistingTaskStatus_taskManagerContainsTaskWithCorrectState() {
        // создаем клон задачи
        Task taskForUpdate = new Task(taskManager.getTasks().get(0));
        // меняем статус
        taskForUpdate.setState(TaskState.DONE);
        // обновляем задачу через менеджер
        taskManager.updateTask(taskForUpdate);
        // проверяем статус задачи после обновления
        Assertions.assertEquals(TaskState.DONE, taskManager.getTask(taskForUpdate.getId()).getState());
    }

    /**
     * Проверка, что у пустого эпика статус = {@link TaskState#NEW}
     */
    @Test
    void validateInitialEmptyEpicState_equalsNEW() {
        Epic epic = taskManager.getEpic(emptyEpic.getId());
        Assertions.assertEquals(TaskState.NEW, epic.getState());
    }

    /**
     * Проверка, что у заполненного эпика статус = {@link TaskState#NEW}
     */
    @Test
    void validateInitialNonEmptyEpicState_equalsNEW() {
        Epic epic = taskManager.getEpic(epicWithTwoSubtasks.getId());
        Assertions.assertEquals(TaskState.NEW, epic.getState());
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
        Assertions.assertEquals(TaskState.IN_PROGRESS, taskManager.getEpic(epicWithTwoSubtasks.getId()).getState());
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
        Assertions.assertEquals(TaskState.DONE, taskManager.getEpic(epicWithTwoSubtasks.getId()).getState());
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
        Assertions.assertEquals(TaskState.NEW, taskManager.getEpic(epicWithTwoSubtasks.getId()).getState());
    }

    /**
     * Получение из менеджера задач 3х различных задач -> история просмотров должна содержать 3 задачи
     */
    @Test
    void getThreeTasks_taskManagerContainsThreeTasksInHistory() {
        // для всех типов задач вызываем по одному методу get
        taskManager.getTask(taskManager.getTasks().get(0).getId());
        taskManager.getSubtask(taskManager.getSubtasks().get(0).getId());
        taskManager.getEpic(taskManager.getEpics().get(0).getId());

        // история просмотров должна содержать 3 элемента
        Assertions.assertEquals(3, taskManager.getHistory().size());
    }
}