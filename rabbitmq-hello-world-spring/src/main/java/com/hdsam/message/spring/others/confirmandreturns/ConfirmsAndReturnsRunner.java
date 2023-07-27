package com.hdsam.message.spring.others.confirmandreturns;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.hdsam.message.spring.others.confirmandreturns.ConfirmsAndReturnsConfig.QUEUE;

/**
 * ReturnsAndConfirmsDemoRunner
 *
 * @author Yeo
 * @date 2023/7/26
 */
public class ConfirmsAndReturnsRunner implements ApplicationRunner {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final CountDownLatch listenLatch = new CountDownLatch(1);


    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.runDemo();
    }

    public void runDemo() throws ExecutionException, InterruptedException, TimeoutException {
        setCallbacks();
        // send normal
        CorrelationData correlationData = new CorrelationData("correlation for message 1");
        rabbitTemplate.convertAndSend("", QUEUE, "foo", correlationData);
        CorrelationData.Confirm confirm = correlationData.getFuture().get(10L, TimeUnit.SECONDS);
        System.out.println("Confirm received for good delivery, ack = " + confirm.isAck());
        if (listenLatch.await(10L, TimeUnit.SECONDS)) {
            System.out.println("Message received by listener");
        } else {
            System.out.println("Message not received by listener");
        }

        // send to not-existence queue
        correlationData = new CorrelationData("correlation for message 2");
        rabbitTemplate.convertAndSend("", QUEUE + QUEUE, "bar", msg -> {
            System.out.println("Message after conversion: " + msg);
            return msg;
        }, correlationData);
        confirm = correlationData.getFuture().get(10, TimeUnit.SECONDS);
        System.out.println("Confirm received for send to missing queue, ack = " + confirm.isAck());
        System.out.println("Return received:" + correlationData.getReturned());

        // send to not-existence exchange
        correlationData = new CorrelationData("correlation for message 3");
        rabbitTemplate.convertAndSend(UUID.randomUUID().toString(), QUEUE, "baz", correlationData);
        confirm = correlationData.getFuture().get(10, TimeUnit.SECONDS);
        System.out.println("Confirm received for send to missing exchange, ack = " + confirm.isAck());
    }

    void setCallbacks() {
        this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (correlationData != null) {
                System.out.println("Received " + (ack ? "ack" : "nack") + " for correlation : " + correlationData);
            }
        });

        this.rabbitTemplate.setReturnsCallback(returned ->
                System.out.println("Returned " + returned.getMessage()
                        + "\n replyCode:" + returned.getReplyCode()
                        + "\n replyText:" + returned.getReplyText()
                        + "\n exchange:" + returned.getExchange()
                        + "\n routingKey:" + returned.getRoutingKey()));
    }

    @RabbitListener(queues = QUEUE)
    public void listen(String in) {
        System.out.println("Listener received: " + in);
        this.listenLatch.countDown();
    }

}
