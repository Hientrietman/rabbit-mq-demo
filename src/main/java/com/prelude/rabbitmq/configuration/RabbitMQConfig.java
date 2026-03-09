package com.prelude.rabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "learning.queue";
    public static final String EXCHANGE_NAME = "learning.exchange";
    public static final String ROUTING_KEY = "learning.routingKey";

    public static final String QUEUE_A = "learning.queuea";
    public static final String QUEUE_B = "learning.queueb";

    public static final String FANOUT_EXCHANGE = "learning.fanout";
    // 1. Khai báo Queue
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true); // true = durable (không mất khi restart)
    }

    @Bean public Queue queueA() { return new Queue(QUEUE_A); }
    @Bean public Queue queueB() { return new Queue(QUEUE_B); }

    // 2. Khai báo Exchange (Direct)
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    // 3. Binding (Nối Queue vào Exchange thông qua Routing Key)
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    // Khai báo Fanout Exchange
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    // Binding không cần .with(routingKey) nữa
    @Bean
    public Binding bindA(Queue queueA, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queueA).to(fanoutExchange);
    }

    @Bean
    public Binding bindB(Queue queueB, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queueB).to(fanoutExchange);
    }

    // 4. Converter để gửi/nhận dưới dạng JSON (Best Practice)
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}