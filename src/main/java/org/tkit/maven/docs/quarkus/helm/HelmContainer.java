package org.tkit.maven.docs.quarkus.helm;

import org.apache.maven.project.MavenProject;
import org.tkit.maven.docs.quarkus.docker.DockerContainer;
import org.tkit.maven.docs.quarkus.version.VersionContainer;

import java.nio.file.Files;
import java.nio.file.Path;

public class HelmContainer {

    private final MavenProject project;

    private final String registry;

    private final VersionContainer version;

    private String values;

    public static HelmContainer create(VersionContainer versionContainer, MavenProject project, String registry, String valuesFile) {
        String values = null;
        try {
            values = Files.readString(Path.of(valuesFile));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return new HelmContainer(versionContainer, project, registry, values);
    }

    private HelmContainer(VersionContainer versionContainer, MavenProject project, String registry, String values) {
        this.project = project;
        this.registry = registry;
        this.version = versionContainer;
        this.values = values;
    }

    public MavenProject getProject() {
        return project;
    }

    public String getRegistry() {
        return registry;
    }

    public VersionContainer getVersion() {
        return version;
    }

    public String getValues() {
        return values;
    }
}
