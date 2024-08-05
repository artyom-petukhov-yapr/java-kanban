package ru.yandex.practicum.manager.history;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.TestTaskFactory;
import ru.yandex.practicum.model.Task;

import java.util.List;

/**
 * Отдельные тесты для {@link InMemoryHistoryManager} для проверки его работы с лимитом хранения просмотров.
 * Прочие реализации менеджеров могут иметь другие лимиты или не иметь их вовсе
 */
public class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    /**
     * После создания менеджера список просмотров пустой
     */
    @Test
    void historyIsEmptyAfterCreation() {
        Assertions.assertEquals(0, historyManager.getHistory().size());
    }

    /**
     * Добавление 2х задач c одинаковым идентификатором ->
     * список просмотров должен содержать только одну задачу
     */
    @Test
    void historyContainsOnlyOneTaskAfterAddingTwoTasksWithEqualIds() {
        // добавление 2х задач с ИД = 0
        historyManager.add(TestTaskFactory.createSampleTask(0));
        historyManager.add(TestTaskFactory.createSampleTask(0));

        // убеждаемся, что список просмотров содержит только одну задачу
        Assertions.assertEquals(1, historyManager.getHistory().size());
    }

    /**
     * Добавление 3х задач:
     * - двух с одинаковым идентификатором
     * - одной с уникальным идентификатором
     * список просмотров должен содержать 2 задачи
     */
    @Test
    void historyContainsTwoTasksAfterAddingThreeTasks() {
        // добавление 2х задач с ИД = 0
        historyManager.add(TestTaskFactory.createSampleTask(0));
        historyManager.add(TestTaskFactory.createSampleTask(0));

        // добавление одной задачи с ИД = 1
        historyManager.add(TestTaskFactory.createSampleTask(1));

        // убеждаемся, что список просмотров содержит 2 задачи
        Assertions.assertEquals(2, historyManager.getHistory().size());
    }

    /**
     * Повторное добавление в историю задачи, должно приводить к удалению из истории информации о её первом просмотре
     */
    @Test
    void firstTaskViewInfoIsRemovedAfterAddingOfSameTaskTwice() {
        // добавление задачи с ИД = 0
        historyManager.add(TestTaskFactory.createSampleTask(0));
        // добавление задачи с ИД = 1
        historyManager.add(TestTaskFactory.createSampleTask(1));
        // повторное добавление задачи с ИД = 0
        historyManager.add(TestTaskFactory.createSampleTask(0));

        // убеждаемся, что ИД первой задачи в списке теперь равен 1, т.к. данные о первом просмотре задачи
        // с ИД = 0 должны быть удалены
        Assertions.assertEquals(1, historyManager.getHistory().get(0).getId());
    }

    /**
     * Удаление первой задачи из истории просмотров -> список просмотров начинается со второй задачи
     */
    @Test
    void historyBeginsFromNextTaskAfterRemovingOfFirstTask() {
        // добавление задачи с ИД = 0
        historyManager.add(TestTaskFactory.createSampleTask(0));
        // добавление задачи с ИД = 1
        historyManager.add(TestTaskFactory.createSampleTask(1));

        // удаление первой задачи (ИД = 0)
        historyManager.remove(0);

        // убеждаемся, что ИД первой задачи в списке теперь равен 1
        int firstTaskId = historyManager.getHistory().get(0).getId();
        Assertions.assertEquals(1, firstTaskId);
    }

    /**
     * Удаление последней задачи из истории просмотров -> список просмотров заканчивается предыдущей задачей
     */
    @Test
    void historyEndsWithPreviousTaskAfterRemovingOfLastTask() {
        // добавление задачи с ИД = 0
        historyManager.add(TestTaskFactory.createSampleTask(0));
        // добавление задачи с ИД = 1
        historyManager.add(TestTaskFactory.createSampleTask(1));

        // удаление второй задачи (ИД = 1)
        historyManager.remove(1);

        // убеждаемся, что ИД последней задачи в списке теперь равен 0
        List<Task> history = historyManager.getHistory();
        int lastTaskId = history.get(history.size() - 1).getId();
        Assertions.assertEquals(0, lastTaskId);
    }

    /**
     * Удаление задачи из середины списка
     */
    @Test
    void taskIsRemovedAfterRemovingOfTaskFromMiddleOfTheList() {
        // добавление 3х задач с идентификаторами 0, 1, 2
        historyManager.add(TestTaskFactory.createSampleTask(0));
        historyManager.add(TestTaskFactory.createSampleTask(1));
        historyManager.add(TestTaskFactory.createSampleTask(2));
        // удаление задачи с идентификатором 1 (середина списка)
        historyManager.remove(1);

        // в списке просмотров должно остаться 2 задачи
        Assertions.assertEquals(2, historyManager.getHistory().size());
    }
}
