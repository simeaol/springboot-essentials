package junit5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

@DisplayName("Test Conditional feature available on JUnit5")
public class ConditionalTest {

    @EnabledOnJre(JRE.JAVA_13) //https://junit.org/junit5/docs/current/user-guide/#writing-tests-conditional-execution
    @EnabledOnOs(OS.LINUX)
    @DisabledOnOs(OS.WINDOWS)
    @EnabledIfEnvironmentVariable(named = "USER", matches = "slamine")
    @Test
    void testJava13LinuxOSForUserSlamine(){
        Assertions.assertEquals(10L, 10);
    }
}
