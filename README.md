本文档基于 https://github.com/alibaba/spring-cloud-alibaba/blob/2022.x/spring-cloud-alibaba-docs/src/main/asciidoc-zh/graalvm.adoc 测试验证，并作配置优化而来。

一、安装环境

```bash
yum install freetype-devel unzip zip gcc
```

1.安装sdk

```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

2.安装java

```bash
sdk install java 22.3.r17-nik
sdk use java 22.3.r17-nik
```

这个版本要与后面的这个配置一致。
```xml
<plugin>
	<groupId>org.graalvm.buildtools</groupId>
	<artifactId>native-maven-plugin</artifactId>
	<configuration>
		<classesDirectory>${project.build.outputDirectory}</classesDirectory>
		<metadataRepository>
			<enabled>true</enabled>
		</metadataRepository>
		<requiredVersion>22.3</requiredVersion>
	</configuration>
	<executions>
		<execution>
			<id>add-reachability-metadata</id>
			<goals>
				<goal>add-reachability-metadata</goal>
				<!--compile时就触发-->
				<goal>compile</goal>
			</goals>
		</execution>
	</executions>
</plugin>
```

二、基于https://github.com/alibaba/spring-cloud-alibaba/releases/tag/2022.0.0.0 的源码改造项目顶层目录：

（1）这部分主要从spring-cloud-alibaba-examples 提取上来。
```xml
<profile>
</profile>
```

（2）修改的地方主要是：

```xml
<!-- add1 begin -->
<plugin>
   <groupId>org.graalvm.buildtools</groupId>
   <artifactId>native-maven-plugin</artifactId>
   <version>${native-maven-plugin.version}</version>
</plugin>
<!-- add1 end -->
```
（3）
```xml
 <!-- add2 begin-->
  <!--compile时就触发-->
  <goal>compile</goal>
   <!-- add2 end-->
```

完整文件如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-build</artifactId>
        <version>4.0.0</version>
        <relativePath/>
    </parent>

    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba</artifactId>
    <version>${revision}</version>
    <packaging>pom</packaging>
    <name>Spring Cloud Alibaba</name>
    <description>Spring Cloud Alibaba</description>
    <url>https://github.com/alibaba/spring-cloud-alibaba</url>

    <properties>
        <!-- Project revision -->
        <revision>2022.0.0.0</revision>

        <!-- Spring Cloud -->
        <spring.cloud.version>2022.0.0</spring.cloud.version>

        <!-- Spring Boot -->
        <spring-boot.version>3.0.2</spring-boot.version>

        <curator.version>4.0.1</curator.version>

        <!-- Maven Plugin Versions -->
        <maven-compiler-plugin.version>3.8.1</maven-compiler-plugin.version>
        <maven-deploy-plugin.version>2.8.2</maven-deploy-plugin.version>
        <!-- JUnit 5 requires Surefire version 2.22.0 or higher -->
        <maven-surefire-plugin.version>2.22.2</maven-surefire-plugin.version>
        <maven-source-plugin.version>3.2.1</maven-source-plugin.version>
        <maven-javadoc-plugin.version>3.1.1</maven-javadoc-plugin.version>
        <maven-gpg-plugin.version>3.0.1</maven-gpg-plugin.version>
        <flatten-maven-plugin.version>1.2.7</flatten-maven-plugin.version>
        <gmavenplus-plugin.version>1.6</gmavenplus-plugin.version>
        <jacoco.version>0.8.8</jacoco.version>
        <native-maven-plugin.version>0.9.19</native-maven-plugin.version>
    </properties>

    <modules>
        <module>spring-cloud-alibaba-examples</module>
    </modules>

    <dependencyManagement>
        <dependencies>

            <!-- Spring Dependencies -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring.cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${project.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

        </dependencies>
    </dependencyManagement>

    <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>org.jacoco</groupId>
                    <artifactId>jacoco-maven-plugin</artifactId>
                    <version>${jacoco.version}</version>
                </plugin>

                <!-- disable auto format from parent -->
                <plugin>
                    <groupId>io.spring.javaformat</groupId>
                    <artifactId>spring-javaformat-maven-plugin</artifactId>
                    <version>${spring-javaformat.version}</version>
                    <executions>
                        <execution>
                            <phase>validate</phase>
                            <configuration>
                                <skip>true</skip>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>

                <!-- add1 begin -->
                <plugin>
                    <groupId>org.graalvm.buildtools</groupId>
                    <artifactId>native-maven-plugin</artifactId>
                    <version>${native-maven-plugin.version}</version>
                </plugin>
                <!-- add1 end -->

            </plugins>
        </pluginManagement>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven-compiler-plugin.version}</version>
                <inherited>true</inherited>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>${maven-surefire-plugin.version}</version>
                <inherited>true</inherited>
                <configuration>
                    <forkCount>1</forkCount>
                    <reuseForks>false</reuseForks>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>flatten-maven-plugin</artifactId>
                <version>${flatten-maven-plugin.version}</version>
                <configuration>
                    <updatePomFile>true</updatePomFile>
                    <flattenMode>resolveCiFriendliesOnly</flattenMode>
                </configuration>
                <executions>
                    <execution>
                        <id>flatten</id>
                        <phase>process-resources</phase>
                        <goals>
                            <goal>flatten</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>flatten.clean</id>
                        <phase>clean</phase>
                        <goals>
                            <goal>clean</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>


        </plugins>
    </build>

    <profiles>
        <profile>
            <id>prod</id>
            <build>
                <pluginManagement>
                    <plugins>
                        <plugin>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-maven-plugin</artifactId>
                            <configuration>
                                <jvmArguments>
                                    -agentlib:native-image-agent=config-merge-dir=src/main/resources/META-INF/native-image/
                                </jvmArguments>
                                <image>
                                    <builder>paketobuildpacks/builder:tiny</builder>
                                    <env>
                                        <BP_NATIVE_IMAGE>true</BP_NATIVE_IMAGE>
                                    </env>
                                </image>
                            </configuration>
                            <executions>
                                <execution>
                                    <id>process-aot</id>
                                    <goals>
                                        <goal>process-aot</goal>
                                    </goals>
                                </execution>
                            </executions>
                        </plugin>
                        <plugin>
                            <groupId>org.graalvm.buildtools</groupId>
                            <artifactId>native-maven-plugin</artifactId>
                            <configuration>
                                <classesDirectory>${project.build.outputDirectory}</classesDirectory>
                                <metadataRepository>
                                    <enabled>true</enabled>
                                </metadataRepository>
                                <requiredVersion>22.3</requiredVersion>
                            </configuration>
                            <executions>
                                <execution>
                                    <id>add-reachability-metadata</id>
                                    <goals>
                                        <goal>add-reachability-metadata</goal>
                                        <!-- add2 begin-->
                                        <!--compile时就触发-->
                                        <goal>compile</goal>
                                         <!-- add2 end-->
                                    </goals>
                                </execution>
                            </executions>
                        </plugin>
                    </plugins>
                </pluginManagement>
            </build>
        </profile>
    </profiles>
</project>

```

三、子项目增加如下内容：
这样只需要单独对这些模块进行native构建

```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>

            <plugin>
                <groupId>org.graalvm.buildtools</groupId>
                <artifactId>native-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

```

四、构建：
mvn package -Pprod



五、运行：
cd spring-cloud-alibaba-examples/business-service/target
./storage-service

cd sca-graalvm/spring-cloud-alibaba-examples/storage-service/target

./business-service


六、测试：
