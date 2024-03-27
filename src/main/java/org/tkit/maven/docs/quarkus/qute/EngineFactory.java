package org.tkit.maven.docs.quarkus.qute;

import io.quarkus.qute.Engine;
import io.quarkus.qute.ReflectionValueResolver;


public class EngineFactory {

    public static Engine createEngine() {
        return Engine.builder()
                .addDefaults()
                .addLocator(new CustomTemplateLocator())
                .addValueResolver(new ReflectionValueResolver())
                .removeStandaloneLines(true)
                .strictRendering(false)
                .build();
    }
}
