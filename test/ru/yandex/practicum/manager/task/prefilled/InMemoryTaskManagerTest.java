package ru.yandex.practicum.manager.task.prefilled;

import ru.yandex.practicum.manager.task.InMemoryTaskManager;

/**
 * Тесты для предзаполненного {@link InMemoryTaskManager}
 */
public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    protected InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }
}
