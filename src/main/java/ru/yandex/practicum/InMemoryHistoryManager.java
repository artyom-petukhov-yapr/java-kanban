package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;

/**
 * Менеджер, отвечающий за хранение истории просмотров задач в оперативной памяти
 */
public class InMemoryHistoryManager implements HistoryManager {
    /**
     * Максимальное количество хранящихся в менеджере историй просмотров
     */
    public static final int HISTORY_LIMIT = 10;

    /**
     * История просмотров задач
     */
    private final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() == HISTORY_LIMIT) {
            // достигнут лимит хранения - удаление самого "старого" просмотра
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
