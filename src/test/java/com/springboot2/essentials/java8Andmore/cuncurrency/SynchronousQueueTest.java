package com.springboot2.essentials.java8Andmore.cuncurrency;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SynchronousQueueTest {

    static ExecutorService executorService = Executors.newCachedThreadPool();
    static SynchronousQueue<String> synchronousQueue = new SynchronousQueue<>();

    public static void main(String[] args) {
        Runnable producer = () ->{
            String threadName = Thread.currentThread().getName();
            log.info("{} adding data to queue", threadName);
            put();
        };

        Runnable consumer = () -> {
            String threadName = Thread.currentThread().getName();
            log.info("{} consuming data from queue", threadName);
            take();
        };
        

        executorService.execute(producer);
        executorService.execute(consumer);
        executorService.shutdown();
    }

    private static void take() {
        try {
            synchronousQueue.take();//read operation is executed only when there are producer writing data into SynchronousQueue, otherwise, program stuck here!
            //synchronousQueue.poll(5, TimeUnit.SECONDS);//wait for 5 second and return null if none data is offer
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void put() {
        try {
            synchronousQueue.put("Added some data");//write operation is executed only when there are consumer is available and waiting, otherwise the program stuck here!
            //synchronousQueue.offer("Added some data", 5, TimeUnit.SECONDS);//try to offer data in specified time, otherwise, skip offer
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
