package com.hdsam.message.routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * ReceiverLogsDirect
 *
 * @author Yeo
 * @date 2023/7/24
 */
public class ReceiverLogsDirect {

    private static final String EXCHANGE_NAME = "hello4";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();

        if (args.length < 1) {
            System.out.println("Usage: ReceiverLogsDirect [info] [warning] [error]");
            System.exit(1);
        }

        for (String severity : args) {
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
        }

        System.out.println(" [*] Waiting for messages. to exit press CTRL + C");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [*] Received '" + message.getEnvelope().getRoutingKey() + "' : '" + msg + "'");
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });

    }
}
