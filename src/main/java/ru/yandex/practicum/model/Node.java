package ru.yandex.practicum.model;

/**
 * Узел списка истории просмотра
 */
public class Node {
    /**
     * Следующий узел в списке
     */
    private Node next;
    /**
     * Предыдущий узел в списке
     */
    private Node prev;
    /**
     * Данные узла
     */
    private Task data;

    /**
     * Конструктор
     * @param prev Следующий узел в списке
     * @param data Данные узла
     * @param next Предыдущий узел в списке
     */
    public Node(Node prev, Task data, Node next) {

        this.prev = prev;
        this.data = data;
        this.next = next;
    }

    /**
     * Получить следующий узел в списке.
     */
    public Node getNext() {
        return next;
    }

    /**
     * Установить следующий узел в списке.
     */
    public void setNext(Node node) {
        next = node;
    }

    /**
     * Получить предыдущий узел в списке.
     */
    public Node getPrev() {
        return prev;
    }

    /**
     * Установить предыдущий узел в списке.
     */
    public void setPrev(Node node) {
        prev = node;
    }

    /**
     * Получить данные узла
     */
    public Task getData() {
        return data;
    }
}
