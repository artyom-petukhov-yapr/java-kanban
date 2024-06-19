package ru.yandex.practicum.manager.taskManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.TestTaskFactory;
import ru.yandex.practicum.manager.Managers;
import ru.yandex.practicum.manager.TaskManager.TaskManager;
import ru.yandex.practicum.manager.model.Task;

/**
 * Тесты для менеджера задач по умолчанию
 */
class DefaultTaskManagerTest {
    TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        // перед каждым тестом создаем новый менеджер задач
        taskManager = Managers.getDefault();
    }

    /**
     * После создания менеджера список задач пустой
     */
    @Test
    void ctor_tasksIsEmpty() {
        Assertions.assertEquals(0, taskManager.getTasks().size());
    }

    /**
     * Добавление задачи -> В списке задач одна задача
     */
    @Test
    void addTask_tasksContainsOneTask() {
        taskManager.addTask(TestTaskFactory.createSampleTask(0));

        Assertions.assertEquals(1, taskManager.getTasks().size());
    }

    /**
     * Добавление задачи -> Для добавленной задачи был сгенерирован id
     */
    @Test
    void addTask_idGenerated() {
        taskManager.addTask(TestTaskFactory.createSampleTask(0));

        Assertions.assertNotEquals(Task.DEFAULT_ID, taskManager.getTasks().get(0).getId());
    }
}