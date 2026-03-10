package com.prelude.rabbitmq.configuration.RabbitMQ;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ACKConfig {

    @Bean("manualFactory")
    public SimpleRabbitListenerContainerFactory manualFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) { // Spring tự tìm Bean converter đã khai báo chỗ khác để nhét vào đây

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }

    @Bean("autoFactory")
    public SimpleRabbitListenerContainerFactory autoFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }
}