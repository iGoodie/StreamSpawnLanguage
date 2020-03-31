package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.TwitchSpawnLanguage;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.runtime.context.TSLActionArguments;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;

public abstract class TSLActionDefinition extends TSLDefinition {

    public TSLActionDefinition(String name) {
        super(name);
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
        List<TSLToken> notificationPart = TSLLexer.notificationPart(tokens);
        if (notificationPart != null) {
            TwitchSpawnLanguage.LOGGER.trace("Displaying notification -> %s", notificationPart);
            context.getActionArguments().put("notification", notificationPart);
        }

        // A TSL Action always satisfies the context once it's performed
        List<TSLToken> actionPart = TSLLexer.actionPart(tokens);
        TwitchSpawnLanguage.LOGGER.trace("Performing action -> %s", actionPart);
        return perform(actionPart, context);
    }

    @Override
    public TSLActionArguments getSampleArguments() {
        return new TSLActionArguments(); // By default, no argument is serialized
    }

    /* ----------------------------------------- */

}
