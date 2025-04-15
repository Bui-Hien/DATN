package com.buihien.datn.messageQueue;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageQueueService<T> {

    private final MessageQueue<T> messageQueue;

    public MessageQueueService(MessageQueue<T> messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void sendMessage(T message) {
        messageQueue.enqueue(message);
        System.out.println("Đã thêm thông điệp: " + message);
    }

    public T receiveMessage() {
        return messageQueue.dequeue();
    }

    public List<T> receiveMessages(int batchSize) {
        List<T> messages = new ArrayList<>();
        T message;

        // Lấy các thông điệp trong một batch
        for (int i = 0; i < batchSize; i++) {
            message = messageQueue.dequeue();
            if (message != null) {
                messages.add(message);
            } else {
                break; // Hết message
            }
        }

        return messages;
    }

    public int getQueueSize() {
        return messageQueue.size();
    }

    public boolean isQueueEmpty() {
        return messageQueue.isEmpty();
    }

    public void requeueMessages(List<RetryableMessage<T>> failedMessages) {
        // Thực hiện đưa các message thất bại vào lại hàng đợi
        for (RetryableMessage<T> message : failedMessages) {
            messageQueue.enqueue(message.getData());
        }
    }

    public void removeMessage(T message) {
        messageQueue.removeMessage(message);
    }
}
