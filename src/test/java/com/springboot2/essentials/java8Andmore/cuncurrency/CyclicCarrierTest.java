package com.springboot2.essentials.java8Andmore.cuncurrency;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import java.util.concurrent.*;

/**
 * Used when we need at some point of the task all parties(thread) wait one to another to complete its tasks
 * before proceed to the next step(s)
 */
public class CyclicCarrierTest {

    @Test
    @Description("Execute the simple math instruction: (432^3) + (3^14) + (45*127/12)")
    public void testCycleCarrier(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        BlockingQueue<Double> queue = new ArrayBlockingQueue<Double>(3);

        Runnable end = () -> {
            System.out.println("Resolving the operation...");
            double total = 0d;
            total += queue.poll();
            total += queue.poll();
            total += queue.poll();
            System.out.println("Result: " + total);

        };

        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, end);


        Runnable r1 = () -> {
            queue.add(432d * 3d);
            await(cyclicBarrier);
            System.out.println("Finished my task");
        };

        Runnable r2 = () -> {
            queue.add(Math.pow(3d,14d));
            await(cyclicBarrier);
            System.out.println("Finished my task");


        };

        Runnable r3 = () -> {
            queue.add((52d*127d/12d));
            await(cyclicBarrier);
            System.out.println("Finished my task");

        };

        executorService.submit(r1);
        executorService.submit(r2);
        executorService.submit(r3);
    }

    @SneakyThrows
    private static void await(CyclicBarrier cyclicBarrier){
        int await = cyclicBarrier.await();
    }

}
