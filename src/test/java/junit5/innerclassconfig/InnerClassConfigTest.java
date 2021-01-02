package junit5.innerclassconfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringJUnitConfig(classes = {InnerClassConfigTest.TestConfig.class})
class InnerClassConfigTest {

    @Configuration
    static class TestConfig{
        @Bean
        Collection getList(){
            return new ArrayList();
        }
    }

    @Autowired
    private Collection collection;

    @Test
    void test(){
        collection.add("Test");
        given(collection.remove("Test")).willReturn(true);

        assertEquals(true, collection.remove("Test"));

        then(collection).should(times(1)).remove("Test");
        then(collection).shouldHaveNoMoreInteractions();


    }
}
