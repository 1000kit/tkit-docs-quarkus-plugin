# tkit-docs-quarkus-plugin

1000kit maven plugin to generated documentation for quarkus project.

[![License](https://img.shields.io/badge/license-Apache--2.0-green?style=for-the-badge&logo=apache)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/org.tkit.maven/tkit-docs-quarkus-plugin?logo=java&style=for-the-badge)](https://maven-badges.herokuapp.com/maven-central/org.tkit.maven/tkit-docs-quarkus-plugin)
[![GitHub Actions Status](https://img.shields.io/github/actions/workflow/status/1000kit/tkit-docs-quarkus-plugin/build.yml?logo=GitHub&style=for-the-badge)](https://github.com/1000kit/tkit-docs-quarkus-plugin/actions/workflows/build.yml)

### Maven configuration

Create a documentation in your maven project.
```xml
 <profile>
    <id>docs</id>
    <build>
        <plugins>
            <plugin>
                <groupId>org.tkit.maven</groupId>
                <artifactId>tkit-docs-quarkus-plugin</artifactId>
                <version>latest-version</version>
                <executions>
                    <execution>
                        <id>default</id>
                        <goals>
                            <goal>docs</goal>
                        </goals>
                        <phase>prepare-package</phase>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</profile>
```
