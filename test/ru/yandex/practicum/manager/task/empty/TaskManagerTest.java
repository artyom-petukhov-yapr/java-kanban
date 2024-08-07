package ru.yandex.practicum.manager.task.empty;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.TestTaskFactory;
import ru.yandex.practicum.manager.Managers;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.model.Task;

/**
 * Тесты для менеджера задач (без предварительного заполнения).
 * Чтобы не дублировать код тестов между различными реализациями {@link TaskManager}
 */
abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;

    /**
     * Создание менеджера задач
     */
    protected abstract T createManager();

    @BeforeEach
    void beforeEach() {
        // перед каждым тестом создаем новый менеджер задач
        taskManager = createManager();
    }

    /**
     * После создания менеджера список задач пустой
     */
    @Test
    void taskListCreation() {
        Assertions.assertEquals(0, taskManager.getTasks().size());
    }

    /**
     * Добавление задачи -> В списке задач одна задача
     */
    @Test
    void tasksContainsOneTask() {
        taskManager.addTask(TestTaskFactory.createSampleTask(0));

        Assertions.assertEquals(1, taskManager.getTasks().size());
    }

    /**
     * Добавление задачи -> Для добавленной задачи был сгенерирован id
     */
    @Test
    void idGenerated() {
        taskManager.addTask(TestTaskFactory.createSampleTask(0));

        Assertions.assertNotEquals(Task.DEFAULT_ID, taskManager.getTasks().get(0).getId());
    }
}