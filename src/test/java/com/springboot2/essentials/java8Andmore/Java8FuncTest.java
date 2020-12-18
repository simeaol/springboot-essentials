package com.springboot2.essentials.java8Andmore;

import com.springboot2.essentials.springboot2essentials.domain.Anime;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

@SpringBootTest
public class Java8FuncTest {

    static Function<Integer, Integer> sum = v1 -> v1 + 1;
    static Function<Integer, Integer> zero = v -> v * 2;
    static BiFunction<Integer, Integer, Integer> multiply = (a, b) -> a * b;

    List<List<String>> list= new ArrayList<>();

    @Test
    public void testSum(){

        Integer apply = sum.andThen(zero).apply(5);
        System.out.println(apply);

    }

    @Test
    public void testOrElse(){
        Optional<String> string = Optional.of("FOO");
        string
                .map(this::runIfExist)
                .orElse(runIfNotExist());//orElse will also be executed (to avoid this behaviour use orElseGet instead). As orElse will be executed even when the map does not match the requirement, it is used to set default value(s)
                //orElseGet(() => this::runIfNotExist) //Use the alternatives flow
    }

    private String runIfExist(String s) {
        System.out.println("Only Run if Exist");
        return s;
    }

    private String runIfNotExist() {
        System.out.println("Only Run if Exist");
        return "EMPTY";
    }

    @Test
    public void testWrapperFunc(){
        List.of(Integer.valueOf(null), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3))
                .stream()
                .map(wrap(e -> doSomething(e)))//stream is stopped at all whenever RuntimeException is thrown
                .forEach(System.out::println);

    }

    private Integer doSomething(Integer value){
        return value;
    }

    private static <T, R> Function<T, R> wrap(CheckFunc<T, R>  checkFunc){
        return t ->  {
            try{
                return checkFunc.apply(t);
            }catch (Exception e){
                throw new RuntimeException(e);
            }

        };
    }

    public interface CheckFunc<T, R>{
        R apply (T t) throws RuntimeException;
    }

    @Test
    public void readFile() throws IOException {
        var path = Paths.get("application.yml");
        var file = Files.readString(path, Charset.defaultCharset());

    }

    @Test
    public void testHttpClient() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(1))
                .version(HttpClient.Version.HTTP_2)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://google.com?search=hi"))
                .build();

        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //response.

        CompletableFuture<String> call = client
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    //switch statement Supported only in Java12
    @Test
    public void testSwitchCaseJava12(){
        int i = 0;
        String result = switch (i){
                case 0 -> break "Zero";
                case 1, 2, 3 -> break "More than 0 and less than 4";
            };
    }

    //break was replaced by yield on Java13. yield is not keyword in jav. It's just used in switch statement. this means you don't need to write them in explicit.
    @Test
    public void testSwitchCaseJava13(){
        int i = 0;
        String result = switch (i){
            case 0 -> yield "Zero"; //in java13 or higher: case 0 -> "Zero";
            case 1, 2, 3 -> yield "More than 0 and less than 4";
        };
    }

    @Test
    public void testTextBlockJava13(){
        String html = """
                      <html>
                        <head>
                        </head>
                        <body>
                            <p> Hello world! </p>
                        </body>
                      </html>
                    """;

        String json = """
                      {
                        "name": "Simeao",
                        "race": "black",
                        "knowJava" : true
                      """
    }

    //Java14 pattern matching
    public void testPatternMatching(){
        Object obj = new Anime();

        if(obj instanceof Anime anime){
            System.out.println(anime.toString());
        }

        List.of(1,2,3);//give immutable list
        Arrays.asList(1,2,3);//give fixed sized array. you cannot add element not because it is immutable but because it has fixed size
    }

    //java 14-15
    public record Client(String name, String email){//record remove boilerplate and provide by default all getters and setters, hashcode, equals, toString. so you don't need to implement them

    }

}
