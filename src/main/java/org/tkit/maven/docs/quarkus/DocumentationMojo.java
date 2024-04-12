package org.tkit.maven.docs.quarkus;

import io.quarkus.qute.*;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.tkit.maven.docs.quarkus.docker.DockerContainer;
import org.tkit.maven.docs.quarkus.extensions.Extension;
import org.tkit.maven.docs.quarkus.extensions.ExtensionContainer;
import org.tkit.maven.docs.quarkus.docs.DocsContainer;
import org.tkit.maven.docs.quarkus.helm.HelmContainer;
import org.tkit.maven.docs.quarkus.index.IndexContainer;
import org.tkit.maven.docs.quarkus.mapping.Mapping;
import org.tkit.maven.docs.quarkus.properties.PropertiesContainer;
import org.tkit.maven.docs.quarkus.qute.EngineFactory;
import org.tkit.maven.docs.quarkus.version.VersionContainer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Mojo(name = "docs", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, threadSafe = true, requiresDependencyResolution = ResolutionScope.COMPILE)
public class DocumentationMojo extends AbstractDocsMojo {

    private static final String TEMPLATE_CONTAINER = "container";

    @Parameter(name = "skipDocs", property = "tkit.docs.skipDocs", defaultValue = "false")
    protected boolean skipDocs;

    @Parameter(name = "includeGroups", property = "tkit.docs.includeGroups", defaultValue = "io.quarkus,org.tkit.quarkus,io.quarkiverse,org.tkit.onecx")
    private List<String> includeGroups;

    @Parameter(name = "excludeScopes", property = "tkit.docs.excludeScopes", defaultValue = "test,provided")
    private List<String> excludeScopes;

    @Parameter(name = "mappingFile", property = "tkit.docs.mappingFile", defaultValue = "extensions-mapping.properties")
    private String mappingFile;

    @Parameter(name = "propertiesFile", property = "tkit.docs.propertiesFile", defaultValue = "src/main/resources/application.properties")
    protected String propertiesFile;

    @Parameter(name = "generateVersion", property = "tkit.docs.generateVersion", defaultValue = "true")
    protected boolean generateVersion;

    @Parameter(name = "generateExtensions", property = "tkit.docs.generateExtensions", defaultValue = "true")
    protected boolean generateExtensions;

    @Parameter(name = "generateProperties", property = "tkit.docs.generateProperties", defaultValue = "true")
    protected boolean generateProperties;

    @Parameter(name = "generateHelm", property = "tkit.docs.generateHelm", defaultValue = "true")
    protected boolean generateHelm;

    @Parameter(name = "generateDocker", property = "tkit.docs.generateDocker", defaultValue = "true")
    protected boolean generateDocker;

    @Parameter(name = "generateDocs", property = "tkit.docs.generateDocs", defaultValue = "true")
    protected boolean generateDocs;

    @Parameter(name = "generateIndex", property = "tkit.docs.generateIndex", defaultValue = "true")
    protected boolean generateIndex;

    @Parameter(name = "dockerRegistry", property = "tkit.docs.dockerRegistry", defaultValue = "ghcr.io/onecx/")
    protected String dockerRegistry;

    @Parameter(name = "helmRegistry", property = "tkit.docs.helmRegistry", defaultValue = "oci://ghcr.io/onecx/charts/")
    protected String helmRegistry;

    @Parameter(name = "helmValuesFile", property = "tkit.docs.helmValuesFile", defaultValue = "src/main/helm/values.yaml")
    protected String helmValuesFile;

    @Parameter(name = "indexIncludeFile", property = "tkit.docs.indexIncludeFile", defaultValue = "docs.adoc")
    protected String indexIncludeFile;

    @Parameter(name = "indexIncludeConfigFile", property = "tkit.docs.indexIncludeConfigFile", defaultValue = "true")
    protected boolean indexIncludeConfigFile;

    @Parameter(name = "copyConfigDir", property = "tkit.docs.copyConfigDir", defaultValue = "${project.build.directory}/asciidoc/generated/config")
    protected File copyConfigDir;

    @Parameter(name = "skipCopyConfigFile", property = "tkit.docs.skipCopyConfigFile", defaultValue = "false")
    protected boolean skipCopyConfigFile;

