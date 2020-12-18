package com.springboot2.essentials.java8Andmore.cuncurrency;

import lombok.SneakyThrows;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Used to control number of time to execute a single task before moving to another one.
 * CountDownLatch is not reusable, with mean, before reaching the number of count configured
 * it will not possible to restart it again
 */
public class CountDownLatchTest {

    static volatile int i = 0;

    @SneakyThrows
    public static void main(String[] args){


        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        CountDownLatch countDownLatch = new CountDownLatch(3);

        Runnable t1 = () -> {
            int j = new Random().nextInt();
            int x = i * j;
            System.out.println(i + "*" + j + " = " + x);
            countDownLatch.countDown();//when count down is executed the number of time configured, it not take affect anymore
        };

        /**
         * scheduleAtFixedRate -> schedule a task to be executed at fixed rate but if the task take more than the configured
         * time the scheduler will not launch new task, which means that the new task will be executed only when the previous
         * finish.
         *
         * scheduleWithFixedDelay -> opposite of scheduleAtFixedRate, this will launch new task everytime the delay is reached
         */
        scheduledExecutorService.scheduleAtFixedRate(t1, 0, 1, TimeUnit.SECONDS);
        //scheduledExecutorService.scheduleWithFixedDelay(t1, 0, 1, TimeUnit.SECONDS);


        while(true){
            countDownLatch.wait(); // "Thread main" will waiting in this line until count down reach zero, then the next instruction will be executed. when reached zero this wait will not take affect anymore
            i = new Random().nextInt();
            //countDownLatch = new CountDownLatch(3);//as countdown is un-reusable to use it again you need to create new instance of it
        }

    }
}
