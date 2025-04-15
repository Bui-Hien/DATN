package com.buihien.datn.messageQueue;

import com.buihien.datn.domain.Token;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageQueueConfig {

    @Bean(name = "forgotPasswordQueue")
    public InMemoryMessageQueue<Token> forgotPasswordQueue() {
        return new InMemoryMessageQueue<>();
    }

    @Bean(name = "forgotPasswordQueueService")
    @Qualifier("forgotPasswordQueueService")
    public MessageQueueService<Token> forgotPasswordQueueService(
            @Qualifier("forgotPasswordQueue") InMemoryMessageQueue<Token> queue) {
        return new MessageQueueService<>(queue);
    }
}