package org.tkit.maven.docs.quarkus;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public abstract class AbstractDocsMojo extends AbstractMojo {

    /**
     * The project being built.
     */
    @Parameter(readonly = true, required = true, defaultValue = "${project}")
    protected MavenProject currentProject;

    /**
     * The output file
     */
    @Parameter(name = "outputDir", property = "docs.outputDir", defaultValue = "${project.build.directory}/asciidoc/generated/config")
    protected String outputDir;

    protected void writeToFile(String data, String file) throws MojoExecutionException {
        Path out1 = getOutputFile(file);
        writeTo(out1, data);
    }

    protected void writeToSuffixFile(String data, String fileSuffix) throws MojoExecutionException {
        Path out1 = getOutputFileSuffix(fileSuffix);
        writeTo(out1, data);
    }

    protected void writeTo(Path out1, String data) throws MojoExecutionException {
        try {
            Files.writeString(out1, data,  StandardOpenOption.CREATE);
            getLog().info("Generate documentation file: " + out1);
        } catch (Exception ex) {
            getLog().error("Error generate documentation file: " + out1, ex);
            throw new MojoExecutionException(ex);
        }
    }

    protected MavenProject getProject() {
        return currentProject;
    }

    protected Path getOutputFileSuffix(String suffix) {
        return getOutputFile(currentProject.getArtifactId() + suffix);
    }

    protected Path getOutputFile(String name) {
        Path dir = getOutputDir();
        return dir.resolve(name);
    }

    protected Path getOutputDir() {
        Path parent = Paths.get(outputDir);
        File dir = parent.toFile();

        if (!dir.exists()) {
            var r = dir.mkdirs();
            if (r) {
                getLog().debug("Create output directory " + outputDir);
            }
        }
        return parent;
    }
}
