package com.buihien.datn.messageQueue;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.Token;
import com.buihien.datn.dto.LogMessageQueueDto;
import com.buihien.datn.service.LogMessageQueueService;
import com.buihien.datn.service.MailService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
public class MessageQueueListener {

    @Value("${message.queue.forgot-password-threads:2}")
    private int forgotPasswordThreads;

    @Value("${message.queue.retry-max-times:3}")
    private int maxRetry;

    @Autowired
    @Qualifier("forgotPasswordQueueService")
    private MessageQueueService<Token> forgotPasswordQueueService;

    @Autowired
    private MailService mailService;

    @Autowired
    private LogMessageQueueService logMessageQueueService;

    private ExecutorService forgotPasswordExecutor;


    @PostConstruct
    public void initExecutors() {
        this.forgotPasswordExecutor = new ThreadPoolExecutor(
                forgotPasswordThreads,
                forgotPasswordThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

    }


    @Scheduled(fixedRate = 5000) // Kiểm tra mỗi 5 giây
    public void processMessages() {
        // Xử lý cho quên mật khẩu
        final int totalForgotPasswordMessages = forgotPasswordQueueService.getQueueSize();
        final int messagesPerThreadForgotPassword = totalForgotPasswordMessages / forgotPasswordThreads + (totalForgotPasswordMessages % forgotPasswordThreads == 0 ? 0 : 1);

        for (int t = 0; t < forgotPasswordThreads; t++) { // Chạy số luồng cấu hình cho quên mật khẩu
            final int startIndex = t * messagesPerThreadForgotPassword; // Final value to be used in lambda
            final int endIndex = Math.min(startIndex + messagesPerThreadForgotPassword, totalForgotPasswordMessages); // Final value to be used in lambda

            forgotPasswordExecutor.submit(() -> {
                List<RetryableMessage<Token>> failed = new ArrayList<>();
                for (int i = startIndex; i < endIndex; i++) {
                    Token message = forgotPasswordQueueService.receiveMessage();
                    if (message != null) {
                        try {
                            if (message.getUser()!= null) {
                                mailService.sendConfirmLink(message.getUser().getEmail(), message.getUser().getUsername(), message.getResetToken());
                            }
                            // Lưu log vào bảng log_message_queue
                            LogMessageQueueDto logMessageQueueDto = new LogMessageQueueDto();
                            logMessageQueueDto.setMessage("Gửi mail quên mật khẩu: " + message.getUser().getUsername());
                            logMessageQueueDto.setAction("Gửi mail quên mật khẩu");
                            logMessageQueueDto.setStatus(DatnConstants.LogMessageQueueStatus.SUCCESS.getValue());
                            logMessageQueueDto.setType(DatnConstants.LogMessageQueueTypes.FORGOT_PASSWORD.getValue());
                            logMessageQueueService.saveOrUpdate(logMessageQueueDto);
                            // Thành công: Xóa khỏi hàng đợi
                            forgotPasswordQueueService.removeMessage(message);
                        } catch (Exception e) {
                            // Thất bại: Tăng số lần retry và xử lý nếu cần
                            RetryableMessage<Token> retryableMessage = new RetryableMessage<>(message);
                            retryableMessage.incrementRetryCount();
                            if (retryableMessage.getRetryCount() >= maxRetry) {
                                // Lưu log vào bảng log_message_queue
                                LogMessageQueueDto logMessageQueueDto = new LogMessageQueueDto();
                                logMessageQueueDto.setMessage("Gửi mail quên mật khẩu: " + message.getUser().getUsername());
                                logMessageQueueDto.setAction("Gửi mail quên mật khẩu");
                                logMessageQueueDto.setStatus(DatnConstants.LogMessageQueueStatus.FAILED.getValue());
                                logMessageQueueDto.setType(DatnConstants.LogMessageQueueTypes.FORGOT_PASSWORD.getValue());
                                logMessageQueueService.saveOrUpdate(logMessageQueueDto);
                                forgotPasswordQueueService.removeMessage(message);
                            } else {
                                failed.add(retryableMessage);
                            }
                        }
                    }
                }

                // Nếu có message thất bại, thử lại
                if (!failed.isEmpty()) {
                    forgotPasswordQueueService.requeueMessages(failed);
                }
            });
        }
    }
}