    @Override
    public void execute() throws MojoExecutionException {

        if (skipDocs) {
            getLog().info("tkit quarkus documentation plugin is disabled");
            return;
        }

        if ("pom".equals(getProject().getPackaging())) {
            getLog().debug("tkit quarkus documentation plugin is disabled for pom packaging");
            return;
        }

        var engine = EngineFactory.createEngine();

        var docsContainer = DocsContainer.create(getProject(), generateVersion, generateProperties, generateExtensions, generateDocker, generateHelm);

        var containerVersion = VersionContainer.create(getProject(), "0.0.0-rc.%s", "%s-rc.%s");

        renderTemplate(engine, containerVersion, "attributes.qute", "-attributes.adoc");

        if (docsContainer.isGenerateVersion()) {
            renderTemplate(engine, containerVersion, "version.qute", "-version.adoc");
        }

        if (docsContainer.isGenerateExtensions()) {
            var containerExtensions = createExtensions();
            renderTemplate(engine, containerExtensions, "extensions.qute", "-extensions.adoc");
        }

        if (docsContainer.isGenerateProperties()) {
            var containerProperties = PropertiesContainer.createContainer(propertiesFile);
            renderTemplate(engine, containerProperties, "properties.qute", "-properties.adoc");
        }

        if (docsContainer.isGenerateDocker()) {
            var dockerContainer = DockerContainer.create(containerVersion, getProject(), dockerRegistry);
            renderTemplate(engine, dockerContainer, "docker.qute", "-docker.adoc");
        }

        if (docsContainer.isGenerateHelm()) {
            var helmContainer = HelmContainer.create(containerVersion, getProject(), helmRegistry, helmValuesFile);
            renderTemplate(engine, helmContainer, "helm.qute", "-helm.adoc");
        }

        if (generateDocs) {
            renderTemplate(engine, docsContainer, "docs.qute", "-docs.adoc");
        }

        // copy other generated files
        var configFile = copyConfigFile();

        // generated index
        if (generateIndex) {


            if (indexIncludeConfigFile) {
                if (configFile == null) {
                    getLog().warn("Skip include config file in the index page. Generated config file does not exists.");
                }
            } else {
                getLog().warn("Skip include config file in the index page.");
                configFile = null;
            }

            var container = IndexContainer.create(getProject(), indexIncludeFile, configFile);
            Template tmp = engine.getTemplate("index.qute");
            writeToFile(tmp.data(TEMPLATE_CONTAINER, container).render(), "index.adoc");
        }

    }

    private void renderTemplate(Engine engine, Object container, String template, String outputSuffix) throws MojoExecutionException {
        Template tmp = engine.getTemplate(template);
        writeToSuffixFile(tmp.data(TEMPLATE_CONTAINER, container).render(), outputSuffix);
    }


    private ExtensionContainer createExtensions()  {
        // filter all project dependencies
        var project = getProject();

        var dependencies = project.getDependencies().stream()
                .filter(x -> !excludeScopes.contains(x.getScope()))
                .filter(x -> includeGroups.stream().anyMatch(f -> x.getGroupId().startsWith(f)))
                .toList();


        var mapping = new Mapping(mappingFile);


        var container = new ExtensionContainer();

        for (Dependency dep : dependencies) {

            if (mapping.isUnknown(dep)) {
                container.addUnknowns(Extension.of(dep));
                continue;
            }

            if (!mapping.isEnabled(dep)) {
                continue;
            }

            var docUrl = mapping.docLink(dep);
            var configUrl = mapping.configLink(dep);
            container.addExtension(Extension.of(dep, docUrl, configUrl));
        }

        return container;
    }


    private String copyConfigFile() {

        if (skipCopyConfigFile) {
            getLog().info("Copy configuration file is disabled");
            return null;
        }

        if (!copyConfigDir.exists()) {
            getLog().warn("Config file directory does not exists. Dir: " + copyConfigDir);
            return null;
        }

        return copyFile(copyConfigDir, getProject().getArtifactId() + ".adoc");
    }

    private String copyFile(File dir, String file) {
        Path from = dir.toPath().resolve(file);
        if (!from.toFile().exists()) {
            getLog().warn("Copy config file does not exists. File: " + from);
            return null;
        }
        try {
            var to = Paths.get(outputDir, file);
            Files.copy(from, to, REPLACE_EXISTING);
            getLog().info("Copy config file to: " + to);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return file;
    }
}
