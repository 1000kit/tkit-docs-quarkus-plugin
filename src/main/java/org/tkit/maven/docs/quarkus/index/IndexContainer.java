package org.tkit.maven.docs.quarkus.index;

import org.apache.maven.project.MavenProject;

public class IndexContainer {

    private String includeFile;

    private String configFile;

    private MavenProject project;

    public static IndexContainer create(MavenProject project, String includeFile, String configFile) {
        var i = new IndexContainer();
        i.includeFile = includeFile;
        i.project = project;
        i.configFile = configFile;
        return i;
    }

    private IndexContainer() {
    }

    public MavenProject getProject() {
        return project;
    }

    public boolean isConfigFile() {
        return configFile != null;
    }

    public String getConfigFile() {
        return configFile;
    }

    public String getIncludeFile() {
        return includeFile;
    }

}
