package ru.yandex.practicum.manager.model;

import java.util.HashSet;

/**
 * Эпик - задача, включающая N подзадач (N >= 0).
 */
public class Epic extends Task {
    /**
     * Идентификаторы подзадач эпика.
     */
    private final HashSet<Integer> subtasksIds;

    public Epic(String name, String description) {
        super(name, description);
        subtasksIds = new HashSet<>();
    }

    public Epic(Epic other) {
        super(other);
        subtasksIds = new HashSet<>(other.subtasksIds);
    }

    public void addSubtask(int id) {
        subtasksIds.add(id);
    }

    public boolean containsSubtask(int id) {
        return subtasksIds.contains(id);
    }

    public boolean removeSubtask(int id) {
        return subtasksIds.remove(id);
    }

    public HashSet<Integer> getSubtasks() {
        return subtasksIds;
    }

    public void clearSubtasks() {
        subtasksIds.clear();
    }

    /**
     * Заменить список подзадач на указанный.
     * @param subtasksIds
     */
    public void replaceSubtasks(HashSet<Integer> subtasksIds) {
        this.subtasksIds.clear();
        this.subtasksIds.addAll(subtasksIds);
    }
}
