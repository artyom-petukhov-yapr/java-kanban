package ru.yandex.practicum.manager.task.prefilled;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.TestTaskFactory;
import ru.yandex.practicum.manager.task.FileBackedTaskManager;
import ru.yandex.practicum.model.Epic;

import java.io.IOException;

/**
 * Тесты для предзаполненного {@link FileBackedTaskManager}
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
}
