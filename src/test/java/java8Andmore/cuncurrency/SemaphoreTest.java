package java8Andmore.cuncurrency;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Semaphore consists basically in an structural data structure where elements need to take acquire before
 * performing any task, otherwise, they will be add on queue while waiting for release on semaphore which is
 * performed only one thread finishes it task
 */
@Slf4j
public class SemaphoreTest {

    private static final AtomicInteger count = new AtomicInteger();
    private static final Semaphore SEMAPHORE = new Semaphore(3);
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(100);

    @SneakyThrows
    public static void main(String[] args){

        Runnable r1 = () -> {
            try {
                count.incrementAndGet();
                String name = Thread.currentThread().getName();
                log.info("{} trying to get acquire.", name);
                SEMAPHORE.acquire();
                log.info("{} acquired. Computing something ...", name);
                sleep();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                SEMAPHORE.release();
                count.decrementAndGet();
            }
        };

        Runnable r2 = () -> {
            log.info("Total of element waiting to get acquire = {}", count.get());
        };

        scheduledExecutorService.scheduleWithFixedDelay(r2, 0, 1, TimeUnit.SECONDS);


        for(int i=0;  i < 100; i++){
            scheduledExecutorService.execute(r1);
        }

        scheduledExecutorService.shutdown();

        for(;;){//=while(true)

        }

    }

    private static void sleep() {
        try {
           // SEMAPHORE.wait();
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    public boolean testWhile(){
        for(boolean isTrue=false;;){
            if(new Random().nextInt() %2 == 0){
                isTrue = true;
            }
        }
    }
}
