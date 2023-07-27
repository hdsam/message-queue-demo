package com.hdsam.message.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Recv
 *
 * @author Yeo
 * @date 2023/7/21
 */
public class Recv {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new  String(message.getBody(), "UTF-8");
            System.out.println(" ConsumerTag: " + consumerTag);
            System.out.println(" DeliveryTag: " + message.getEnvelope().getDeliveryTag());
            System.out.println(" [x] Received '" + msg + "'");
        };

        CancelCallback cancelCallback = consumerTag -> {};

        channel.basicQos(5);
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);


    }
}
