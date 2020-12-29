package java8Andmore.cuncurrency;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

@Log4j2
public class ReentrantLockTest {
    private static int count;

    private static ReentrantLock lock = new ReentrantLock();
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {

        //lock can be required how many time as needed and the unlock must be called this number of times to be unlocked
        //.e.g: if a get lock twice: lock.lock(); lock.lock() will be need to get unlock twice: lock.unlock(); lock.unlock()
        Runnable r1 = () -> {
            String thread = Thread.currentThread().getName();
            log.info("{} getting lock", thread);
            lock.lock();
            log.info("{}, increment to {}", thread, count++);
            log.info("{} unlock", thread);
            lock.unlock();

        };

        for (int i=0; i <= 5; i++){
            executorService.execute(r1);
        }

        executorService.shutdown();

    }
}
