package org.tkit.maven.docs.quarkus.docs;


import org.apache.maven.project.MavenProject;

public class DocsContainer {

    private boolean generateVersion;

    private boolean generateExtensions;

    private boolean generateProperties;

    private boolean generateHelm;

    private boolean generateDocker;

    private MavenProject project;

    public static DocsContainer create(MavenProject project, boolean generateVersion, boolean generateProperties, boolean generateExtensions, boolean generateDocker, boolean generateHelm) {
        var g = new DocsContainer();
        g.generateDocker = generateDocker;
        g.generateProperties = generateProperties;
        g.generateExtensions = generateExtensions;
        g.generateVersion = generateVersion;
        g.generateHelm = generateHelm;
        g.project = project;
        return g;
    }

    public MavenProject getProject() {
        return project;
    }

    public boolean isGenerateDocker() {
        return generateDocker;
    }

    public boolean isGenerateExtensions() {
        return generateExtensions;
    }

    public boolean isGenerateHelm() {
        return generateHelm;
    }

    public boolean isGenerateProperties() {
        return generateProperties;
    }

    public boolean isGenerateVersion() {
        return generateVersion;
    }
}
