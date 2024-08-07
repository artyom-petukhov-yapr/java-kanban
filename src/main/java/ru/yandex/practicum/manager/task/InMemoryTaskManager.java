package ru.yandex.practicum.manager.task;

import ru.yandex.practicum.manager.Managers;
import ru.yandex.practicum.manager.history.HistoryManager;
import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.Subtask;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.model.TaskState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Менеджер задач.
 * Предоставляет API для работы с задачами 3х типов:
 * {@link Task} - отдельно стоящие задачи, не входящие в эпики
 * {@link Epic} - эпики (задачи, включающие N шт. {@link Subtask} (N >= 0).)
 * {@link Subtask} - подзадачи, являющиеся частью эпиков
 */
public class InMemoryTaskManager implements TaskManager {
    /**
     * Значение для следующего идентификатора.
     */
    private int nextId = 0;

    /**
     * Задачи
     */
    protected final HashMap<Integer, Task> tasks = new HashMap<>();

    /**
     * Эпики
     */
    protected final HashMap<Integer, Epic> epics = new HashMap<>();

    /**
     * Подзадачи эпиков
     *
     * @implNote Добавление и удаление подзадач делегировано {@link Epic} для синхронности данной HashMap и
     * списков подзадач внутри эпиков
     */
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    /**
     * Менеджер, отвечающий за хранение истории просмотров задач
     */
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    /**
     * Получить следующее значение идентификатора.
     * Каждый следующий вызов возвращает значение на 1 больше, чем результат предыдущего вызова.
     * @return следующее значение идентификатора (начиная с 0).
     */
    public int getNextId() {
        return nextId++;
    }

    /**
     * Получить все задачи типа {@link Task}
     */
    @Override
    public List<Task> getTasks() {
        return tasks.values().stream().toList();
    }

    /**
     * Удалить все задачи типа {@link Task}
     */
    @Override
    public void clearTasks() {
        tasks.clear();
    }

    /**
     * Получить {@link Task} по её идентификатору.
     *
     * @param id Идентификатор задачи
     * @return {@link Task} если задача найдена, иначе null
     */
    @Override
    public Task getTask(int id) {
        Task result = tasks.get(id);
        if (result != null) {
            historyManager.add(result);
        }
        return result;
    }

    /**
     * Добавить новую задачу.
     * Для задачи будет присвоен уникальный идентификатор
     *
     * @param task Задача
     * @return Идентификатор задачи
     */
    @Override
    public int addTask(Task task) {
        task.setId(getNextId());
        tasks.put(task.getId(), task);
        return task.getId();
    }

