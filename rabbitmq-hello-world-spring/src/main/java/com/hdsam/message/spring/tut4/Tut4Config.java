package com.hdsam.message.spring.tut4;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Tut4Config
 *
 * @author Yeo
 * @date 2023/7/24
 */
@Profile({"tut4", "routing"})
@Configuration
public class Tut4Config {

    @Bean
    public DirectExchange direct() {

        return new DirectExchange("tut.direct");
    }

    @Profile("receiver")
    private static class ReceiverConfig{

        @Bean
        public Queue autoDeleteQueue1(){
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue2(){
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding1a(DirectExchange directExchange, Queue autoDeleteQueue1){
            return BindingBuilder.bind(autoDeleteQueue1).to(directExchange).with("orange");
        }


        @Bean
        public Binding binding1b(DirectExchange directExchange, Queue autoDeleteQueue1){
            return BindingBuilder.bind(autoDeleteQueue1).to(directExchange).with("black");
        }

        @Bean
        public Binding binding2a(DirectExchange directExchange, Queue autoDeleteQueue2){
            return BindingBuilder.bind(autoDeleteQueue2).to(directExchange).with("green");
        }


        @Bean
        public Binding binding2b(DirectExchange directExchange, Queue autoDeleteQueue2){
            return BindingBuilder.bind(autoDeleteQueue2).to(directExchange).with("black");
        }

        @Bean
        public Tut4Receiver receiver(){
            return new Tut4Receiver();
        }
    }

    @Profile("sender")
    @Bean
    public Tut4Sender sender(){
        return new Tut4Sender();
    }

}
