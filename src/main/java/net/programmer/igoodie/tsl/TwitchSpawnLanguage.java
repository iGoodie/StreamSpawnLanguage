package net.programmer.igoodie.tsl;

import net.programmer.igoodie.tsl.definition.TSLActionDefinition;
import net.programmer.igoodie.tsl.definition.TSLEventDefinition;
import net.programmer.igoodie.tsl.definition.TSLPredicateDefinition;
import net.programmer.igoodie.tsl.exception.TSLPluginError;
import net.programmer.igoodie.tsl.meta.action.TSLNothingAction;
import net.programmer.igoodie.tsl.meta.predicate.TSLWithPredicate;
import net.programmer.igoodie.tsl.util.log.TSLLogLevel;
import net.programmer.igoodie.tsl.util.log.TSLLogger;

import java.util.HashMap;
import java.util.Map;

public class TwitchSpawnLanguage {

    public static TSLLogger LOGGER = new TSLLogger().setLogLevel(TSLLogLevel.ALL);

    private static Map<String, TSLEventDefinition> events; // upper(name) -> #TSLEventDefinition
    private static Map<String, TSLPredicateDefinition> predicates; // upper(name) -> #TSLPredicateDefinition
    private static Map<String, TSLActionDefinition> actions; // upper(name) -> #TSLActionDefinition

    public static void bootstrap() {
        events = new HashMap<>();
        predicates = new HashMap<>();
        actions = new HashMap<>();

        try {
            LOGGER.info("Bootstrapping TwitchSpawn Language...");
            registerPredicateDefinition(new TSLWithPredicate());
            registerActionDefinition(new TSLNothingAction());

        } catch (TSLPluginError ignored) {
            throw new InternalError(); // <-- This shall never be thrown

        } finally {
            LOGGER.info("TwitchSpawn Language bootstrapped successfully!");
        }
    }

    /* ----------------------------- ASSERTIONS */

    private static void assertBootstrapped() {
        if (events == null)
            throw new IllegalStateException("TSL is not bootstrapped yet");
    }

    /* ----------------------------- REGISTRY */

    public static void registerEventDefinition(TSLEventDefinition event) throws TSLPluginError {
        assertBootstrapped();

        if (events.containsKey(event.getName().toUpperCase()))
            throw new TSLPluginError("Unable to create TSL Event Definition as it was already declared");

        events.put(event.getName().toUpperCase(), event);
        TwitchSpawnLanguage.LOGGER.info("Registered event definition -> %s", event.getName());
    }

    public static void registerPredicateDefinition(TSLPredicateDefinition predicate) throws TSLPluginError {
        assertBootstrapped();

        if (predicates.containsKey(predicate.getName().toUpperCase()))
            throw new TSLPluginError("Unable to create TSL Predicate Definition as it was already declared");

        predicates.put(predicate.getName().toUpperCase(), predicate);
        TwitchSpawnLanguage.LOGGER.info("Registered predicate definition -> %s", predicate.getName());
    }

    public static void registerActionDefinition(TSLActionDefinition action) throws TSLPluginError {
        assertBootstrapped();

        if (actions.containsKey(action.getName().toUpperCase()))
            throw new TSLPluginError("Unable to create TSL Action Definition as it was already declared");

        actions.put(action.getName().toUpperCase(), action);
        TwitchSpawnLanguage.LOGGER.info("Registered action definition -> %s", action.getName());
    }

    /* ----------------------------- ACCESSORS */

    public static TSLEventDefinition getEventDefinition(String name) {
        assertBootstrapped();
        return events.get(name.toUpperCase());
    }

    public static TSLPredicateDefinition getPredicateDefinition(String name) {
        assertBootstrapped();
        return predicates.get(name.toUpperCase());
    }

    public static TSLActionDefinition getActionDefinition(String name) {
        assertBootstrapped();
        return actions.get(name.toUpperCase());
    }

}
