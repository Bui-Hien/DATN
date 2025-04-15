package com.buihien.datn.messageQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class InMemoryMessageQueue<T> implements MessageQueue<T> {

    private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

    @Override
    public void enqueue(T message) {
        queue.add(message);
    }

    @Override
    public T dequeue() {
        return queue.poll();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public void removeMessage(T message) {
        queue.remove(message);
    }
}
