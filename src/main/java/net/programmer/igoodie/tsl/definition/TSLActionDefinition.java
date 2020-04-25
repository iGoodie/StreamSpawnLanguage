package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.runtime.context.TSLActionArguments;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;

public abstract class TSLActionDefinition extends TSLDefinition {

    private boolean parseNotification;

    public TSLActionDefinition(String name) {
        this(name, true);
    }

    public TSLActionDefinition(String name, boolean parseNotification) {
        super(name);
        this.parseNotification = parseNotification;
    }

    public boolean parsesNotification() {
        return parseNotification;
    }

    /* ----------------------------------------- */

    /**
     * Performs the action with given action parameters and context
     *
     * @param tokens  Action parameters as TSL tokens
     * @param context Actual execution context
     * @return Whether performing was successful or not
     * 0
     */
    public abstract boolean perform(List<TSLToken> tokens, TSLContext context);

    @Override
    public final boolean satisfies(List<TSLToken> tokens, TSLContext context) {
        return true;
    }

    @Override
    public TSLActionArguments getShape() {
        return new TSLActionArguments(); // By default, no argument is serialized
    }

    /* ----------------------------------------- */

}
