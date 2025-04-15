package com.buihien.datn.messageQueue;

public interface MessageQueue<T> {
    void enqueue(T message);
    T dequeue();
    boolean isEmpty();
    int size();
    void removeMessage(T message);
}
