package org.tkit.maven.docs.quarkus.docs;


import org.apache.maven.project.MavenProject;

public class Container {

    private Configuration config;

    private MavenProject project;

    private HelmContainer helm;

    private PropertiesContainer properties;

    private ExtensionContainer extensions;

    public static Container create(MavenProject project, Configuration config) {
        var g = new Container();
        g.config = config;
        g.project = project;
        if (config.isExtensions()) {
            g.extensions = ExtensionContainer.create(project, config);
        }
        if (config.isHelm()) {
            g.helm = HelmContainer.create(config.getHelmValuesFile());
        }
        if (config.isProperties()) {
            g.properties = PropertiesContainer.create(config.getPropertiesFile());
        }
        return g;
    }

    public ExtensionContainer getExtensions() {
        return extensions;
    }

    public PropertiesContainer getProperties() {
        return properties;
    }

    public HelmContainer getHelm() {
        return helm;
    }

    public MavenProject getProject() {
        return project;
    }

    public Configuration getConfig() {
        return config;
    }
}
