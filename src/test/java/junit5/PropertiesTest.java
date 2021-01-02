package junit5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@TestPropertySource("classpath:application-test.properties")
@ActiveProfiles("fake-config")
@SpringJUnitConfig(classes = PropertiesTest.TestConfig.class)
class PropertiesTest {

    @ComponentScan("com.springboot2.essentials.springboot2essentials.config")
    @Configuration
    static class TestConfig{

    }

    @Autowired
    Environment env;

    @Test
    void test(){
        env.getProperty("config.name");
    }


}
