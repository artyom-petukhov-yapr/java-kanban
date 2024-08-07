package ru.yandex.practicum.manager.task.empty;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.TestTaskFactory;
import ru.yandex.practicum.manager.task.FileBackedTaskManager;
import ru.yandex.practicum.model.Epic;

import java.io.IOException;

/**
 * Тесты для {@link FileBackedTaskManager} (без предварительного заполнения)
 */
public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    @Override
    protected FileBackedTaskManager createManager() {
        return new FileBackedTaskManager();
    }

    @BeforeEach
    @Override
    void beforeEach() {
        super.beforeEach();

        if (taskManager.getStateFile().exists()) {
            taskManager.getStateFile().delete();
        }
    }

    /**
     * Проверка, что после сохранения пустого менеджера файл существует
     */
    @Test
    void saveEmptyManager() {
        taskManager.save();
        boolean fileExists = taskManager.getStateFile().exists();
        Assertions.assertTrue(fileExists);
    }

    /**
     * Проверка загрузка состояния менеджера с одной задачей
     */
    @Test
    void loadManagerOneTask() throws IOException {
        taskManager.addTask(TestTaskFactory.createSampleTask(0));
        taskManager.save();

        FileBackedTaskManager taskManagerFromFile = FileBackedTaskManager.loadFromFile(taskManager.getStateFile());

        Assertions.assertEquals(1, taskManagerFromFile.getTasks().size());
    }

    /**
     * Проверка загрузка состояния менеджера с одним эпиком
     */
    @Test
    void loadManagerOneEpic() throws IOException {
        taskManager.addEpic(TestTaskFactory.createSampleEpic(0));
        taskManager.save();

        FileBackedTaskManager taskManagerFromFile = FileBackedTaskManager.loadFromFile(taskManager.getStateFile());

        Assertions.assertEquals(1, taskManagerFromFile.getEpics().size());
    }

    /**
     * Проверка загрузка состояния менеджера с одной подзадачей
     */
    @Test
    void loadManagerOneSubtask() throws IOException {
        Epic epic = TestTaskFactory.createSampleEpic(0);
        taskManager.addEpic(epic);
        taskManager.addSubtask(TestTaskFactory.createSampleSubtask(0, epic.getId()));
        taskManager.save();

        FileBackedTaskManager taskManagerFromFile = FileBackedTaskManager.loadFromFile(taskManager.getStateFile());
        //проверяем что есть 1 подзадача
        Assertions.assertEquals(1, taskManagerFromFile.getSubtasks().size());
    }

    /**
     * Проверка загрузка состояния менеджера с эпиком и 2мя подзадачами - убеждаемся, что в эпике присутствуют ссылки на
     * подзадачи (т.к. они "восстанавливаются" отдельно по мере загрузки подзадач, а не сериализуются вместе с эпиком)
     */
    @Test
    void loadManagerTwoSubtasks() throws IOException {
        Epic epic = TestTaskFactory.createSampleEpic(0);
        taskManager.addEpic(epic);
        taskManager.addSubtask(TestTaskFactory.createSampleSubtask(0, epic.getId()));
        taskManager.addSubtask(TestTaskFactory.createSampleSubtask(1, epic.getId()));
        taskManager.save();

        FileBackedTaskManager taskManagerFromFile = FileBackedTaskManager.loadFromFile(taskManager.getStateFile());
        //проверяем, что в эпике есть 2 подзадачи
        Epic loadedEpic = taskManagerFromFile.getEpics().get(0);
        Assertions.assertEquals(2, loadedEpic.getSubtasks().size());
    }
}
