package project20280.stacksqueues;

import project20280.interfaces.Queue;
import project20280.list.CircularlyLinkedList;

/**
 * Realization of a circular FIFO queue as an adaptation of a
 * CircularlyLinkedList. This provides one additional method not part of the
 * general Queue interface. A call to rotate() is a more efficient simulation of
 * the combination enqueue(dequeue()). All operations are performed in constant
 * time.
 */

public class LinkedCircularQueue<E> implements Queue<E> {
    private CircularlyLinkedList<E> circularlyLinkedList;
    public LinkedCircularQueue() {
        circularlyLinkedList = new CircularlyLinkedList<E>();
    }
    @Override
    public int size() {
        return circularlyLinkedList.size();
    }

    @Override
    public boolean isEmpty() {
        return circularlyLinkedList.isEmpty();
    }

    @Override
    public void enqueue(E e) {
        circularlyLinkedList.addLast(e);
    }

    @Override
    public E first() {
        return circularlyLinkedList.get(0);
    }

    @Override
    public E dequeue() {
        return circularlyLinkedList.removeFirst();
    }

}
