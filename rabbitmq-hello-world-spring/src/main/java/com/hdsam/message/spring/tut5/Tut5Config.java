package com.hdsam.message.spring.tut5;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Tut5Config
 *
 * @author Yeo
 * @date 2023/7/24
 */
@Profile({"tut5", "topics"})
@Configuration
public class Tut5Config {

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("tut.topic");
    }

    @Profile("receiver")
    public static class ReceiverConfig{

        @Bean
        public Tut5Receiver receiver() {
            return new Tut5Receiver();
        }

        @Bean
        public Queue autoDeleteQueue1() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue2() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding1a(TopicExchange topicExchange, Queue autoDeleteQueue1) {
            return BindingBuilder.bind(autoDeleteQueue1).to(topicExchange).with("*.orange.*");
        }

        @Bean
        public Binding binding1b(TopicExchange topicExchange, Queue autoDeleteQueue1) {
            return BindingBuilder.bind(autoDeleteQueue1).to(topicExchange).with("*.*.rabbit");
        }

        @Bean
        public Binding binding2a(TopicExchange topicExchange, Queue autoDeleteQueue2){
            return BindingBuilder.bind(autoDeleteQueue2).to(topicExchange).with("lazy.#");
        }
    }

    @Profile("sender")
    @Bean
    public Tut5Sender sender() {
        return new Tut5Sender();
    }

}
