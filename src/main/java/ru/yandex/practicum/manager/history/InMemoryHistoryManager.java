package ru.yandex.practicum.manager.history;

import ru.yandex.practicum.model.Node;
import ru.yandex.practicum.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Менеджер, отвечающий за хранение истории просмотров задач в оперативной памяти
 */
public class InMemoryHistoryManager implements HistoryManager {

    /**
     * Голова списка с историей просмотра задач
     * @implNote Согласно ТЗ 6го спринта:
     * Отдельный класс для списка создавать не нужно — реализуйте его прямо в классе InMemoryHistoryManager
     */
    private Node head = null;

    /**
     * Хвост списка с историей просмотра задач
     */
    private Node tail = null;

    /**
     * Мапа для получения задачи за O(1) из списка по её идентификатору
     */
    private final HashMap<Integer, Node> historyMap = new HashMap<>();

    /**
     * Удалить узел из списка просмотров
     * @param node
     * @implNote Согласно ТЗ работает за O(1)
     */
    private void removeNode(Node node) {
        if (node.getPrev() == null) {
            // удаление первого узла - заменяем голову на следующий за ней узел
            head = node.getNext();
        } else {
            // удаление узла, не являющегося головой - у предыдущего узла устанавливаем ссылку следующий за ним узел
            node.getPrev().setNext(node.getNext());
        }

        if (node.getNext() == null) {
            // удаление узла-хвоста списка - заменяем хвост на предыдущий за ним узел
            tail = node.getPrev();
        } else {
            // удаление узла, не являющегося хвостом списка - у следующего узла устанавливаем ссылку предыдущий
            // за ним узел
            node.getNext().setPrev(node.getPrev());
        }

        // удаление узла из мапы по его идентификатору
        historyMap.remove(node.getData().getId());
    }

    /**
     * Получить список задач из списка просмотров
     *
     * @ImplNote Согласно ТЗ 6го спринта:
     * getTasks — должны собирать все задачи из связанного списка в обычный ArrayList
     */
    private ArrayList<Task> getTasks() {
        ArrayList<Task> history = new ArrayList<>();
        Node node = head;
        while (node != null) {
            history.add(node.getData());
            node = node.getNext();
        }
        return history;
    }

    /**
     * Добавить задачу в конец списка просмотров
     * @param task
     */
    private void linkLast(Task task) {
        Node newNode = new Node(tail, task, null);
        if (head == null) {
            // добавление первого узла - инициализируем голову списка
            head = newNode;
        } else {
            // добавление узла в конец списка - для хвоста устанавливаем ссылку на новый узел в качестве следующего
            // за ним узла
            tail.setNext(newNode);
        }
        // хвост теперь ссылается на новый узел
        tail = newNode;

        // добавление нового узла в мапу
        historyMap.put(task.getId(), newNode);
    }

    @Override
    public void add(Task task) {
        if (historyMap.containsKey(task.getId())) {
            // задача есть в истории просмотра - удаление данных о ее просмотре из истории
            removeNode(historyMap.get(task.getId()));
        }
        // добавление задачи в конец списка просмотров
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        removeNode(historyMap.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
