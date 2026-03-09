package com.prelude.rabbitmq.controller;

import com.prelude.rabbitmq.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ProducerController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/")
    public String sendMessage(@RequestBody Map<String, Object> message){

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                message
        );
        return "Message Sent!";
    }
}
