package net.programmer.igoodie.tsl;

import org.junit.jupiter.api.Test;

import javax.script.*;

public class TSLFlowTests {

    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    @Test
    public void foo() {
        try {
            Bindings bindings = new SimpleBindings();
            bindings.put("night", "night");
            bindings.put("ingameTime", "night");

            Object eval = engine.eval("", bindings);

            System.out.println(eval.getClass().getSimpleName());

        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

}
