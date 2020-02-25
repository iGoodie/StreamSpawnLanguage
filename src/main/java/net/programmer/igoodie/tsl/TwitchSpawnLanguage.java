package net.programmer.igoodie.tsl;

import net.programmer.igoodie.tsl.definition.TSLEventDefinition;
import net.programmer.igoodie.tsl.definition.TSLPredicateDefinition;
import net.programmer.igoodie.tsl.exception.TSLPluginError;
import net.programmer.igoodie.tsl.meta.predicate.TSLWithPredicate;
import net.programmer.igoodie.tsl.util.log.TSLLogLevel;
import net.programmer.igoodie.tsl.util.log.TSLLogger;

import java.util.HashMap;
import java.util.Map;

public class TwitchSpawnLanguage {

    public static TSLLogger LOGGER = new TSLLogger().setLogLevel(TSLLogLevel.ALL);

    private static Map<String, TSLEventDefinition> events; // lower(name) -> #TSLEventDefinition
    private static Map<String, TSLPredicateDefinition> predicates; // lower(name) -> #TSLPredicateDefinition

    public static void bootstrap() {
        events = new HashMap<>();
        predicates = new HashMap<>();

        try {
            registerPredicateDefinition(new TSLWithPredicate());

        } catch (TSLPluginError ignored) {
            throw new InternalError();
        } finally {
            LOGGER.info("TwitchSpawn bootstrapped successfully!");
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

        if (events.containsKey(event.getName().toLowerCase()))
            throw new TSLPluginError("Unable to create TSL Event Definition as it was already declared");

        events.put(event.getName().toLowerCase(), event);
    }

    public static void registerPredicateDefinition(TSLPredicateDefinition predicate) throws TSLPluginError {
        assertBootstrapped();

        if (predicates.containsKey(predicate.getName().toLowerCase()))
            throw new TSLPluginError("Unable to create TSL Predicate Definition as it was already declared");

        predicates.put(predicate.getName().toLowerCase(), predicate);
    }

    /* ----------------------------- ACCESSORS */

    public static TSLEventDefinition getEventDefinition(String name) {
        assertBootstrapped();
        return events.get(name.toLowerCase());
    }

    public static TSLPredicateDefinition getPredicateDefinition(String name) {
        assertBootstrapped();
        return predicates.get(name.toLowerCase());
    }

}
