package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class SinglyLinkedList<E> implements List<E> {

    private static class Node<E> {
        private final E element;            // reference to the element stored at this node
        private Node<E> next;         // reference to the subsequent node in the list

        public Node(E e, Node<E> n) {
            element = e;
            next = n;
        }

        // Accessor methods
        public E getElement() {
            return element;
        }
        public Node<E> getNext() {
            return next;
        }

        // Modifier methods
        public void setNext(Node<E> n) {
            next = n;
        }

    } //----------- end of nested Node class -----------

    private Node<E> head = null;               // head node of the list (or null if empty)
    private int size = 0;                      // number of nodes in the list

    public SinglyLinkedList() {
    }              // constructs an initially empty list

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int position) {
        if (position > size || position < 0) return null;

        Node<E> currentNode = head;
        for (int i = 0; i<position; i++) {
            currentNode = currentNode.next;
        }
        return currentNode.getElement();
    }

    @Override
    public void add(int position, E e) {
        if (position < 0 || position > size) {
            System.out.println("Cannot add element at that index.");
            return;
        }

        if (isEmpty() || position == 0) addFirst(e);
        else {
            Node<E> currentNode = head;

            for (int i = 1; i<position; i++) {
                currentNode = currentNode.next;
            }

            Node<E> newNode = new Node<E>(e, currentNode.next);
            currentNode.setNext(newNode);
            size++;
        }
    }


    @Override
    public void addFirst(E e) {
        head = new Node<E>(e, head);
        size++;
    }

    @Override
    public void addLast(E e) {
        if (isEmpty()) addFirst(e);
        else {
            Node<E> currentNode = head;

            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }

            currentNode.setNext(new Node<E>(e, null));
            size++;
        }
    }

    @Override
    public E remove(int position) {
        if (isEmpty() || position < 0 || position > size) return null;
        if (position == 0) return removeFirst();

        Node<E> previousNode = new Node<>(null,head);
        Node<E> currentNode = head;

        for (int i = 0; i<position; i++) {
            previousNode = currentNode;
            currentNode = currentNode.next;
        }

        previousNode.setNext(currentNode.next);
        size--;
        return currentNode.getElement();
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) return null;

        E headElement = head.getElement();
        head = head.next;
        size--;
        return headElement;
    }

    @Override
    public E removeLast() {
        if (isEmpty()) return null;
        if (size == 1) return removeFirst();

        Node<E> previousNode = new Node<>(null, head);
        Node<E> currentNode = head;
        while (currentNode.next != null) {
            previousNode = currentNode;
            currentNode = currentNode.next;
        }

        previousNode.setNext(null);
        size--;
        return currentNode.getElement();
    }

    @Override
    public Iterator<E> iterator() {
        return new SinglyLinkedListIterator<E>();
    }

    private class SinglyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head;

        @Override
        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public E next() {
            E res = curr.getElement();
            curr = curr.next;
            return res;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head;
        while (curr != null) {
            sb.append(curr.getElement());
            if (curr.getNext() != null)
                sb.append(", ");
            curr = curr.getNext();
        }
        sb.append("]");
        return sb.toString();
    }
}
