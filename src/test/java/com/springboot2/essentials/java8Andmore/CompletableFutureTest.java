package com.springboot2.essentials.java8Andmore;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.*;

@Slf4j
public class CompletableFutureTest {

    @Test
    @DisplayName("Test Completable Future beginning with completed future already")
    public void testCFutureWithComplete(){
        CompletableFuture<String> cf = CompletableFuture.completedFuture("I born completed");
        try{
            String result = cf.join();//similar to get() but throws Exception
        }catch (CancellationException | CompletionException e){
            log.error(e.getMessage());
        }
    }

    @Test
    @DisplayName("Test CompletableFuture failedFuture")
    public void testFailedFuture(){
        CompletableFuture<String> cf = CompletableFuture.failedFuture(new IllegalArgumentException("Illegal argument exception"));
        try{
            cf.getNow("Value If Absent"); //not blocking call. get value(or throws exception in case of failure) or return default value in case of absent
            cf.obtrudeValue("ObtrudeValue"); //force reset value from get()
            cf.obtrudeException(new InterruptedException("ObtrudeException"));//force throw exception
        }catch (CancellationException | CompletionException e){
            log.error(e.getMessage());
        }
    }

    @Test
    @DisplayName("Completable Future with delay")
    public void testCFutureWithDelay(){
        CompletableFuture<Optional<String>> cf = CompletableFuture.supplyAsync(() -> {
            //Do mething
            return Optional.of("Executed with delay");
        }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));

        Optional<String> result = cf.join();

    }

    @Test
    @DisplayName("Test CompletableFuture completes using Thread")
    public void testComplete(){
        CompletableFuture<String> completableFuture = new CompletableFuture();
        new Thread(() -> {
            try{
                if(new Random().nextInt() %2 == 0){
                    throw new InterruptedException("Odd value");
                }else{
                    completableFuture.complete("Success. Not odd!");//manually complete a Future. If task is already completed, complete is ignored
                }

            }catch (InterruptedException e){
                completableFuture.completeExceptionally(e);
            }
        }).start();
        completableFuture.exceptionally(ex -> {
            System.out.println(ex.getMessage());
            return "Default value on exception. " + ex.getCause();
        });
        completableFuture.thenAccept(System.out::println);
        completableFuture.handle((ctx, ex) -> {
            if(ex != null){
                System.out.println("Complete with error. " + ex);
                return "Completed";
            }else{
                System.out.println("Complete with success. result="+ctx);
            }
            return "Default";
        });
    }

    //RunAsync need 2 completableFeature to be created
    @Test
    @DisplayName("Test runAsync method")
    public void testRunAsync() {
        CompletableFuture<String> c1 = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                String value = CompletableFutureTest.dummyDbCall();
                c1.complete(value);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                c1.completeExceptionally(e);
            }
        });
        c1.exceptionally(d -> "ERROR" + d.getCause())
                .thenAccept(System.out::println);

    }

    @Test
    @DisplayName("Test supplyAsync method")
    public void testSupplyAsync(){
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(CompletableFutureTest::dummyDbCall);
        completableFuture.exceptionally(ex -> "Default value in case of error. " + ex.getCause())
                .thenApply(r -> r.toLowerCase())
                .thenAccept(System.out::println);
    }

    private static String dummyDbCall(){
        try {
            Thread.sleep(1_000);
            return "OK";
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            return "NOK";
        }
    }

    @Test
    @DisplayName("acceptEither, finish the execution when one of the future is completed firstly.")
    public void testAcceptEither(){
        var cf1 = CompletableFuture.supplyAsync(() -> {
            return "CF 1";
        }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));

        var cf2 = CompletableFuture.supplyAsync(() -> {
            return "CF 1";
        }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));
        
        var either = cf1
                .acceptEitherAsync(cf2, log::info, CompletableFuture.delayedExecutor(6, TimeUnit.SECONDS));

        Void join = either.join(); //join will block until one of the CompletableFuture finish (doesn't matter if it was cf1 or cf2).
    }

    @Test
    @DisplayName("testAllOff. This function finish only when all CompletableFuture complete, otherwise, will still running forever")
    public void testAllOf(){
        var cf1 = new CompletableFuture<String>();
        var cf2 = new CompletableFuture<String>();
        var cf3 = new CompletableFuture<String>();
        var cf4 = new CompletableFuture<String>();

        var allOf = CompletableFuture.allOf(cf1, cf2, cf3, cf4)
                .thenRunAsync(() -> log.debug("thenRunAsync"))
                .thenRunAsync(() -> {
                    //Do something
                });
        cf1.complete("Cf1");
        log.info("CF1 done !");
        Void result = allOf.join(); //this join will not be finished until cf2, cf3 and cf4 completes
        log.info("Finish");


    }

    @Test
    @DisplayName("testAnyOf. This function finish when at least one CompletableFuture completes")
    public void testAnyOf(){
        var cf1 = CompletableFuture.completedFuture("cf1");
        var cf2 = CompletableFuture.completedFuture("cf2");
        var cf3 = CompletableFuture.completedFuture("cf3");
        var cf4 = CompletableFuture.completedFuture("cf4");

        var anyOf = CompletableFuture.anyOf(cf1, cf2, cf3, cf4)
                .thenRunAsync(() -> log.debug("thenRunAsync"))
                .thenRunAsync(() -> {
                    //Do something
                });
        cf1.complete("Cf1");
        log.info("CF1 done !");
        Void result = anyOf.join();
        log.info("Finish");


    }
}
