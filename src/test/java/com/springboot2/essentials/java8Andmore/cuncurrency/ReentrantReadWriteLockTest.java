package com.springboot2.essentials.java8Andmore.cuncurrency;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Log4j2
public class ReentrantReadWriteLockTest {

    private static int count;

    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {

        //writeLock locks all threads to use the resource (doesn't matter if it is read or write lock they will be locked)
        Runnable r1 = () -> {
            String thread = Thread.currentThread().getName();
            log.info("{} getting write lock", thread);
            Lock writeLock = lock.writeLock();
            log.info("{}, increment to {}", thread, count++);
            log.info("{} writeLock unlock", thread);
            writeLock.unlock();

        };

        //readLock does not locks threads from reading resource as they will not change the current status
        Runnable r2 = () -> {
            String thread = Thread.currentThread().getName();
            log.info("{} getting read lock", thread);
            Lock writeLock = lock.readLock();
            log.info("{}, reading count ={}", thread, count);
            log.info("{}, read count ={}", thread, count);
            log.info("{} readLock unlock", thread);
            writeLock.unlock();

        };

        for (int i=0; i <= 5; i++){
            executorService.execute(r1);
            executorService.execute(r2);
        }

    }
}
