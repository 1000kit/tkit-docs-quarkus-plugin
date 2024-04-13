package org.tkit.maven.docs.quarkus.docs;


import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.tkit.maven.docs.quarkus.mapping.Mapping;

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


    public static ExtensionContainer create(MavenProject project, Configuration config)  {
        // filter all project dependencies

        var dependencies = project.getDependencies().stream()
                .filter(x -> !config.getDependenciesExcludeScopes().contains(x.getScope()))
                .filter(x -> config.getDependenciesIncludeGroups().stream().anyMatch(g -> x.getGroupId().startsWith(g)))
                .toList();


        var mapping = new Mapping(config.getDependenciesMappingFile());


        var container = new ExtensionContainer();

        for (Dependency dep : dependencies) {

            if (mapping.isUnknown(dep)) {
                container.addUnknowns(Extension.of(dep));
                continue;
            }

            if (!mapping.isEnabled(dep)) {
                continue;
            }

            var docUrl = mapping.docLink(dep);
            var configUrl = mapping.configLink(dep);
            container.addExtension(Extension.of(dep, docUrl, configUrl));
        }

        return container;
    }

}
