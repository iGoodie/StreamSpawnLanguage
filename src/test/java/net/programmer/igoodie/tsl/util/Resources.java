package net.programmer.igoodie.tsl.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Resources {

    public static String readTSL(String rulesetName) {
        Path path = Paths.get(
                "src", "test", "resources",
                "rulesets", rulesetName
        );

        try {
            return new String(Files.readAllBytes(path));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
