package com.hdsam.message.workqueues;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Recv
 *
 * @author Yeo
 * @date 2023/7/21
 */
public class Worker {

    private static final String QUEUE_NAME = "hello2";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            try {
                String msg = new String(message.getBody(), "UTF-8");
                System.out.println(" ConsumerTag: " + consumerTag);
                System.out.println(" DeliveryTag: " + message.getEnvelope().getDeliveryTag());
                System.out.println(" [x] Received '" + msg + "'");
                doWork(msg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };
        CancelCallback cancelCallback = consumerTag -> {};
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);

    }

    private static void doWork(String message) throws InterruptedException {
        for (char c : message.toCharArray()) {
            if (c == '.') {
                Thread.sleep(1000);
            }
        }
    }
}
