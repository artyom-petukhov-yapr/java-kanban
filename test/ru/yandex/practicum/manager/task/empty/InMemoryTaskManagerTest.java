package ru.yandex.practicum.manager.task.empty;

import ru.yandex.practicum.manager.task.InMemoryTaskManager;

/**
 * Тесты для {@link InMemoryTaskManager} (без предварительного заполнения)
 */
public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    protected InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }
}
