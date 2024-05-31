package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Смоук-тесты для менеджера задач
 */
class TaskManagerTest {
    TaskManager taskManager;
    @BeforeEach
    void beforeEach() {
        // перед каждым тестом создаем новый менеджер задач
        taskManager = new TaskManager();
    }

    /**
     * После создания менеджера список задач пустой
     */
    @Test
    void taskManager_ctor_tasksIsEmpty() {
        Assertions.assertEquals(taskManager.getTasks().length, 0);
    }

    /**
     * Добавление задачи -> В списке задач одна задача
     */
    @Test
    void taskManager_addTask_tasksContainsOneTask() {
        taskManager.addTask(createSampleTask(0));
        Assertions.assertEquals(taskManager.getTasks().length, 1);
    }

    /**
     * Добавление задачи -> Для добавленной задачи был сгенерирован id
     */
    @Test
    void taskManager_addTask_idGenerated() {
        taskManager.addTask(createSampleTask(0));
        Assertions.assertNotEquals(taskManager.getTasks()[0].getId(), Task.DEFAULT_ID);
    }

    private Task createSampleTask(int index) {
        return new Task("Task name " + index, "Task description " + index);
    }
}