package ru.yandex.practicum.exception;

/**
 * Исключение при попытке сохранения состояния менеджера
 * @implNote Согласно ТЗ спринта 7:
 * Исключения вида IOException нужно отлавливать внутри метода save и
 * выкидывать собственное непроверяемое исключение ManagerSaveException.
 */
public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message, Exception e) {
        super(message, e);
    }
}
