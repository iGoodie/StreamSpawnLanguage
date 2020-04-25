package net.programmer.igoodie.exampleplugin.listener;

import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.node.TSLActionNode;
import net.programmer.igoodie.tsl.runtime.pubsub.TSLListener;

public class ExampleListener extends TSLListener {

    @Override
    public void onAction(TSLContext context, TSLActionNode action) {
        System.out.printf("RECEIVED: %s %s %s\n",
                context.getActionDefinition().getName(),
                context.getActionArguments(),
                action.getTokens());

        if (context.getActionDefinition().parsesNotification()) {
            context.getActionArguments().put("notification",
                    TSLLexer.notificationPart(action.getTokens()));
            context.getActionDefinition()
                    .perform(TSLLexer.actionPart(action.getTokens()), context);

        } else {
            context.getActionDefinition()
                    .perform(action.getTokens(), context);
        }

        System.out.println();
    }

}
