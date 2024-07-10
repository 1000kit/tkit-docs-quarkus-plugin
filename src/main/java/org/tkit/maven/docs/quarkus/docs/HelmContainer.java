package org.tkit.maven.docs.quarkus.docs;

import java.nio.file.Files;
import java.nio.file.Path;

public class HelmContainer {

    private final String values;

    public static HelmContainer create(String helmValuesFile) {
        String values;
        try {
            values = Files.readString(Path.of(helmValuesFile));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return new HelmContainer(values);
    }

    private HelmContainer(String values) {
        this.values = values;
    }

    public String getValues() {
        return values;
    }
}
