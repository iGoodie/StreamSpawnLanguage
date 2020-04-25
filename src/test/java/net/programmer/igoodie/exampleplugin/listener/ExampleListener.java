package net.programmer.igoodie.exampleplugin.listener;

import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.node.TSLActionNode;
import net.programmer.igoodie.tsl.runtime.pubsub.TSLListener;

public class ExampleListener extends TSLListener {

    @Override
    public void onAction(TSLContext context, TSLActionNode action) {
        System.out.printf("RECEIVED: %s %s\n",
                context.getActionDefinition().getName(),
                context.getActionArguments());

        context.getActionDefinition()
                .perform(action.getTokens(), context);

        System.out.println();
    }

}
