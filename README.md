本文档基于 
https://github.com/alibaba/spring-cloud-alibaba/blob/2022.x/spring-cloud-alibaba-docs/src/main/asciidoc-zh/graalvm.adoc 以及dubbo项目：
https://github.com/apache/dubbo-samples/tree/master/2-advanced/dubbo-samples-native-image-registry 以及 https://github.com/apache/dubbo-samples/tree/master/1-basic/dubbo-samples-spring-boot


测试整合验证，并作配置优化而来。

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
sdk install java 23.0.5.r17-nik
sdk use java 23.0.5.r17-nik
sdk default java 23.0.5.r17-nik
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

注意：spring-boot 使用3.0.9。此版本对支持@Aspect在native-image下运行。
（1）父项目
```xml
<dependencyManagement>
    <dependencies>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-build-dependencies</artifactId>
            <version>${spring-cloud-build.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>

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
            <version>${spring.cloud.alibaba.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>

        <!-- dubbo begin-->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-bom</artifactId>
            <version>${dubbo.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-remoting-zookeeper-curator5</artifactId>
            <version>${dubbo.version}</version>
            <exclusions>
                <exclusion>
                    <artifactId>slf4j-log4j12</artifactId>
                    <groupId>org.slf4j</groupId>
                </exclusion>
                <exclusion>
                    <artifactId>log4j</artifactId>
                    <groupId>log4j</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-metadata-report-zookeeper</artifactId>
            <version>${dubbo.version}</version>
            <exclusions>
                <exclusion>
                    <artifactId>dubbo-metrics-prometheus</artifactId>
                    <groupId>org.apache.dubbo</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-serialization-jdk</artifactId>
            <version>${dubbo.version}</version>
            <exclusions>
                <exclusion>
                    <artifactId>hessian-lite</artifactId>
                    <groupId>com.alibaba</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-spring-boot-autoconfigure</artifactId>
            <version>${dubbo.version}</version>
            <exclusions>
                <exclusion>
                    <artifactId>dubbo</artifactId>
                    <groupId>org.apache.dubbo</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        <!-- dubbo end -->

        <!--zookeeper,begin-->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-dependencies-zookeeper-curator5</artifactId>
            <version>${dubbo.version}</version>
            <type>pom</type>
            <exclusions>
                <exclusion>
                    <artifactId>logback-classic</artifactId>
                    <groupId>ch.qos.logback</groupId>
                </exclusion>
                <exclusion>
                    <artifactId>logback-core</artifactId>
                    <groupId>ch.qos.logback</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        <!--zookeeper,end-->

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


        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
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
                <skip>true</skip>
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
                    </goals>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-maven-plugin</artifactId>
            <version>${dubbo.version}</version>
            <executions>
                <execution>
                    <phase>process-sources</phase>
                    <goals>
                        <goal>dubbo-process-aot</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>

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
```


三、子项目增加如下内容：
这样只需要单独对这些模块进行native构建

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <skip>false</skip>
            </configuration>
        </plugin>

        <plugin>
            <groupId>org.graalvm.buildtools</groupId>
            <artifactId>native-maven-plugin</artifactId>
            <configuration>
                <skip>false</skip>
            </configuration>
        </plugin>

        <plugin>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-maven-plugin</artifactId>
            <configuration>
                <mainClass>org.apache.dubbo.springboot.demo.provider.ProviderApplication</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>

```

四、构建：
（1）非native:
mvn clean package

(2)native方式:
mvn clean package native:compile


五、运行：
cd spring-cloud-alibaba-examples/business-service/target
./storage-service

cd sca-graalvm/spring-cloud-alibaba-examples/storage-service/target

./business-service


六、测试：
