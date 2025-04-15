package com.buihien.datn.messageQueue;

public class RetryableMessage<T> {
    private T data;
    private int retryCount;

    public RetryableMessage(T data) {
        this.data = data;
        this.retryCount = 0;
    }

    public T getData() {
        return data;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void incrementRetryCount() {
        this.retryCount++;
    }
}
