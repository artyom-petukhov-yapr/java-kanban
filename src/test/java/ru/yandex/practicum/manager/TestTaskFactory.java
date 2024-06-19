package ru.yandex.practicum.manager;

import ru.yandex.practicum.manager.model.Epic;
import ru.yandex.practicum.manager.model.Subtask;
import ru.yandex.practicum.manager.model.Task;

/**
 * Фабрика для создания задач/подзадач/эпиков.
 * Используется во многих тестах, поэтому код вынесен в отдельный класс.
 */
public class TestTaskFactory {
    public static Task createSampleTask(int id) {
        return setId(new Task("Task name " + id, "Task description " + id), id);
    }

    public static Epic createSampleEpic(int id) {
        return setId(new Epic("Epic name " + id, "Epic description " + id), id);
    }

    public static Subtask createSampleSubtask(int id, int epicId) {
        return setId(new Subtask("Subtask name " + id, "Subtask description " + id, epicId), id);
    }

    /**
     * Установка идентификатора для задачи
     * @param task Задача
     * @param id Идентификатор
     * @return Задачу с установленным идентификатором
     * @param <T> {@link Task} или его наследник
     * @implNote Написан, чтобы минимизировать дублирование кода и размер методов создания задач/подзадач/эпиков
     */
    private static <T extends Task> T setId(T task, int id) {
        task.setId(id);
        return task;
    }
}
