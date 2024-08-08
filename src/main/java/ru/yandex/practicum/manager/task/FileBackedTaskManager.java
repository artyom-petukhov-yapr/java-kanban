package ru.yandex.practicum.manager.task;

import ru.yandex.practicum.exception.ManagerSaveException;
import ru.yandex.practicum.model.*;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    /**
     * Файл с состоянием менеджера
     */
    private final File stateFile;

    /**
     * Название файла по-умолчанию для хранения задач
     */
    public static final String defaultStateFilename = "tasks.csv";

    /**
     * Конструктор
     */
    public FileBackedTaskManager() {
        this(new File(defaultStateFilename));
    }

    /**
     * Конструктор
     *
     * @param stateFile - файл для хранения состояния менеджера
     */
    FileBackedTaskManager(File stateFile) {
        this.stateFile = stateFile;
    }

    /**
     * Сохранить состояние менеджера в файл
     */
    public void save() {
        try {
            try (FileWriter writer = new FileWriter(stateFile, false)) {
                // 1. запись шапки csv
                writer.write("id,type,name,status,description,epic\n");
                // 2. сохранение задач
                for (Task task : getTasks()) {
                    writer.write(toString(task) + "\n");
                }
                // 3. сохранение эпиков.
                // эпики сохраняются раньше, чем подзадачи, чтобы при загрузке подзадач из файла эпики уже были вычитаны
                for (Task task : getEpics()) {
                    writer.write(toString(task) + "\n");
                }
                // 4. сохранение подзадач
                for (Task task : getSubtasks()) {
                    writer.write(toString(task) + "\n");
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время сохранения состояния менеджера", e);
        }
    }

    /**
     * Загрузить состояние менеджера из файла
     */
    public void load() throws IOException {
        try (FileReader fileReader = new FileReader(stateFile);
             BufferedReader reader = new BufferedReader(fileReader)) {
            // 1. чтение шапки csv
            reader.readLine();
            // 2. загрузка задач
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = fromString(line);
                // Добавление задачи в соответствующий словарь, т.к.:
                // 1. считаем что состояние было сохранено/загружено корректно
                // 2. добавление задач через методы addTask/addSubtask/addEpic приводит к перегенерации идентификаторов
                //    и прочим сайд-эффектам
                if (task instanceof Epic) {
                    epics.put(task.getId(), (Epic) task);
                } else if (task instanceof Subtask) {
                    Subtask subtask = (Subtask) task;
                    subtasks.put(subtask.getId(), subtask);
                    // добавляем информацию о подзадаче в соответствующий эпик, т.к. связь двусторонняя
                    // между эпиком его подзадачами.
                    epics.get(subtask.getEpicId()).addSubtask(subtask.getId());
                } else {
                    tasks.put(task.getId(), task);
                }
            }
        }
    }

    /**
     * @implNote Согласно ТЗ спринта 7:
     * создайте статический метод static FileBackedTaskManager loadFromFile(File file),
     * который будет восстанавливать данные менеджера из файла при запуске программы.
     */
    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager result = new FileBackedTaskManager(file);
        result.load();
        return result;
    }

    /**
     * Привести {@link Task} к строковому представлению.
     */
    private String toString(Task task) {
        TaskType taskType = TaskType.valueOf(task.getClass().getSimpleName().toUpperCase());
        String result = String.format("%d,%s,%s,%s,%s", task.getId(), taskType, task.getName(), task.getState(), task.getDescription());
        if (taskType == TaskType.SUBTASK) {
            // в случае подзадачи пишем идентификатор эпика
            result += "," + ((Subtask) task).getEpicId();
        }
        return result;
    }

    /**
     * Получить {@link Task} по её строковому представлению.
     */
    private Task fromString(String value) {
        String[] parts = value.split(",");

        int id = Integer.parseInt(parts[0]);
        TaskType taskType = TaskType.valueOf(parts[1]);
        String name = parts[2];
        TaskState state = TaskState.valueOf(parts[3]);
        String description = parts[4];

        switch (taskType) {
            case EPIC:
                return new Epic(id, state, name, description);
            case SUBTASK:
                int epicId = Integer.parseInt(parts[5]);
                return new Subtask(id, state, name, description, epicId);
            default:
                return new Task(id, state, name, description);
        }
    }

    /*
        Вследствие постановки задания в ТЗ спринта 7:
        "Есть более изящное решение: можно наследовать FileBackedTaskManager от InMemoryTaskManager и
        получить от класса-родителя желаемую логику работы менеджера. Останется только дописать в некоторых местах
        вызовы метода автосохранения."
        выполнен override методов, изменяющих состояние менеджера
     */

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public int addTask(Task task) {
        int result = super.addTask(task);
        save();
        return result;
    }

    @Override
    public boolean updateTask(Task task) {
        if (super.updateTask(task)) {
            save();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeTask(int id) {
        if (super.removeTask(id)) {
            save();
            return true;
        }
        return false;
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public int addEpic(Epic epic) {
        int result = super.addEpic(epic);
        save();
        return result;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        if (super.updateEpic(epic)) {
            save();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeEpic(int id) {
        if (super.removeEpic(id)) {
            save();
            return true;
        }
        return false;
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        if (super.updateSubtask(subtask)) {
            save();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeSubtask(int id) {
        if (super.removeSubtask(id)) {
            save();
            return true;
        }
        return false;
    }

    public File getStateFile() {
        return stateFile;
    }
}
