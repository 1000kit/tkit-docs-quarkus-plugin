package org.tkit.maven.docs.quarkus.docker;

import org.apache.maven.project.MavenProject;
import org.tkit.maven.docs.quarkus.version.VersionContainer;

public class DockerContainer {

    private final MavenProject project;

    private final String registry;

    private final VersionContainer version;

    public static DockerContainer create(VersionContainer versionContainer, MavenProject project, String registry) {
        return new DockerContainer(versionContainer, project, registry);
    }

    private DockerContainer(VersionContainer versionContainer, MavenProject project, String registry) {
        this.project = project;
        this.registry = registry;
        this.version = versionContainer;
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

}
