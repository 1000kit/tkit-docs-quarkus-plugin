package org.tkit.maven.docs.quarkus.docs;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PropertiesContainer {

    private final List<String> properties;


    public static PropertiesContainer create(String propertiesFile) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(propertiesFile));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return new PropertiesContainer(filter(lines));
    }

    private PropertiesContainer(List<String> properties) {
        this.properties = properties;
    }

    private static List<String> filter(List<String> lines) {
        return lines.stream().filter(line -> {
            var tmp = line.trim();

            if (tmp.isBlank()) {
                return false;
            }
            if (tmp.startsWith("#") && !tmp.startsWith("##")) {
                return false;
            }
            // skip profiles
            if (tmp.startsWith("%") && !tmp.startsWith("%prod")) {
                return false;
            }

            // skip special properties
            return !tmp.startsWith("quarkus.test");
        }).toList();
    }

    public List<String> getProperties() {
        return properties;
    }

    public String getContent() {
        return String.join("\n", properties);
    }
}
