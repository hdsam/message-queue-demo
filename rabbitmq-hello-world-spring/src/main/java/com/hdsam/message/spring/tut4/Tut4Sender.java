package com.hdsam.message.spring.tut4;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tut4Sender
 *
 * @author Yeo
 * @date 2023/7/24
 */
public class Tut4Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DirectExchange directExchange;

    AtomicInteger index = new AtomicInteger(0);

    AtomicInteger count = new AtomicInteger(0);

    private static final String[] KEYS = {"orange", "black", "green"};

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
        this.rabbitTemplate.convertAndSend(directExchange.getName(), key, message);
        System.out.println(" [x] Sent '" + message + "'");
    }
}
