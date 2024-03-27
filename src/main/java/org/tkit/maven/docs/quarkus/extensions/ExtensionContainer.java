package org.tkit.maven.docs.quarkus.extensions;


import java.util.ArrayList;
import java.util.List;

public class ExtensionContainer {

    private final List<Extension> extensions = new ArrayList<>();

    private final List<Extension> unknowns = new ArrayList<>();

    public List<Extension> getExtensions() {
        return extensions;
    }

    public void addExtension(Extension extension) {
        extensions.add(extension);
    }

    public List<Extension> getUnknowns() {
        return unknowns;
    }

    public void addUnknowns(Extension extension) {
        unknowns.add(extension);
    }
}
