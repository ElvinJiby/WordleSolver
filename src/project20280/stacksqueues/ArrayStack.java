package project20280.stacksqueues;

import project20280.interfaces.Stack;

public class ArrayStack<E> implements Stack<E> {

    public static final int CAPACITY = 100; // default array capacity
    private final E[] data;  // generic array used for storage
    private int t = -1;  // index of the top element in stack

    public ArrayStack() {
        this(CAPACITY);
    }  // constructs stack with default capacity
    @SuppressWarnings({"unchecked"})
    public ArrayStack(int capacity) { // constructs stack with given capacity
        data = (E[]) new Object[capacity];
    }

    @Override
    public int size() {
        return (t + 1);
    }
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void push(E e) {
        if (size() == data.length) throw new IllegalStateException("ArrayStack is Full.");

        t++;
        data[t] = e;
    }

    @Override
    public E top() {
        if (isEmpty()) return null;
        return data[t];
    }

    @Override
    public E pop() {
        if (isEmpty()) return null;

        E removedElement = data[t];
        data[t] = null;
        t--;
        return removedElement;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        if (!isEmpty()) {
            for (int i = t; i >= 0; --i) {
                sb.append(data[i]);
                if (i != 0) sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        Stack<Integer> S = new ArrayStack<>();  // contents: ()
        S.push(5);                              // contents: (5)
        System.out.println(S);
        S.push(3);                              // contents: (5, 3)
        System.out.println(S);
        System.out.println(S.size());           // contents: (5, 3)     outputs 2
        System.out.println(S.pop());            // contents: (5)        outputs 3
        System.out.println(S.isEmpty());        // contents: (5)        outputs false
        System.out.println(S.pop());            // contents: ()         outputs 5
        System.out.println(S.isEmpty());        // contents: ()         outputs true
        System.out.println(S.pop());            // contents: ()         outputs null
        S.push(7);                              // contents: (7)
        S.push(9);                              // contents: (7, 9)
        System.out.println(S.top());            // contents: (7, 9)     outputs 9
        S.push(4);                              // contents: (7, 9, 4)
        System.out.println(S.size());           // contents: (7, 9, 4)  outputs 3
        System.out.println(S.pop());            // contents: (7, 9)     outputs 4
        S.push(6);                              // contents: (7, 9, 6)
        S.push(8);                              // contents: (7, 9, 6, 8)
        System.out.println(S.pop());            // contents: (7, 9, 6)  outputs 8
        System.out.println(S);
    }
}
