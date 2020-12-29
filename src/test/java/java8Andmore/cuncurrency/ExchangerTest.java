package java8Andmore.cuncurrency;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Similar to SynchronousQueue but there is msg exchanger between those involved threads
 */
@Slf4j
public class ExchangerTest {
    static ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    static Exchanger<String> EXCHANGER = new Exchanger<>();

    public static void main(String[] args) {
        Runnable r1 = () -> {
            String msg = "Hey, how is it going ?";
            String threadName = Thread.currentThread().getName();
           log.info("{} saying: {}", threadName,msg);
            try {
                String response = EXCHANGER.exchange(msg);
                log.info("{} Received: {}", threadName, response);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        };

        Runnable r2 = () -> {
            String msg = "Hey man, doing well. What about you ?";
            String threadName = Thread.currentThread().getName();
            log.info("{} saying: {}", threadName, msg);
            try {
                String response = EXCHANGER.exchange(msg);
                log.info("{} Received: {}", threadName, response);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        };

        EXECUTOR.execute(r1);
        EXECUTOR.execute(r2);

        EXECUTOR.shutdown();
    }
}
