package ru.yandex.practicum.manager;

import ru.yandex.practicum.manager.task.InMemoryTaskManager;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.manager.history.HistoryManager;
import ru.yandex.practicum.manager.history.InMemoryHistoryManager;

/**
 * Утилитарный класс для создания менеджеров
 */
public class Managers {
    /**
     * Получить менеджер задач по-умолчанию
     */
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    /**
     * Получить менеджер истории просмотра задач по-умолчанию
     */
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
