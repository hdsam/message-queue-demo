package com.hdsam.message.spring.others.ttl;

import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;

import java.util.Date;

/**
 * TtlReceiver
 *
 * @author Yeo
 * @date 2023/7/26
 */
public class TtlReceiver {


    @RabbitListener(queues = "#{ttlQueue.name}")
    public void receive1(String in) throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        System.out.println(getDate() + " instance deadLetter Queue consumer  [x] Received '" + in + "' at ");
        doWork(in);
        watch.stop();
        System.out.println(getDate() + " instance deadLetter Queue consumer  [x] Received '" + in + "' at ");
    }

    @RabbitListener(queues = "#{deadLetterQueue.name}")
    public void receive2(String in) throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        System.out.println(getDate() + " instance deadLetter Queue consumer  [x] Received '" + in + "' at ");
        doWork(in);
        watch.stop();
        System.out.println(getDate() + " instance deadLetter Queue consumer  [x] Done '" + in + "' at ");
    }

    private static String getDate() {
        return DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
    }


    private void doWork(String in) throws InterruptedException {
        for (char ch : in.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }
}
