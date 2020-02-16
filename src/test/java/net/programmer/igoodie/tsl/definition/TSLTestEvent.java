package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TSLTestEvent extends TSLEventDefinition {

    public TSLTestEvent() {
        super("Test Event");
    }

    @Override
    public TSLEventArguments getSampleArguments() {
        return new TSLEventArguments()
                .with("actor", "iGoodie")
                .with("viewers", 12345)
                .with("property1", false)
                .with("property2", 12345);
    }

    @Test
    @DisplayName("should accept provided properties")
    public void propertiestTest() {
        TSLTestEvent definition = new TSLTestEvent();
        Assertions.assertTrue(definition.acceptsProperty("property1"));
        Assertions.assertTrue(definition.acceptsProperty("property2"));
        Assertions.assertFalse(definition.acceptsProperty("property3"));
    }

}
