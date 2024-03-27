package org.tkit.maven.docs.quarkus.version;

import org.apache.maven.project.MavenProject;
import pl.project13.core.CommitIdGenerationMode;
import pl.project13.core.CommitIdPropertiesOutputFormat;
import pl.project13.core.GitCommitIdExecutionException;
import pl.project13.core.GitCommitIdPlugin;
import pl.project13.core.git.GitDescribeConfig;
import pl.project13.core.log.LogInterface;
import pl.project13.core.util.BuildFileChangeListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;

public class GitCallback implements GitCommitIdPlugin.Callback {

    private final MavenProject project;

    private final Properties output;

    public GitCallback(MavenProject project) {
        this.project = project;
        this.output = new Properties();
    }

    public Properties getOutput() {
        return output;
    }

    @Override
    public Supplier<String> supplyProjectVersion() {
        return project::getVersion;
    }

    @Nonnull
    @Override
    public LogInterface getLogInterface() {
        return new CustomLogInterface();
    }

    @Nonnull
    @Override
    public String getDateFormat() {
        return "yyyy-MM-dd'T'HH:mm:ssXXX";
    }

    @Nonnull
    @Override
    public String getDateFormatTimeZone() {
        return "";
    }

    @Nonnull
    @Override
    public String getPrefixDot() {
        return "";
    }

    @Override
    public List<String> getExcludeProperties() {
        return List.of();
    }

    @Override
    public List<String> getIncludeOnlyProperties() {
        return List.of();
    }

    @Nullable
    @Override
    public Date getReproducibleBuildOutputTimestamp() throws GitCommitIdExecutionException {
        return new Date();
    }

    @Override
    public boolean useNativeGit() {
        return false;
    }

    @Override
    public long getNativeGitTimeoutInMs() {
        return 30000;
    }

    @Override
    public int getAbbrevLength() {
        return 7;
    }

    @Override
    public GitDescribeConfig getGitDescribe() {
        return new GitDescribeConfig();
    }

    @Override
    public CommitIdGenerationMode getCommitIdGenerationMode() {
        return CommitIdGenerationMode.FLAT;
    }

    @Override
    public boolean getUseBranchNameFromBuildEnvironment() {
        return true;
    }

    @Override
    public boolean isOffline() {
        return false;
    }

    @Override
    public String getEvaluateOnCommit() {
        return "HEAD";
    }

    @Override
    public File getDotGitDirectory() {
        return project.getBasedir().toPath().resolve(".git").toFile();
    }

    @Override
    public boolean shouldGenerateGitPropertiesFile() {
        return false;
    }

    @Override
    public void performPublishToAllSystemEnvironments(Properties properties) {

    }

    @Override
    public void performPropertiesReplacement(Properties properties) {
        output.putAll(properties);
    }

    @Override
    public CommitIdPropertiesOutputFormat getPropertiesOutputFormat() {
        return null;
    }

    @Override
    public BuildFileChangeListener getBuildFileChangeListener() {
        return null;
    }

    @Override
    public String getProjectName() {
        return project.getName();
    }

    @Override
    public File getProjectBaseDir() {
        return project.getBasedir();
    }

    @Override
    public File getGenerateGitPropertiesFile() {
        return null;
    }

    @Override
    public Charset getPropertiesSourceCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public boolean shouldPropertiesEscapeUnicode() {
        return true;
    }

    @Override
    public boolean shouldFailOnNoGitDirectory() {
        return true;
    }
}
