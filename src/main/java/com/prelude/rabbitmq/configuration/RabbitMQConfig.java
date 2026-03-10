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

    public static final String TOPIC_QUEUE_ALL = "learning.topic";
    public static final String TOPIC_QUEUE_CREATED = "learning.topic.created";
    public static final String TOPIC_QUEUE_UPDATED = "learning.topic.updated";
    public static final String TOPIC_EXCHANGE = "learning.topicExchange";

    public static final String MANUAL_ACK_QUEUE = "learning.manual_ack";
    public static final String MANUAL_ACK_ROUTING_KEY = "learning.manual_ack.routingKey";

    // 1. Khai báo Queue
    //Direct
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true); // true = durable (không mất khi restart)
    }

    //Fan-Out
    @Bean public Queue queueA() { return new Queue(QUEUE_A); }
    @Bean public Queue queueB() { return new Queue(QUEUE_B); }

    //Topic
    @Bean public Queue topicQueue() { return new Queue(TOPIC_QUEUE_ALL); }
    @Bean public Queue topicQueue2() { return new Queue(TOPIC_QUEUE_CREATED); }
    @Bean public Queue topicQueue3() { return new Queue(TOPIC_QUEUE_UPDATED); }

    @Bean public Queue testManualACKQueue() {return new Queue(MANUAL_ACK_QUEUE);}

    // 2. Khai báo Exchange (Direct)
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }
    // Khai báo Fanout Exchange
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }
    // Khai báo Topic Exchange
    @Bean public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }
    // 3. Binding (Nối Queue vào Exchange thông qua Routing Key)
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
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
    // Binding queue để nhận tất cả message (wildcard *)
    @Bean
    public Binding topicBindingAll(Queue topicQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue).to(topicExchange).with(TOPIC_QUEUE_ALL);
    }

    // Binding queue nhận created messages
    @Bean
    public Binding topicBindingCreated(Queue topicQueue2, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue2).to(topicExchange).with(TOPIC_QUEUE_CREATED);
    }

    // Binding queue nhận updated messages
    @Bean
    public Binding topicBindingUpdated(Queue topicQueue3, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue3).to(topicExchange).with(TOPIC_QUEUE_UPDATED);
    }
    @Bean
    public Binding manualAck(Queue testManualACKQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(testManualACKQueue).to(directExchange).with(MANUAL_ACK_ROUTING_KEY);
    }
    // 4. Converter để gửi/nhận dưới dạng JSON (Best Practice)
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}