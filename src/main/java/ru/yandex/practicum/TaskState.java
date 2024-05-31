package ru.yandex.practicum;

/**
 * Статус задачи.
 */
public enum TaskState {
    /**
     * Задача только создана, но к её выполнению ещё не приступили
     */
    NEW,
    /**
     * Над задачей ведётся работа
     */
    IN_PROGRESS,
    /**
     * Задача выполнена
     */
    DONE
}
