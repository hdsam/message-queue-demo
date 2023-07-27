package com.hdsam.message.spring.tut5;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tut5Sender
 *
 * @author Yeo
 * @date 2023/7/24
 */
public class Tut5Sender {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange topicExchange;

    AtomicInteger index = new AtomicInteger(0);

    AtomicInteger count = new AtomicInteger(0);

    private static final String[] KEYS = {
            "quick.orange.rabbit",
            "lazy.orange.elephant",
            "quick.orange.fox",
            "lazy.brown.fox",
            "lazy.pink.rabbit",
            "quick.brown.fox"
    };

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        StringBuilder builder = new StringBuilder("Hello to ");
        if (this.index.incrementAndGet() == 3) {
            this.index.set(0);
        }
        String key = KEYS[this.index.get()];
        builder.append(key).append(" ");
        builder.append(this.count.incrementAndGet());
        String message = builder.toString();
        this.rabbitTemplate.convertAndSend(topicExchange.getName(), key, message);
        System.out.println(" [x] Sent '" + message + "'");
    }
}
