package net.programmer.igoodie.tsl;

import net.programmer.igoodie.tsl.definition.TSLPrintAction;
import net.programmer.igoodie.tsl.definition.TSLTestEvent;
import net.programmer.igoodie.tsl.exception.TSLError;
import net.programmer.igoodie.tsl.exception.TSLPluginError;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;
import net.programmer.igoodie.tsl.util.Resources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.script.*;

public class TSLFlowTests {

    @BeforeAll
    public static void bootstrap() {
        StreamSpawnLanguage.bootstrap();

        try {
            StreamSpawnLanguage.registerEventDefinition(TSLTestEvent.INSTANCE);
            StreamSpawnLanguage.registerActionDefinition(TSLPrintAction.INSTANCE);

        } catch (TSLPluginError tslPluginError) {
            tslPluginError.printStackTrace();
        }
    }

    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

//    @Test
//    public void foo() {
//        try {
//            Bindings bindings = new SimpleBindings();
//            bindings.put("night", "night");
//            bindings.put("ingameTime", "night");
//
//            Object eval = engine.eval("", bindings);
//
//            System.out.println(eval.getClass().getSimpleName());
//
//        } catch (ScriptException e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    @DisplayName("should traverse all the way to the action node")
    public void flowTest() throws TSLError {
        String tsl = Resources.readTSL("rules.test1.tsl");
        TSLRuleset ruleset = new TSLParser(tsl).parse();

        TSLEventArguments eventArguments = new TSLEventArguments();
        eventArguments.put("property1", false);
        eventArguments.put("property2", 20);

        ruleset.dispatch(TSLTestEvent.INSTANCE, eventArguments);
    }

}
