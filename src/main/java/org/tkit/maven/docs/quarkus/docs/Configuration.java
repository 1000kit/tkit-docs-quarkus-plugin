package org.tkit.maven.docs.quarkus.docs;

import java.util.List;

public class Configuration {

    private String extensionsFile;
    private String attributesFile;
    private boolean properties;
    private boolean extensions;
    private boolean docker;
    private boolean helm;
    private boolean index;
    private boolean docs;
    private boolean attributes;
    private boolean indexConfig;
    private boolean indexHeader;
    private boolean indexDocs;
    private String indexDocsFile;
    private String indexConfigFile;
    private String indexHeaderFile;
    private String helmValuesFile;
    private String propertiesFile;
    private List<String> dependenciesIncludeGroups;
    private List<String> dependenciesExcludeScopes;
    private String dependenciesMappingFile;

    public String getExtensionsFile() {
        return extensionsFile;
    }

    public void setExtensionsFile(String extensionsFile) {
        this.extensionsFile = extensionsFile;
    }

    public String getAttributesFile() {
        return attributesFile;
    }

    public void setAttributesFile(String attributesFile) {
        this.attributesFile = attributesFile;
    }

    public String getIndexDocsFile() {
        return indexDocsFile;
    }

    public void setIndexDocsFile(String indexDocsFile) {
        this.indexDocsFile = indexDocsFile;
    }

    public String getIndexConfigFile() {
        return indexConfigFile;
    }

    public void setIndexConfigFile(String indexConfigFile) {
        this.indexConfigFile = indexConfigFile;
    }

    public List<String> getDependenciesIncludeGroups() {
        return dependenciesIncludeGroups;
    }

    public void setDependenciesIncludeGroups(List<String> dependenciesIncludeGroups) {
        this.dependenciesIncludeGroups = dependenciesIncludeGroups;
    }

    public List<String> getDependenciesExcludeScopes() {
        return dependenciesExcludeScopes;
    }

    public void setDependenciesExcludeScopes(List<String> dependenciesExcludeScopes) {
        this.dependenciesExcludeScopes = dependenciesExcludeScopes;
    }

    public String getDependenciesMappingFile() {
        return dependenciesMappingFile;
    }

    public void setDependenciesMappingFile(String dependenciesMappingFile) {
        this.dependenciesMappingFile = dependenciesMappingFile;
    }

    public boolean isIndexConfig() {
        return indexConfig;
    }

    public void setIndexConfig(boolean indexConfig) {
        this.indexConfig = indexConfig;
    }

    public boolean isIndexHeader() {
        return indexHeader;
    }

    public void setIndexHeader(boolean indexHeader) {
        this.indexHeader = indexHeader;
    }

    public boolean isIndexDocs() {
        return indexDocs;
    }

    public void setIndexDocs(boolean indexDocs) {
        this.indexDocs = indexDocs;
    }

    public String getIndexHeaderFile() {
        return indexHeaderFile;
    }

    public void setIndexHeaderFile(String indexHeaderFile) {
        this.indexHeaderFile = indexHeaderFile;
    }

    public boolean isAttributes() {
        return attributes;
    }

    public void setAttributes(boolean attributes) {
        this.attributes = attributes;
    }

    public void setDocs(boolean docs) {
        this.docs = docs;
    }

    public boolean isDocs() {
        return docs;
    }

    public void setIndex(boolean index) {
        this.index = index;
    }

    public boolean isIndex() {
        return index;
    }

    public boolean isProperties() {
        return properties;
    }

    public void setProperties(boolean properties) {
        this.properties = properties;
    }

    public boolean isExtensions() {
        return extensions;
    }

    public void setExtensions(boolean extensions) {
        this.extensions = extensions;
    }

    public boolean isDocker() {
        return docker;
    }

    public void setDocker(boolean docker) {
        this.docker = docker;
    }

    public boolean isHelm() {
        return helm;
    }

    public void setHelm(boolean helm) {
        this.helm = helm;
    }

    public String getHelmValuesFile() {
        return helmValuesFile;
    }

    public void setHelmValuesFile(String helmValuesFile) {
        this.helmValuesFile = helmValuesFile;
    }

    public String getPropertiesFile() {
        return propertiesFile;
    }

    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }
}
