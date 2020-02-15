package net.programmer.igoodie.tsl;

import net.programmer.igoodie.tsl.definition.TSLEventDefinition;
import net.programmer.igoodie.tsl.exception.TSLPluginError;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class TwitchSpawnLanguage {

    private static Map<String, TSLEventDefinition> events; // lower(name) -> #TSLEvent

    public static void bootstrap() {
        events = new HashMap<>();
    }

    public static void registerEventDefinition(TSLEventDefinition event) throws TSLPluginError {
        if (events == null)
            throw new IllegalStateException("TSL is not bootstrapped yet");

        if (events.containsKey(event.getName().toLowerCase()))
            throw new TSLPluginError("Unable to create TSL Event Definition as it was already declared");

        events.put(event.getName().toLowerCase(), event);
    }

    /* ----------------------------- */

    public static TSLEventDefinition getEventDefinition(String name) {
        return events.get(name.toLowerCase());
    }

}
