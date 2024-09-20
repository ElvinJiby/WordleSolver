package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class DoublyLinkedList<E> implements List<E> {

    private static class Node<E> {
        private final E data;
        private Node<E> next;
        private Node<E> prev;

        public Node(E e, Node<E> p, Node<E> n) {
            data = e;
            prev = p;
            next = n;
        }

        public E getData() {
            return data;
        }

        public Node<E> getNext() {
            return next;
        }

        public Node<E> getPrev() {
            return prev;
        }

        public void setNext(Node<E> next) {
            this.next = next;
        }
        public void setPrev(Node<E> prev) {
            this.prev = prev;
        }
    }

    private final Node<E> head;
    private final Node<E> tail;
    private int size = 0;

    public DoublyLinkedList() {
        head = new Node<E>(null, null, null);
        tail = new Node<E>(null, head, null);
        head.next = tail;
    }

    private void addBetween(E e, Node<E> pred, Node<E> succ) {
        Node<E> newNode = new Node<E>(e, pred, succ);
        pred.setNext(newNode);
        succ.setPrev(newNode);
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int i) {
        if (isEmpty() || i > size || i < 0) return null;

        Node<E> currentNode = head.getNext();
        for (int j = 0; j<i; j++) {
            currentNode = currentNode.next;
        }
        return currentNode.getData();
    }

    @Override
    public void add(int i, E e) {
        if (isEmpty() || i > size || i < 0) addFirst(e);
        else {
            Node<E> currentNode = head.getNext();

            for (int j = 1; j<i; j++) {
                currentNode = currentNode.next;
            }

            Node<E> newNode = new Node<E>(e, currentNode, currentNode.next);
            currentNode.setNext(newNode);
            currentNode.next.setPrev(newNode);
            size++;
        }
    }

    @Override
    public E remove(int i) {
        if (isEmpty() || i < 0 || i > size) return null;
        if (i == 0) return removeFirst();

        Node<E> previousNode = new Node<E>(null,null,head);
        Node<E> currentNode = head.getNext();

        for (int j = 0; j<i; j++) {
            previousNode = currentNode;
            currentNode = currentNode.next;
        }

        previousNode.setNext(currentNode.next);
        currentNode.next.setPrev(previousNode);
        size--;
        return currentNode.getData();
    }

    private class DoublyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head.next;

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
        return new DoublyLinkedListIterator<E>();
    }

    private E remove(Node<E> n) {
        Node<E> previousNode = n.getPrev();
        Node<E> nextNode = n.getNext();

        previousNode.setNext(nextNode);
        nextNode.setPrev(previousNode);
        size--;
        return n.getData();
    }

    public E first() {
        if (isEmpty()) {
            return null;
        }
        return head.next.getData();
    }

    public E last() {
        if (isEmpty()) return null;
        return tail.getPrev().getData();
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) return null;
        return remove(head.getNext());
    }

    @Override
    public E removeLast() {
        if (isEmpty()) return null;
        return remove(tail.getPrev());
    }

    @Override
    public void addLast(E e) {
        addBetween(e, tail.getPrev(), tail);
    }

    @Override
    public void addFirst(E e) {
        addBetween(e, head, head.getNext());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head.next;
        while (curr != tail) {
            sb.append(curr.data);
            curr = curr.next;
            if (curr != tail) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<Integer>();
        ll.addFirst(0);
        ll.addFirst(1);
        ll.addFirst(2);
        ll.addLast(-1);
        System.out.println(ll);

        ll.removeFirst();
        System.out.println(ll);

        ll.removeLast();
        System.out.println(ll);

        for (Integer e : ll) {
            System.out.println("value: " + e);
        }
    }
}