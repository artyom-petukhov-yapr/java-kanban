package ru.yandex.practicum.manager.history;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.TestTaskFactory;
import ru.yandex.practicum.manager.Managers;
import ru.yandex.practicum.model.Task;

class DefaultHistoryManagerTest {

    HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = Managers.getDefaultHistory();
    }

    /**
     * После создания менеджера список просмотров пустой
     */
    @Test
    void historyIsEmptyAfterCreation() {
        Assertions.assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    void historyContainsOneTaskAfterAddingOneTask() {
        historyManager.add(TestTaskFactory.createSampleTask(0));

        Assertions.assertEquals(1, historyManager.getHistory().size());
    }

    /**
     * Добавление 2х задач -> первая добавленная задача находится в начале списка, возвращаемого getHistory()
     */
    @Test
    void firstTaskHasZeroIndexInHistoryAfterAddingTwoTasks() {
        // добавление первой задачи
        Task firstTask = TestTaskFactory.createSampleTask(0);
        historyManager.add(firstTask);
        // добавление 2й задачи
        historyManager.add(TestTaskFactory.createSampleTask(1));

        // убеждаемся, что первая добавленная задача находится в начале списка, возвращаемого getHistory()
        Assertions.assertEquals(firstTask, historyManager.getHistory().get(0));
    }
}