package net.programmer.igoodie.tsl.exception;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TSLParsingError extends TSLError {

    private List<TSLSyntaxError> syntaxErrors;

    public TSLParsingError(TSLSyntaxError... syntaxErrors) {
        this(Arrays.asList(syntaxErrors));
    }

    public TSLParsingError(List<TSLSyntaxError> syntaxErrors) {
        super(syntaxErrors.stream().map(TSLSyntaxError::getMessage).collect(Collectors.joining("\n")));
        this.syntaxErrors = syntaxErrors;
    }

    public List<TSLSyntaxError> getSyntaxErrors() {
        return syntaxErrors;
    }

}
