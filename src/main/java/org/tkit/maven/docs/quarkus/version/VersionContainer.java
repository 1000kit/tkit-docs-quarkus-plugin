package org.tkit.maven.docs.quarkus.version;

import org.apache.maven.project.MavenProject;

import pl.project13.core.GitCommitIdPlugin;

import java.util.Properties;

import static pl.project13.core.GitCommitPropertyConstant.*;

public class VersionContainer {

    private String lastReleaseVersion;

    private String lastRcVersion;

    private Properties properties;

    public String getLastRcVersion() {
        return lastRcVersion;
    }

    public boolean isLastReleaseVersion() {
        return lastReleaseVersion != null && !lastReleaseVersion.isBlank();
    }

    public String getLastReleaseVersion() {
        return lastReleaseVersion;
    }

    public Properties getProperties() {
        return properties;
    }

    public static VersionContainer create(MavenProject project, String rcTemplate, String releaseTemplate) {

        final GitCallback cb = new GitCallback(project);
        try {
            GitCommitIdPlugin.runPlugin(cb, new Properties());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        var properties =  cb.getOutput();

        var lastTag = properties.getProperty(CLOSEST_TAG_NAME);
        var lastCount = properties.getProperty(CLOSEST_TAG_COMMIT_COUNT);
        var totalCount = properties.getProperty(TOTAL_COMMIT_COUNT);


        var rcVersion = String.format(rcTemplate, totalCount);
        if (lastTag != null && !lastTag.isBlank()) {
            rcVersion = String.format(releaseTemplate, lastTag, lastCount);
        }


        var v = new VersionContainer();
        v.lastReleaseVersion = lastTag;
        v.lastRcVersion = rcVersion;
        v.properties = properties;
        return v;
    }

}
