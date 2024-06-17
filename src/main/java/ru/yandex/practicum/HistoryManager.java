package ru.yandex.practicum;

import java.util.List;

/**
 * Интерфейс менеджера, отвечающего за хранение истории просмотров задач
 */
public interface HistoryManager {
    /**
     * Добавить задачу в историю просмотра
     */
    void add(Task task);

    /**
     * Получить список просмотренных задач
     */
    List<Task> getHistory();
}
