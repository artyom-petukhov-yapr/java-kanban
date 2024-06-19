package ru.yandex.practicum.manager.model;

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
