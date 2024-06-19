package ru.yandex.practicum.manager;

import ru.yandex.practicum.manager.TaskManager.InMemoryTaskManager;
import ru.yandex.practicum.manager.TaskManager.TaskManager;
import ru.yandex.practicum.manager.historyManager.HistoryManager;
import ru.yandex.practicum.manager.historyManager.InMemoryHistoryManager;

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
