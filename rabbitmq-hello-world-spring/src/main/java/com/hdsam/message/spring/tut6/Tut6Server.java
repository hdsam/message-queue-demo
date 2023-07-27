package com.hdsam.message.spring.tut6;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * Tut6Server
 *
 * @author Yeo
 * @date 2023/7/24
 */
public class Tut6Server {

    @RabbitListener(queues = "tut.rpc.requests")
    public int fibonacci(int i) {
        System.out.println(" [x] Received request for" + i);
        int result = fib(i);
        System.out.println(" [.] Returned " + result);
        return result;
    }

    private int fib(int i) {
        return (i == 0 || i == 1) ? 1 : fib(i - 1) + fib(i - 2);
    }
}
