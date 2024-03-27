package org.tkit.maven.docs.quarkus.extensions;

import org.apache.maven.model.Dependency;

public class Extension {

    private Dependency dependency;

    private String docUrl;

    private String configUrl;

    public static Extension of(Dependency dependency) {
        return of(dependency, null, null);
    }

    public static Extension of(Dependency dependency, String docUrl, String configUrl) {
        var e = new Extension();
        e.configUrl = configUrl;
        e.docUrl = docUrl;
        e.dependency = dependency;
        return e;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public boolean isDocUrl() {
        return docUrl != null && !docUrl.isBlank();
    }

    public String getConfigUrl() {
        return configUrl;
    }

    public boolean isConfigUrl() {
        return configUrl != null && !configUrl.isBlank();
    }
}