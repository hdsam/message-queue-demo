package com.hdsam.message.spring.others.ttl;

import org.joda.time.DateTime;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TtlSender
 *
 * @author Yeo
 * @date 2023/7/26
 */
public class TtlSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Qualifier("ttlExchange")
    @Autowired
    private DirectExchange directExchange;

    AtomicInteger count = new AtomicInteger(0);

    @Scheduled(fixedDelay = 1000L, initialDelay = 500L)
    public void send(){
        StringBuilder builder = new StringBuilder("hello world ");
        int index = count.incrementAndGet();
        String message = builder.append(index).toString();
        rabbitTemplate.convertAndSend(directExchange.getName(), "", message);
        System.out.println(DateTime.now().toString("yyyy-MM-dd HH:mm:ss") +
                " [x]  Sent message '" + message + "'");
    }
}
