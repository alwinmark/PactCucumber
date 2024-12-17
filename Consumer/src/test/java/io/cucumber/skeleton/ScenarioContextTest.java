package io.cucumber.skeleton;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ScenarioContextTest {


    @Test
    public void test_putGet_differentTypes() {
        ScenarioContext testee = new ScenarioContext();
        Object value = "A String";
        ScenarioContext.Variable var = new ScenarioContext.Variable("a");

        testee.put(var, value);

        assertThrows(RuntimeException.class, () -> {
            testee.get(var, ScenarioContext.class);
        });
    }

    @Test
    public void test_putGet_null_shouldReturnNull() {
        ScenarioContext testee = new ScenarioContext();
        Object value = null;
        ScenarioContext.Variable var = new ScenarioContext.Variable("a");

        testee.put(var, value);

        assertDoesNotThrow(() -> {
            testee.get(var, ScenarioContext.class);
        });
    }

}