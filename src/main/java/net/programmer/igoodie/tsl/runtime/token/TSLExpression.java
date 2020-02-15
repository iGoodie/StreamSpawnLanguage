package net.programmer.igoodie.tsl.runtime.token;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.context.TSLExpressionBindings;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TSLExpression extends TSLToken {

    public static final ScriptEngine NASHORN_ENGINE = new ScriptEngineManager().getEngineByName("nashorn");
    public static final TSLExpressionBindings BINDINGS = new TSLExpressionBindings();

    private boolean validated;
    private String expression;

    public TSLExpression(String expression) {
        this.expression = expression;
    }

    public void validate(TSLContext validationContext) throws TSLSyntaxError {
        try {
            evaluate(validationContext);
            validated = true;

        } catch (ScriptException e) {
            throw new TSLSyntaxError(
                    e.getMessage(),
                    TSLSyntaxError.causedNear(TSLTokenizer.EXPR_BEGIN + expression + TSLTokenizer.EXPR_END, 0)
            );
        }
    }

    private Object evaluate(TSLContext context) throws ScriptException {
        BINDINGS.attachContext(context);
        Object evaluation = NASHORN_ENGINE.eval(expression, BINDINGS);
        BINDINGS.deattachContext();
        return String.valueOf(evaluation);
    }

    @Override
    public String getRaw() {
        return TSLTokenizer.EXPR_BEGIN
                + expression
                + TSLTokenizer.EXPR_END;
    }

    @Override
    public String getValue(TSLContext context) {
        if (!validated) {
            throw new InternalError("Evaluator called before validation!");
        }

        try {
            return String.valueOf(evaluate(context));
        } catch (ScriptException ignored) {
            return null;
        }
    }

    @Override
    public String toString() {
        return this.getRaw();
    }

    public static void updateBindings() {
        TSLExpressionBindings.updateBinding(BINDINGS);
    }

}
