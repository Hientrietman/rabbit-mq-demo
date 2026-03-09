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

    @PostMapping("/fan-out")
    public String sendMessageFanOut(@RequestBody Map<String, Object> message){

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.FANOUT_EXCHANGE,
                "",
                message
        );
        return "Message Sent!";
    }

    @PostMapping("/topic/all")
    public String sendToAllTopic(@RequestBody Map<String, Object> message){

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.TOPIC_EXCHANGE,
                "learning.topic.test", // hoặc "learning.topic.created", "learning.topic.updated"
                message
        );
        return "Message Sent!";
    }
    @PostMapping("/topic/created")
    public String sendToCreated(@RequestBody Map<String, Object> message){

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.TOPIC_EXCHANGE,
                "learning.topic.created", // hoặc "learning.topic.created", "learning.topic.updated"
                message
        );
        return "Message Sent!";
    }

    @PostMapping("/topic/updated")
    public String sendToUpdated(@RequestBody Map<String, Object> message){

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.TOPIC_EXCHANGE,
                "learning.topic.updated", // hoặc "learning.topic.created", "learning.topic.updated"
                message
        );
        return "Message Sent!";
    }
}
