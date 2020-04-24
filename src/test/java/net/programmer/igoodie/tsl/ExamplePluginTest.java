package net.programmer.igoodie.tsl;

import net.programmer.igoodie.exampleplugin.ExamplePlugin;
import net.programmer.igoodie.exampleplugin.listener.ExampleListener;
import net.programmer.igoodie.tsl.exception.TSLError;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;
import net.programmer.igoodie.tsl.util.Resources;
import net.programmer.igoodie.tsl.util.log.TSLLogLevel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExamplePluginTest {

    @BeforeAll
    public static void initializeExamplePlugin() {
        StreamSpawnLanguage.bootstrap();
        ExamplePlugin.initialize();
        StreamSpawnLanguage.LOGGER.setLogLevel(TSLLogLevel.DEBUG);
    }

    @Test
    @DisplayName("should initialize and run Example plugin")
    public void runPlugin() throws TSLError {
        String tsl = Resources.readTSL("rules.exampleplugin.tsl");

        TSLParser parser = new TSLParser(tsl);
        TSLRuleset ruleset = parser.parse();

        ruleset.subscribe(new ExampleListener());

        TSLEventArguments eventArguments = new TSLEventArguments()
                .with("numberArgument1", 1)
                .with("numberArgument2", 2);

        ruleset.dispatch(StreamSpawnLanguage.getEventDefinition("Example Event"), eventArguments);
    }

}
