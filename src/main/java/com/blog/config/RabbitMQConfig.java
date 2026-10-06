package com.blog.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String COMMENT_QUEUE = "comment.notification.queue";
    public static final String COMMENT_EXCHANGE = "comment.notification.exchange";
    public static final String COMMENT_ROUTING_KEY = "comment.notification.key";

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public Queue commentQueue() {
        return new Queue(COMMENT_QUEUE, true);
    }

    @Bean
    public DirectExchange commentExchange() {
        return new DirectExchange(COMMENT_EXCHANGE);
    }

    @Bean
    public Binding commentBinding(Queue commentQueue, DirectExchange commentExchange) {
        return BindingBuilder.bind(commentQueue).to(commentExchange).with(COMMENT_ROUTING_KEY);
    }
}
