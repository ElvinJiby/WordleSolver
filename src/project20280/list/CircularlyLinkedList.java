package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class CircularlyLinkedList<E> implements List<E> {

    private class Node<T> {
        private final T data;
        private Node<T> next;

        public Node(T e, Node<T> n) {
            data = e;
            next = n;
        }

        public T getData() {
            return data;
        }

        public void setNext(Node<T> n) {
            next = n;
        }

        public Node<T> getNext() {
            return next;
        }
    }

    private Node<E> tail = null;
    private int size = 0;

    public CircularlyLinkedList() {

    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int i) {
        if (i > size || i < 0 || isEmpty()) return null;

        Node<E> currentNode = tail.getNext();
        for (int j = 0; j<i; j++) {
            currentNode = currentNode.next;
        }
        return currentNode.getData();
    }

    @Override
    public void add(int i, E e) {
        if (i > size || i < 0) {
            System.out.println("Invalid position to insert element.");
            return;
        }
        if (isEmpty()) addFirst(e);

        Node<E> currentNode = tail;
        for (int j = 0; j<i; j++) {
            currentNode = currentNode.getNext();
        }

        Node<E> newNode = new Node<E>(e, currentNode.getNext());
        currentNode.setNext(newNode);
        size++;
    }

    @Override
    public E remove(int i) {
        if (isEmpty() || i < 0 || i > size) return null;

        Node<E> previousNode = tail;
        Node<E> currentNode = tail.getNext();

        for (int j = 0; j<i; j++) {
            previousNode = currentNode;
            currentNode = currentNode.getNext();
        }
        previousNode.setNext(currentNode.getNext());
        size--;
        return currentNode.getData();
    }

    public void rotate() {
        if (tail != null) tail = tail.getNext();
    }

    private class CircularlyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) tail;

        @Override
        public boolean hasNext() {
            return curr != tail;
        }

        @Override
        public E next() {
            E res = curr.data;
            curr = curr.next;
            return res;
        }

    }

    @Override
    public Iterator<E> iterator() {
        return new CircularlyLinkedListIterator<E>();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) return null;

        Node<E> head = tail.getNext();

        if (head == tail) tail = null; // if there's only one node
        else tail.setNext(head.getNext()); // set the new head to the 2nd node
        size--;

        return head.getData();
    }

    @Override
    public E removeLast() {
        if (isEmpty()) return null;

        E removedElement = tail.getData();
        Node<E> currentNode = tail.getNext();
        while (currentNode.getNext() != tail) {
            currentNode = currentNode.getNext();
        }
        currentNode.setNext(tail.getNext());
        tail = currentNode;
        size--;

        return removedElement;
    }

    @Override
    public void addFirst(E e) {
        if (isEmpty()) {
            tail = new Node<E>(e, null);
            tail.setNext(tail);
        } else {
            Node<E> newNode = new Node<E>(e, tail.getNext());
            tail.setNext(newNode);
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        addFirst(e);
        rotate();
    }


    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = tail;
        do {
            curr = curr.next;
            sb.append(curr.data);
            if (curr != tail) {
                sb.append(", ");
            }
        } while (curr != tail);
        sb.append("]");
        return sb.toString();
    }


    public static void main(String[] args) {
        CircularlyLinkedList<Integer> ll = new CircularlyLinkedList<Integer>();
        for (int i = 10; i < 20; ++i) {
            ll.addLast(i);
        }

        System.out.println(ll);

        ll.removeFirst();
        System.out.println(ll);

        ll.removeLast();
        System.out.println(ll);

        ll.rotate();
        System.out.println(ll);

        ll.removeFirst();
        ll.rotate();
        System.out.println(ll);

        ll.removeLast();
        ll.rotate();
        System.out.println(ll);

        for (Integer e : ll) {
            System.out.println("value: " + e);
        }

    }
}
