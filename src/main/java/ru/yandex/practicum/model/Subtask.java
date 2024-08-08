package ru.yandex.practicum.model;

/**
 * Подзадача, являющаяся частью эпика.
 */
public class Subtask extends Task {
    /**
     * Идентификатор эпика, в который входит данная подзадача.
     */
    private int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(int id, TaskState state, String name, String description, int epicId) {
        super(id, state, name, description);
        this.epicId = epicId;
    }

    public Subtask(Subtask other) {
        super(other);
        epicId = other.epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
