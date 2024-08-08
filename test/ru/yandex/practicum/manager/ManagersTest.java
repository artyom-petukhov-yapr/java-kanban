package ru.yandex.practicum.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Тесты для {@link Managers}
 */
public class ManagersTest {
    /**
     * {@link Managers#getDefault()} возвращает объект менеджера
     */
    @Test
    void defaultManager() {
        Assertions.assertNotNull(Managers.getDefault());
    }

    /**
     * {@link Managers#getDefaultHistory()} возвращает объект менеджера
     */
    @Test
    void defaultHistoryManager() {
        Assertions.assertNotNull(Managers.getDefaultHistory());
    }
}
