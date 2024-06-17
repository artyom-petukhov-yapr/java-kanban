package ru.yandex.practicum;

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