    /**
     * Обновить задачу.
     * Если задача с данным идентификатором найдена, то она будет обновлена. Иначе нет.
     *
     * @param task Задача
     * @return true - задача обновлена, иначе - false
     */
    @Override
    public boolean updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            return tasks.put(task.getId(), task) != null;
        }
        // задачи с данным идентификатором нет в списке, поэтому "обновление" не производим
        return false;
    }

    /**
     * Удалить задачу
     *
     * @param id Идентификатор удаляемой задачи
     * @return true - задача удалена, иначе - false
     */
    @Override
    public boolean removeTask(int id) {
        return tasks.remove(id) != null;
    }

    /**
     * Получить все задачи типа {@link Epic}
     */
    @Override
    public List<Epic> getEpics() {
        return epics.values().stream().toList();
    }

    /**
     * Удалить все задачи типа {@link Epic}
     */
    @Override
    public void clearEpics() {
        // удаление всех подзадач, т.к. они могут существовать только пока существуют эпики, на которые они ссылаются
        clearSubtasks();
        epics.clear();
    }

    /**
     * Получить {@link Epic} по его идентификатору.
     *
     * @param id Идентификатор эпика
     * @return {@link Epic} если задача найдена, иначе null
     */
    @Override
    public Epic getEpic(int id) {
        Epic result = getEpicSilently(id);
        if (result != null) {
            historyManager.add(result);
        }
        return result;
    }

    /**
     * Получить {@link Epic} по его идентификатору без занесения в историю просмотров.
     */
    private Epic getEpicSilently(int id) {
        return epics.get(id);
    }

    /**
     * Добавить новый эпик.
     * Эпику будет присвоен уникальный идентификатор
     * Список подзадач эпика будет принудительно очищен.
     * Для работы с подзадачами используйте {@link #addSubtask(Subtask)}, {@link #removeSubtask(int)}
     *
     * @param epic Эпик
     * @return Идентификатор эпика
     */
    @Override
    public int addEpic(Epic epic) {
        // генерация id для эпика
        epic.setId(getNextId());
        // очистка подзадач для эпика
        epic.clearSubtasks();
        // добавление эпика
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    /**
     * Обновить эпик.
     * Если эпик с данным идентификатором найден, то он будет обновлен. Иначе нет.
     * Список подзадач для эпика НЕ будет модифицирован.
     * Для работы с подзадачами используйте {@link #addSubtask(Subtask)}, {@link #removeSubtask(int)}
     * Статус эпика НЕ будет заменен на переданный
     *
     * @param epic Эпик
     * @return true - эпик обновлен, иначе - false
     */
    @Override
    public boolean updateEpic(Epic epic) {
        Epic epicFromManager = getEpicSilently(epic.getId());
        if (epicFromManager != null) {
            // подзадачи в переданном эпике заменяются на подзадачи из эпика, хранимого в менеджере
            epic.replaceSubtasks(epicFromManager.getSubtasks());
            // статус эпика не меняется
            epic.setState(epicFromManager.getState());
            // обновление эпика
            return epics.put(epic.getId(), epic) != null;
        }
        // эпика с данным идентификатором нет в списке, поэтому "обновление" не производим
        return false;
    }

    /**
     * Получить подзадачи эпика
     *
     * @param id Идентификатор эпика
     * @return Подзадачи эпика, если он был найден по id. Иначе постой список.
     */
    @Override
    public List<Subtask> getEpicSubtasks(int id) {
        Epic epic = getEpicSilently(id);
        if (epic == null) {
            return new ArrayList<>();
        }
        return epic.getSubtasks().stream().map(stId -> getSubtaskSilently(stId)).toList();
    }

    /**
     * Удалить эпик
     *
     * @param id Идентификатор удаляемого эпика
     * @return true - эпик удален, иначе - false
     */
    @Override
    public boolean removeEpic(int id) {
        Epic epic = getEpicSilently(id);
        if (epic == null) {
            // эпик с указанным идентификатором не найден
            return false;
        }
        // удаление подзадач эпика
        epic.getSubtasks().forEach(stId -> subtasks.remove(stId));
        // удаление самого эпика
        return epics.remove(id) != null;
    }

    /**
     * Обновить статус эпика на основе статусов его подзадач
     */
    private void refreshEpicState(Epic epic) {
        // получение уникальных (distinct) статусов всех подзадач эпика
        List<TaskState> distinctStates =
                epic.getSubtasks().stream().map(stId -> getSubtaskSilently(stId).getState()).distinct().toList();
        switch (distinctStates.size()) {
            case 0 ->
                // нет подзадач - статус NEW
                    epic.setState(TaskState.NEW);
            case 1 ->
                // все подзадачи имеют одинаковый статус - установка данного статуса для эпика
                    epic.setState(distinctStates.get(0));
            default ->
                // подзадачи находятся в различных статусах - установка IN_PROGRESS для эпика
                    epic.setState(TaskState.IN_PROGRESS);
        }
    }

    /**
     * Получить все задачи типа {@link Subtask}
     */
    @Override
    public List<Subtask> getSubtasks() {
        return subtasks.values().stream().toList();
    }

    /**
     * Удалить все задачи типа {@link Subtask}
     */
    @Override
    public void clearSubtasks() {
        subtasks.clear();
        // очистка подзадач во всех эпиках и установка статуса NEW для эпиков
        epics.values().forEach(epic -> {
            epic.clearSubtasks();
            epic.setState(TaskState.NEW);
        });
    }

    /**
     * Получить {@link Subtask} по её идентификатору.
     *
     * @param id Идентификатор подзадачи
     * @return {@link Subtask} если подзадача найдена, иначе null
     */
    @Override
    public Subtask getSubtask(int id) {
        Subtask result = getSubtaskSilently(id);
        if (result != null) {
            historyManager.add(result);
        }
        return result;
    }

    /**
     * Получить {@link Subtask} по её идентификатору без занесения в историю просмотров.
     */
    private Subtask getSubtaskSilently(int id) {
        return subtasks.get(id);
    }

    /**
     * Добавить новую подзадачу для эпика (идентификатор эпика получается из данных подзадачи)
     * Подзадаче будет присвоен уникальный идентификатор
     *
     * @param subtask Подзадача
     * @return Идентификатор подзадачи, если соответсвующий ей эпик был найден, и она была добавлена. Иначе null
     */
    @Override
    public Integer addSubtask(Subtask subtask) {
        Epic epic = getEpicSilently(subtask.getEpicId());
        if (epic == null) {
            // эпик не найден
            return null;
        }
        subtask.setId(getNextId());
        // добавление подзадачи
        subtasks.put(subtask.getId(), subtask);
        // добавление идентификатора подзадачи в данные эпика
        epic.addSubtask(subtask.getId());
        // обновление статуса эпика
        refreshEpicState(epic);
        return subtask.getId();
    }

    /**
     * Обновить подзадачу.
     * Если для подзадачи найден соответсвующий ей эпик, то она будет обновлена. Иначе нет.
     *
     * @param subtask Подзадача
     * @return true - эпик обновлен, иначе - false
     */
    @Override
    public boolean updateSubtask(Subtask subtask) {
        Epic epic = getEpicSilently(subtask.getEpicId());
        if (epic == null) {
            // эпик не найден
            return false;
        }
        if (!epic.containsSubtask(subtask.getId())) {
            // в эпике нет данных об этой подзадаче
            return false;
        }
        // подзадача найдена, найден соответствующей ей эпик - выполняем обновление
        subtasks.put(subtask.getId(), subtask);
        // обновление статуса эпика
        refreshEpicState(epic);
        return true;
    }

    /**
     * Удалить подзадачу
     *
     * @param id Идентификатор удаляемой подзадачи
     * @return true - подзадача удалена, иначе - false
     */
    @Override
    public boolean removeSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            return false;
        }
        Epic epic = getEpicSilently(subtask.getEpicId());
        if (epic == null) {
            return false;
        }
        // удаление подзадачи
        subtasks.remove(id);
        // удаление подзадачи из эпика
        epic.removeSubtask(subtask.getId());
        // обновление статуса эпика
        refreshEpicState(epic);
        return true;
    }

    /**
     * Получить историю просмотра задач
     */
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
