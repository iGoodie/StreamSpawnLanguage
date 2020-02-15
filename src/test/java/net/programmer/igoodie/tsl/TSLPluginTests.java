package net.programmer.igoodie.tsl;

import net.programmer.igoodie.tsl.definition.TSLTestEvent;
import net.programmer.igoodie.tsl.exception.TSLPluginError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TSLPluginTests {

    @BeforeAll
    public static void bootstrapTwitchSpawn() {
        TwitchSpawnLanguage.bootstrap();
    }

    @Test
    @DisplayName("should disallow duplicated TSL definitions")
    public void shouldDisallow() {
        TSLTestEvent definition1 = new TSLTestEvent();
        TSLTestEvent definition2 = new TSLTestEvent();
        TSLTestEvent definition3 = new TSLTestEvent();

        Assertions.assertThrows(TSLPluginError.class, () -> {
            TwitchSpawnLanguage.registerEventDefinition(definition1);
            TwitchSpawnLanguage.registerEventDefinition(definition2);
            TwitchSpawnLanguage.registerEventDefinition(definition3);
        });
    }

}
