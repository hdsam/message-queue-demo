package com.hdsam.message.spring.others.ttl;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.TimeUnit;

/**
 * ExpiredQueueConfig
 *
 * @author Yeo
 * @date 2023/7/25
 */
@Profile({"ttl"})
@Configuration
public class TtlConfig {


    private static final long MESSAGE_TTL_SECONDS = 60L;

    static class TtlQueueConfig{
        @Bean
        public DirectExchange ttlExchange() {
            return new DirectExchange("ttl.direct");
        }


        @Bean
        public Queue ttlQueue() {
            return QueueBuilder.durable("ttl.queue")
                    .ttl((int) TimeUnit.SECONDS.toMillis(MESSAGE_TTL_SECONDS))
                    .deadLetterExchange("ttl.dl-direct")
                    .deadLetterRoutingKey("drk")
                    .build();
        }

        @Bean
        public Binding ttlQueueBinding(@Qualifier("ttlQueue") Queue ttlQueue,
                                       @Qualifier("ttlExchange") DirectExchange ttlExchange) {
            return BindingBuilder.bind(ttlQueue).to(ttlExchange).with("");
        }
    }

    static class DeadLetterMessageConfig {

        @Bean
        public DirectExchange deadLetterExchange() {
            return new DirectExchange("ttl.dl-direct");
        }

        @Bean
        public Queue deadLetterQueue() {
            return QueueBuilder.durable("ttl.dl-queue").build();

        }

        @Bean
        public Binding deadLetterQueueBinding(@Qualifier("deadLetterExchange") DirectExchange deadLetterExchange,
                                              @Qualifier("deadLetterQueue") Queue deadLetterQueue) {
            return BindingBuilder
                    .bind(deadLetterQueue)
                    .to(deadLetterExchange)
                    .with("drk");
        }

    }

    @Profile("sender")
    @Bean
    public TtlSender sender() {
        return new TtlSender();
    }



    @Profile("receiver")
    @Bean
    public TtlReceiver receiver(){
        return new TtlReceiver();
    }


}
