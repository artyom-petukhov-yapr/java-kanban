package ru.yandex.practicum.manager;

import ru.yandex.practicum.manager.task.FileBackedTaskManager;
import ru.yandex.practicum.manager.task.InMemoryTaskManager;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.manager.history.HistoryManager;
import ru.yandex.practicum.manager.history.InMemoryHistoryManager;

import java.util.List;

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
     * Получить все доступные менеджеры задач
     * @return
     */
    public static List<TaskManager> getAllAvailableTaskManagers() {
        return List.of(new FileBackedTaskManager(), new InMemoryTaskManager());
    }

    /**
     * Получить менеджер истории просмотра задач по-умолчанию
     */
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
