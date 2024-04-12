package org.tkit.maven.docs.quarkus.version;

import org.apache.maven.project.MavenProject;

public class VersionContainer {

    private String lastReleaseVersion;

    private String lastRcVersion;

    private MavenProject project;

    public String getLastRcVersion() {
        return lastRcVersion;
    }

    public boolean isLastReleaseVersion() {
        return lastReleaseVersion != null && !lastReleaseVersion.isBlank();
    }

    public String getLastReleaseVersion() {
        return lastReleaseVersion;
    }

    public MavenProject getProject() {
        return project;
    }

    public static VersionContainer create(MavenProject project, String rcTemplate, String releaseTemplate) {

        var v = new VersionContainer();
        v.lastReleaseVersion = null;
        v.lastRcVersion = null;
        v.project = project;
        return v;
    }

}
