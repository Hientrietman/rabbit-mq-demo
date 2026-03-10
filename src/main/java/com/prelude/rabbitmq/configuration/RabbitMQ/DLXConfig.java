package com.prelude.rabbitmq.configuration.RabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DLXConfig {

    private final String DLX_QUEUE = "DLX.QUEUE";
    private final String DLX_EXCHANGE = "DLX.EXCHANGE";
    private final String DLX_ROUTING_KEY = "DLX.ROUTING.KEY";


    @Bean
    public Queue deadLetterQueue (){
        return QueueBuilder.durable(DLX_QUEUE).build();
    }

    @Bean
    public DirectExchange deadLetterExchange(){
        return new DirectExchange(DLX_EXCHANGE);
    }

    @Bean Binding dlxBinding(DirectExchange directExchange){
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DLX_ROUTING_KEY);
    }
}
