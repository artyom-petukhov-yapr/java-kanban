package ru.yandex.practicum;

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
    void getDefault_returnsNotNull() {
        Assertions.assertNotNull(Managers.getDefault());
    }

    /**
     * {@link Managers#getDefaultHistory()} возвращает объект менеджера
     */
    @Test
    void getDefaultHistory_returnsNotNull() {
        Assertions.assertNotNull(Managers.getDefaultHistory());
    }
}
