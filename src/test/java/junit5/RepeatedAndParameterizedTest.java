package junit5;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

/**
 * Author: slamine
 */
class RepeatedAndParameterizedTest {

    @DisplayName("MyRepeatedTest")
    @RepeatedTest(value = 5, name = "{displayName}: {currentRepetition} of {totalRepetitions}") //display-name will be printed out on test cases with the name-values defined into brackets
    void test(){//Don't needs annotation @Test

    }

    @RepeatedTest(value = 5)
    void repeatTestWithDI(TestInfo testInfo, RepetitionInfo repetitionInfo, TestReporter testReporter){
        //TestInfo: Provides information about the test name, method, test class, test tags
        //RepetitionInfo: Provides information about the test repetition (it's available only on RepeatedTest or in BeforeEach, *All, etc... )
        //TestReporter: Allows to publish runtime information for test reporting
        System.out.println(testInfo.getDisplayName() + " : " + repetitionInfo.getCurrentRepetition());
        testReporter.publishEntry("Publishing event. Current Repetition="+repetitionInfo.getCurrentRepetition());
    }

    @DisplayName("parameterized through Value Source Test")
    @ParameterizedTest(name = "{displayName} : [{index}] - {argumentsWithNames}")
    @ValueSource(strings = {"simeao", "david", "lamine", "blah blah blah"})
    void arameterizedTest(String val){
        System.out.println(val);//This will run the test multiple time (strings.length)
    }

    @DisplayName("Enum Source Test")
    @ParameterizedTest(name = "{displayName} : [{index}] - {argumentsWithNames}")
    @EnumSource(EnumType.class)
    void parameterizedEnumsTest(EnumType type){
        System.out.println(type);
    }

    enum EnumType{
        TYPE_1, = true
        TYPE_2,
        Type_3
    }

    @DisplayName("CSV Input Test")
    @ParameterizedTest(name = "{displayName} : [{index}] - {argumentsWithNames}")
    @CsvSource({
            "Title1, 1, 2",
            "Title2, 8, 5",
            "Title3, 1, 5",
    })
    void csvInputTest(String title, int val1, int val2){
        System.out.println(title + " = " + val1 + ":" + val2);
    }

    @DisplayName("CSV From File Source Test")
    @ParameterizedTest(name = "{displayName} : [{index}] - {argumentsWithNames}")
    @CsvFileSource(resources = "/input.csv", numLinesToSkip = 1)
    void csvFromFileTest(String title, int val1, int val2){
        System.out.println(title + " = " + val1 + ":" + val2);
    }

    @DisplayName("Method Provider Test")
    @ParameterizedTest(name = "{displayName} : [{index}] - {argumentsWithNames}")
    @MethodSource("getArgs")
    void fromMethodProviderTest(String title, int val1, int val2){
        System.out.println(title + " = " + val1 + ":" + val2);
    }

    static Stream<Arguments> getArgs(){
        return Stream.of(
                Arguments.of("Title1", 1, 2),
                Arguments.of("Title2", 3, 5),
                Arguments.of("Title3", 9, 8));
    }

    @DisplayName("Custom Provider Test")
    @ParameterizedTest(name = "{displayName} : [{index}] - {argumentsWithNames}")
    @ArgumentsSource(CustomArgsProvider.class)
    void customProviderTest(String title, int val1, int val2){
        System.out.println(title + " = " + val1 + ":" + val2);
    }

    class CustomArgsProvider implements ArgumentsProvider{

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of("Title-1", 1, 2),
                    Arguments.of("Title-2", 3, 5),
                    Arguments.of("Title-3", 9, 8));
        }
    }
}
