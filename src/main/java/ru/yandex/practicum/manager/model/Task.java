package ru.yandex.practicum.manager.model;

/**
 * Отдельно стоящая задача, не входящая в эпик.
 * Базовый класс для {@link Epic} и {@link Subtask}.
 */
public class Task {
    public final static int DEFAULT_ID = -1;

    /**
     * Идентификатор
     */
    private int id = DEFAULT_ID;

    /**
     * Название
     */
    private String name;

    /**
     * Описание
     */
    private String description;

    /**
     * Статус задачи
     */
    private TaskState state = TaskState.NEW;

    /**
     * Получить идентификатор
     */
    public final int getId() {
        return id;
    }

    /**
     * Установить идентификатор
     */
    public final void setId(int id) {
        this.id = id;
    }

    /**
     * Получить название
     */
    public final String getName() {
        return name;
    }

    /**
     * Получить описание
     */
    public final String getDescription() {
        return description;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(Task other) {
        id = other.id;
        name = other.name;
        description = other.description;
        state = other.state;
    }

    /**
     * @implNote "две задачи с одинаковым id должны выглядеть для менеджера как одна и та же",
     * поэтому при сравнении учитывается только {@link #id}
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    /**
     * @implNote "две задачи с одинаковым id должны выглядеть для менеджера как одна и та же",
     * поэтому в качестве результат возвращается {@link #id}
     */
    @Override
    public final int hashCode() {
        return id;
    }

    /**
     * Получить статус задачи.
     */
    public TaskState getState() {
        return state;
    }

    /**
     * Установить статус задачи
     */
    public void setState(TaskState state) {
        this.state = state;
    }
}
