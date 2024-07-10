package org.tkit.maven.docs.quarkus.docs;

import java.nio.file.Files;
import java.nio.file.Path;

public class OpenApiContainer {

  private final String values;

  public static OpenApiContainer create(String openApiFile) {
    String values;
    System.out.println("MYLOG: CREATE CONTAINER");
    try {
      values = Files.readString(Path.of(openApiFile));
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return new OpenApiContainer(values);
  }

  private OpenApiContainer(String values) {
    this.values = values;
  }

  public String getValues() {
    return values;
  }
}