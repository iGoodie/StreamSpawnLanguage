package net.programmer.igoodie.tsl;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.util.TSLRandomizer;
import org.junit.jupiter.api.Test;

public class RandomizerTests {

    @Test
    public void shouldParsePercentages() throws TSLSyntaxError {
        TSLRandomizer<String> randomizer = new TSLRandomizer<>();

        randomizer.addElement("Coal", "90.301");
        randomizer.addElement("Diamond", "05.5");

        System.out.println(randomizer.getTotalPercentage());
        for (int i = 0; i < 100; i++) {
            System.out.println(randomizer.randomItem());
        }
    }

}
