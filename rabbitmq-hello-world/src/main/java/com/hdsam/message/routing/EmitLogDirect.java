package com.hdsam.message.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * EmitLogDirect
 *
 * @author Yeo
 * @date 2023/7/24
 */
public class EmitLogDirect {

    private final static String EXCHANGE_NAME = "hello4";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            String severity = getSeverity(args);
            String message = getMessage(args);

            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [*] Sent '" + severity + "' : '" + message + "'");
        }

    }


    private static String getSeverity(String[] args) {
        return args.length > 1 ? args[0] : "info";
    }

    private static String getMessage(String[] args) {
        return args.length > 1 ? args[1] : "";
    }
}
