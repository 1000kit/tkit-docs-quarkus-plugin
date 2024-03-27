package org.tkit.maven.docs.quarkus.mapping;


import org.apache.maven.model.Dependency;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mapping {

    private static final Map<String, Pattern> PATTERN_MAP = new HashMap<>();

    private static final String SUFFIX_GROUP_VERSION = ".version.pattern";
    private static final String SUFFIX_DOC_LINK = ".doc.url";
    private static final String SUFFIX_CONFIG_LINK = ".config.url";

    private final Properties properties;

    public Mapping(String mappingFile) {
        // load mapping file
        properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(mappingFile));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getKey(Dependency dep) {
        return properties.getProperty(getId(dep));
    }

    public boolean isEnabled(Dependency dep) {
        return Boolean.parseBoolean(getKey(dep));
    }

    public boolean isUnknown(Dependency dep) {
        return getKey(dep) == null;
    }

    public String configLink(Dependency dep) {
        return createLink(dep, SUFFIX_CONFIG_LINK, getVersion(dep));
    }

    public String docLink(Dependency dep) {
        return createLink(dep, SUFFIX_DOC_LINK, getVersion(dep));
    }

    private String getId(Dependency dep) {
        return dep.getGroupId() + "." + dep.getArtifactId();
    }
    private String createLink(Dependency dep, String keySuffix, String version) {
        var id = getId(dep);
        var url = properties.getProperty(id + keySuffix);
        if (url == null ) {
            var gen = properties.getProperty(dep.getGroupId() + keySuffix);
            if (gen != null) {
                gen = gen.replace("{version}", version);
                gen = gen.replace("{artifactId}", dep.getArtifactId());
                url = gen;
            }
        }
        return url;
    }

    public String getVersion(Dependency dep) {
        var version = dep.getVersion();

        // check existing pattern for groupId
        Pattern pattern = PATTERN_MAP.get(dep.getGroupId());
        if (pattern != null) {
            Matcher matcher = pattern.matcher(version);
            if (matcher.find()) {
                return matcher.group();
            }
            return version;
        }

        // check mapping properties
        var ver = properties.getProperty(dep.getGroupId() + SUFFIX_GROUP_VERSION);
        if (ver != null && !ver.isBlank()) {
            pattern = Pattern.compile(ver);
            PATTERN_MAP.put(dep.getGroupId(), pattern);
            Matcher matcher = pattern.matcher(version);
            if (matcher.find()) {
                return matcher.group();
            }
        }
        return version;
    }
}
