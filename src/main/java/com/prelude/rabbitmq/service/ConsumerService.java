package com.prelude.rabbitmq.service;

import com.prelude.rabbitmq.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ConsumerService {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(Map<String, Object> message) {
        System.out.println("Đã nhận tin nhắn từ Queue: " + message.get("message"));
        // Bạn có thể xử lý logic CRUD, gửi mail... ở đây
    }
}
