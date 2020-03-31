package net.programmer.igoodie.exampleplugin;

import net.programmer.igoodie.exampleplugin.actions.ExamplePrintAction;
import net.programmer.igoodie.exampleplugin.decorators.MyDecorator;
import net.programmer.igoodie.exampleplugin.decorators.MyParameterDecorator;
import net.programmer.igoodie.exampleplugin.events.ExampleEvent;
import net.programmer.igoodie.exampleplugin.lambda.Number2ToNumberFunction;
import net.programmer.igoodie.exampleplugin.lambda.Number3ToNumberFunction;
import net.programmer.igoodie.exampleplugin.lambda.NumberNToNumberFunction;
import net.programmer.igoodie.exampleplugin.predicates.ExampleIfPredicate;
import net.programmer.igoodie.tsl.TwitchSpawnLanguage;
import net.programmer.igoodie.tsl.exception.TSLPluginError;
import net.programmer.igoodie.tsl.runtime.context.TSLExpressionBindings;

import java.util.stream.Stream;

public class ExamplePlugin {

    public static void initialize() {
        try {
            registerDecorators();
            registerEvents();
            registerPredicates();
            registerActions();
            registerExpressionBindingProviders();

        } catch (TSLPluginError e) {
            System.out.println("Dun-dun-duuuun! :c");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void registerDecorators() throws TSLPluginError {
        TwitchSpawnLanguage.registerDecoratorDefinition(new MyDecorator());
        TwitchSpawnLanguage.registerDecoratorDefinition(new MyParameterDecorator());
    }

    public static void registerEvents() throws TSLPluginError {
        TwitchSpawnLanguage.registerEventDefinition(new ExampleEvent());
    }

    public static void registerPredicates() throws TSLPluginError {
        TwitchSpawnLanguage.registerPredicateDefinition(new ExampleIfPredicate());

    }

    public static void registerActions() throws TSLPluginError {
        TwitchSpawnLanguage.registerActionDefinition(new ExamplePrintAction());
    }

    public static void registerExpressionBindingProviders() throws TSLPluginError {
        TSLExpressionBindings.registerBindingProvider("currentUNIX", System::currentTimeMillis);

        TSLExpressionBindings.registerBindingProvider("piTimesCurrentTime", () -> Math.PI * System.currentTimeMillis());

        TSLExpressionBindings.registerBindingProvider("add", () ->
                (Number3ToNumberFunction) (a, b, c) -> a.doubleValue() + b.doubleValue() + c.doubleValue());

        TSLExpressionBindings.registerBindingProvider("multiply", () ->
                (Number2ToNumberFunction) (a, b) -> a.doubleValue() * b.doubleValue());

        TSLExpressionBindings.registerBindingProvider("sum", () ->
                (NumberNToNumberFunction) (numbers) -> Stream.of(numbers).mapToDouble(Number::doubleValue).sum());
    }

}
