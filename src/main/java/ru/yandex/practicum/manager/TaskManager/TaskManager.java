package ru.yandex.practicum.manager.TaskManager;

import ru.yandex.practicum.manager.model.Epic;
import ru.yandex.practicum.manager.model.Subtask;
import ru.yandex.practicum.manager.model.Task;

import java.util.List;

public interface TaskManager {
    void clearTasks();

    List<Task> getTasks();

    Task getTask(int id);

    int addTask(Task task);

    boolean updateTask(Task task);

    boolean removeTask(int id);

    List<Epic> getEpics();

    void clearEpics();

    Epic getEpic(int id);

    int addEpic(Epic epic);

    boolean updateEpic(Epic epic);

    List<Subtask> getEpicSubtasks(int id);

    boolean removeEpic(int id);

    List<Subtask> getSubtasks();

    void clearSubtasks();

    Subtask getSubtask(int id);

    Integer addSubtask(Subtask subtask);

    boolean updateSubtask(Subtask subtask);

    boolean removeSubtask(int id);

    List<Task> getHistory();
}
