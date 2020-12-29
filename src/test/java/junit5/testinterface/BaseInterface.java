package junit5.testinterface;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;

@Tag("interface test-case")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public interface BaseInterface {

    @BeforeAll
    default void beforeAll(){ //must be static unless the test class is annotated with @TestInstance(Lifecycle.PER_*), otherwise, tests which implements this interface will fail
        System.out.println("beforeAll from BaseInterface");
    }
}
