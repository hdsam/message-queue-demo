package com.hdsam.message.spring.tut1;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * Tut1Receiver
 *
 * @author Yeo
 * @date 2023/7/21
 */
@RabbitListener(queues = "hello")
public class Tut1Receiver {

    @RabbitHandler
    public void receive(String in){
        System.out.println(" [x] Received '" + in + "'");
    }
}
