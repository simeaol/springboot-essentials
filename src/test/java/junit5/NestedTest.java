package junit5;

import org.junit.jupiter.api.*;

@DisplayName("Nested Test")
class NestedTest {

    String service;

    @BeforeEach
    void setUp(){
        service = "Service 1";
        System.out.println("outer Before Each");
    }

    @Test
    @DisplayName("outer testEquals test-case")
    void testEquals(){
        Assertions.assertEquals("Service 1", service);
    }

    @Nested
    @DisplayName("Nested inner class")
    class NestedClass{

        String service2;

        @BeforeEach
        void setUp(){ //Nested Before each will be executed after the outer
            service2 = "Service 2";
            System.out.println("Nested Before Each");
        }

        @Test
        @DisplayName("Inner testEquals test-case")
        void testEquals(){
            Assertions.assertNotEquals(service, service2);
            Assertions.assertEquals("Service 2", service2);
        }

    }
}
