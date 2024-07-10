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
    @Parameter(name = "outputDir", property = "docs.outputDir", defaultValue = "docs/modules/${project.artifactId}/pages/")
    protected String outputDir;

    protected void writeToFile(String data, String file) throws MojoExecutionException {
        Path out1 = getOutputFile(file);
        writeTo(out1, data);
    }
    protected void writeToFile(String data, String file, String subDir) throws MojoExecutionException {
        Path out1 = getOutputFile(file, subDir);
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

    protected Path getOutputFile(String name) {
        Path dir = getOutputDir();
        return dir.resolve(name);
    }
    protected Path getOutputFile(String name, String subDir) {
        Path dir = getOutputDir(subDir);
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

    protected Path getOutputDir(String subDir) {
        Path parent = Paths.get(outputDir + subDir);
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
