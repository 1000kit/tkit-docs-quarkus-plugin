package org.tkit.maven.docs.quarkus;

import io.quarkus.qute.*;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.tkit.maven.docs.quarkus.docs.Configuration;
import org.tkit.maven.docs.quarkus.docs.Container;
import org.tkit.maven.docs.quarkus.qute.EngineFactory;

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

    @Parameter(name = "excludePackages", property = "tkit.docs.excludePackages", defaultValue = "pom")
    private List<String> excludePackages;

    @Parameter(name = "dependenciesIncludeGroups", property = "tkit.docs.dependencies.includeGroups", defaultValue = "io.quarkus,org.tkit.quarkus,io.quarkiverse,org.tkit.onecx")
    private List<String> dependenciesIncludeGroups;

    @Parameter(name = "dependenciesExcludeScopes", property = "tkit.docs.dependencies.excludeScopes", defaultValue = "test,provided")
    private List<String> dependenciesExcludeScopes;

    @Parameter(name = "dependenciesMappingFile", property = "tkit.docs.dependencies.mappingFile", defaultValue = "extensions-mapping.properties")
    private String dependenciesMappingFile;

    @Parameter(name = "propertiesFile", property = "tkit.docs.file.properties", defaultValue = "src/main/resources/application.properties")
    protected String propertiesFile;

    @Parameter(name = "helmValuesFile", property = "tkit.docs.file.helm.values", defaultValue = "src/main/helm/values.yaml")
    protected String helmValuesFile;

    @Parameter(name = "quarkusConfigDir", property = "tkit.docs.quarkus.config.dir", defaultValue = "${project.build.directory}/asciidoc/generated/config")
    protected File quarkusConfigDir;

    @Parameter(name = "quarkusConfigFile", property = "tkit.docs.quarkus.config.file", defaultValue = "${project.artifactId}.adoc")
    protected String quarkusConfigFile;

    @Parameter(name = "skipCopyQuarkusConfigFile", property = "tkit.docs.quarkus.config.copy.skip", defaultValue = "false")
    protected boolean skipCopyQuarkusConfigFile;

    @Parameter(name = "extensions", property = "tkit.docs.generate.extensions", defaultValue = "true")
    protected boolean extensions;

    @Parameter(name = "extensionsFile", property = "tkit.docs.generate.extensions.file", defaultValue = "${project.artifactId}-extensions.adoc")
    protected String extensionsFile;

    @Parameter(name = "properties", property = "tkit.docs.generate.properties", defaultValue = "true")
    protected boolean properties;

    @Parameter(name = "attributes", property = "tkit.docs.generate.attributes", defaultValue = "true")
    protected boolean attributes;

    @Parameter(name = "attributesFile", property = "tkit.docs.generate.attributes.file", defaultValue = "${project.artifactId}-attributes.adoc")
    protected String attributesFile;

    @Parameter(name = "helm", property = "tkit.docs.generate.helm", defaultValue = "true")
    protected boolean helm;

    @Parameter(name = "docker", property = "tkit.docs.generate.docker", defaultValue = "true")
    protected boolean docker;

    @Parameter(name = "docs", property = "tkit.docs.generate.docs", defaultValue = "true")
    protected boolean docs;

    @Parameter(name = "index", property = "tkit.docs.generate.index", defaultValue = "true")
    protected boolean index;

    @Parameter(name = "indexHeaderFile", property = "tkit.docs.index.header", defaultValue = "docs.adoc")
    protected String indexHeaderFile;

    @Parameter(name = "indexIncludeHeader", property = "tkit.docs.index.include.header", defaultValue = "true")
    protected boolean indexIncludeHeader;

    @Parameter(name = "indexIncludeDocs", property = "tkit.docs.index.include.docs", defaultValue = "true")
    protected boolean indexIncludeDocs;

    @Parameter(name = "indexDocsFile", property = "tkit.docs.index.include.docs", defaultValue = "${project.artifactId}-docs.adoc")
    protected String indexDocsFile;

    @Parameter(name = "indexIncludeConfig", property = "tkit.docs.index.include.config", defaultValue = "true")
    protected boolean indexIncludeConfig;

    @Parameter(name = "openApiFiles", property = "tkit.docs.generate.openApi.file")
    protected String[] openApiFiles;

    @Parameter(name = "openApiBasePath", property = "tkit.docs.generate.openApi.path", defaultValue = "main/src/main/openapi")
    protected String openApiBasePath;

    @Parameter(name = "openApi", property = "tkit.docs.generate.openApi", defaultValue = "true")
    protected boolean openApi;


    @Override
    public void execute() throws MojoExecutionException {
        if (skipDocs) {
            getLog().info("1000kit quarkus documentation plugin is disabled");
            return;
        }

        if (excludePackages.contains(getProject().getPackaging())) {
            getLog().debug("1000kit quarkus documentation plugin is disabled for " + getProject().getPackaging() + " packaging");
            return;
        }

        // copy quarkus generated files
        File configFile = null;
        if (indexIncludeConfig) {
            configFile = copyConfigFile();
        }

        var config = getConfiguration(configFile != null);


        var container = Container.create(getProject(), config);

        var engine = EngineFactory.createEngine();
        if (config.isAttributes()) {
            renderTemplate(engine, container, "attributes.qute", config.getAttributesFile());
        }
        if (config.isDocs()) {
            renderTemplate(engine, container, "docs.qute", config.getIndexDocsFile());
        }
        if (config.isExtensions()) {
            renderTemplate(engine, container, "extensions.qute", config.getExtensionsFile());
        }
        if(config.isOpenApi()) {
            for(int i=0; config.getOpenApiFiles().length > i; i++) {
                try {
                    config.setCurrentOpenApiFile(config.getOpenApiFiles()[i]);
                    renderTemplate(engine, container, "openApi.qute", config.getCurrentOpenApiFile().substring(0, config.getCurrentOpenApiFile().lastIndexOf('.')) + ".adoc", "/openapi");
                } catch (MojoExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // generated index
        if (config.isIndex()) {
            renderTemplate(engine, container, "index.qute", "index.adoc");
        }

    }

    private Configuration getConfiguration(boolean indexConfig) {
        var config = new Configuration();
        config.setIndexDocsFile(indexDocsFile);
        config.setAttributesFile(attributesFile);
        config.setExtensionsFile(extensionsFile);
        config.setOpenApiFiles(openApiFiles);
        config.setDocker(docker);
        config.setExtensions(extensions);
        config.setProperties(properties);
        config.setHelm(helm);
        config.setIndex(index);
        config.setOpenApi(openApi);
        config.setDocs(docs);
        config.setAttributes(attributes);
        config.setHelmValuesFile(helmValuesFile);
        config.setPropertiesFile(propertiesFile);
        config.setIndexDocs(indexIncludeDocs);
        config.setIndexConfig(indexConfig);
        config.setIndexHeader(indexIncludeHeader);
        config.setIndexHeaderFile(indexHeaderFile);
        config.setIndexConfigFile(quarkusConfigFile);
        config.setDependenciesExcludeScopes(dependenciesExcludeScopes);
        config.setDependenciesMappingFile(dependenciesMappingFile);
        config.setDependenciesIncludeGroups(dependenciesIncludeGroups);
        config.setOpenApiBasePath(openApiBasePath);
        return config;
    }


    private void renderTemplate(Engine engine, Object container, String template, String name) throws MojoExecutionException {
        Template tmp = engine.getTemplate(template);
        writeToFile(tmp.data(TEMPLATE_CONTAINER, container).render(), name);
    }
    private void renderTemplate(Engine engine, Object container, String template, String name, String subDir) throws MojoExecutionException {
        Template tmp = engine.getTemplate(template);
        writeToFile(tmp.data(TEMPLATE_CONTAINER, container).render(), name, subDir);
    }



    private File copyConfigFile() {

        if (skipCopyQuarkusConfigFile) {
            getLog().info("Copy configuration file is disabled");
            return null;
        }

        Path from = quarkusConfigDir.toPath().resolve(quarkusConfigFile);

        if (!from.toFile().exists()) {
            getLog().warn("Config file does not exists. File: " + quarkusConfigFile);
            return null;
        }

        var to = Paths.get(outputDir, quarkusConfigFile);

        try {

            Files.copy(from, to, REPLACE_EXISTING);
            getLog().info("Copy config file to: " + to);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return to.toFile();
    }

}
