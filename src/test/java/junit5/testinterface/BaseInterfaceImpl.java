package junit5.testinterface;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BaseInterfaceImpl implements BaseInterface{//This class will inherit the tag defined in interface

    @Test
    void test(){
        Assertions.fail("I will fail");
    }
}
