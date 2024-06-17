package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Отдельные тесты для {@link InMemoryHistoryManager} для проверки его работы с лимитом хранения просмотров.
 * Прочие реализации менеджеров могут иметь другие лимиты или не иметь их вовсе
 */
public class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;
    @BeforeEach
    void beforeEach(){
        historyManager = new InMemoryHistoryManager();
    }

    /**
     * Проверка, что лимит равен 10 согласно ТЗ
     */
    @Test
    void LimitEquals10() {
        Assertions.assertEquals(10, InMemoryHistoryManager.HISTORY_LIMIT);
    }

    /**
     * При добавлении {@link InMemoryHistoryManager#HISTORY_LIMIT} + 1 задачи в менеджер в истории сохранится только
     * {@link InMemoryHistoryManager#HISTORY_LIMIT} задач
     */
    @Test
    void add1TaskOverLimit_historyContainsOnlyLimitedCountOfTasks() {
        for (int i = 0; i < InMemoryHistoryManager.HISTORY_LIMIT + 1; i++) {
            historyManager.add(TestTaskFactory.createSampleTask(i));
        }

        Assertions.assertEquals(InMemoryHistoryManager.HISTORY_LIMIT, historyManager.getHistory().size());
    }

    /**
     * При добавлении {@link InMemoryHistoryManager#HISTORY_LIMIT} + 1 задачи в менеджер последняя добавленная задача
     * будет содержаться в истории в конце списка.
     * (убеждаемся, что задача, добавленная сверх лимита хранения, сохраняется, а не игнорируется)
     */
    @Test
    void add1TaskOverLimit_lastTaskInHistoryHasCorrectId() {
        for (int i = 0; i < InMemoryHistoryManager.HISTORY_LIMIT + 1; i++) {
            historyManager.add(TestTaskFactory.createSampleTask(i));
        }

        // ожидаемый идентификатор задачи для последнего элемента в истории
        int expectedId = InMemoryHistoryManager.HISTORY_LIMIT;
        // фактический идентификатор задачи для последнего элемента в истории
        int actualId = historyManager.getHistory().get(InMemoryHistoryManager.HISTORY_LIMIT - 1).getId();

        Assertions.assertEquals(expectedId, actualId);
    }
}
