# boot骨架简介

## 目录规范
```
project
│   README.md  # 项目说明文档
│   profiles   # 环境变量相关配置   
│   bin   # 应用脚本
└─ src
     │   main
     │   │   assembly # assembly 插件配置（打包）
     │   │   java # 应用程序代码
     │   │   reources # 资源文件
     │   │      │  static #静态资源文件  
     │   │      │  public #公开资源      
     │   │      │  templates #模板文件      
     │   test
     │   │  java #测试类
     │   │  resources #测试资源文件           
```
## springboot 开发须知


### 配置加载顺序

* 当没有配置外部配置时  
1. file:./config/
2. file:./
3. classpath:/config/
4. classpath:/

* 已配置外部配置文件  
即指定 spring.config.location,
可以通过操作系统环境变量,配置文件环境变量,启动参数指定  
1. file:./custom-config/
2. classpath:custom-config/
3. file:./config/
4. file:./
5. classpath:/config/
6. classpath:/

* 配置实践 

spring.profiles.active=指定激活配置  
如使用maven 占位符形式 spring.profiles.active=@profiles.active@
需在 pom.xml build/resources/resource 添加
```xml
            <filtering>true</filtering>
```

### 统一结果返回
* 方式一 通过ResponseBodyAdvice 实现(推荐)
```java

    /**
     * 统一结果处理
     */
    @RestControllerAdvice(basePackages = "com.ncs.single.boot.controller")
    public static class ResponseHandler implements ResponseBodyAdvice<Object> {

        @Override
        public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
            return true;
        }

        @Override
        public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
            if (body == null) {
                return ResultBuilder.success();
            }
            if (body instanceof Result) {
                return body;
            }

            if (body instanceof ResponseEntity) {
                return body;
            }
            if (body instanceof MapResultBuilder) {
                body = ((MapResultBuilder) body).build();
            }
            return ResultBuilder.success(body);
        }

    }
```
* 方式二:统一返回结果包装

```java
    @RequestMapping("result")
    @ResponseBody
    public Result result(){
        return ResultBuilder.success("hello world");
    }
```

### 统一异常处理
* 统一异常处理器
```java
代码参见:
1. 异常处理器:com.ncs.single.boot.exception.SystemExceptionHandler
2.springboot mvc配置,将自定义异常拦截器,添加到springmvc dispatcherservlet中
备注:慎用@EnableWebMvc 会覆盖 springboot 自动配置
实现:WebMvcConfigurer 添加 @Configuration ,可以实现自定义扩展,并不会覆盖自动配置

```
* 注解@ExceptionHandler
本质上和方式一一样 
```java
代码参见:com.ncs.single.boot.config.ExceptionHandler
```
* 使用 springboot配置默认异常配置,覆盖默认异常控制器
```java
默认自动异常装配:org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
自动装配的默认控制器:
org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
,可通过注入 ErrorController实现统一结果返回
参见:com.ncs.single.boot.controller.BootErrorController

#禁用springboot Whitelabel,ErrorTemplateMissingCondition 异常
server.error.whitelabel.enabled=false

```

根据喜好,选择一种即可

* 异常页面查找优先级

1. classpath:templates/error
2. 静态资源查找
3. springboot 内置异常页面
具体参见:ErrorMvcAutoConfiguration

### 首页配置
```java
 //实现 WebMvcConfigurer,添加view映射
 @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        String index = "index";
        registry.addViewController("/").setViewName(index);
        registry.addViewController("/index").setViewName(index);
    }

```
### 静态资源介绍

* springboot 默认的静态资源
/** 查找顺序  
1. classpath:/META-INF/resources/
2. classpath:/static/
3. classpath:/public/

### 自动装配原理
主要依赖 xx-autoconfigure 和xxx-starter
*  META-INF/spring.factories 包含所有自动装配的类
*  META-INF/ 也包含了相关配置提示,方便ide代码提示
* org.springframework.boot.autoconfigure.condition 条件注解实现动态条件注入   

装配注解简介:  
注解 | 作用
- | :-: 
@ConditionalOnJava | java版本条件 
@ConditionalOnBean | bean存在满足
@ConditionalOnMissingBean | bean不存在满足
@ConditionalOnExpression | el表达条件
@ConditionalOnClass | 存在类
@ConditionalOnSingleCandidate | bean只存在一个或该bean被注解@Primary
@ConditionalOnProperty | 配置存在
@ConditionalOnResource | 资源存在
@ConditionalOnWebApplication | web环境
@ConditionalOnNotWebApplication | 非web环境
@ConditionalOnJndi | jndi配置存在
@ConditionalOnCloudPlatform | 运行在某个云平台



### spring boot的日志处理
* 使用slf4j+logback  
springboot的日志依赖,可以在spring-boot-dependencies 中查看
```java
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring‐boot‐starter‐logging</artifactId>
</dependency>
```
使用该依赖包,可以将各种主流的日志实现桥接到sl4fj->logback,保证日志正常数据

* springboot的日志配置文件

默认是配置 classpath下的logback-spring.xml,不要用 logback.xml,  
logback.xml logback会优先加载
logback-spring.xml 可以使用springboot的配置
如: <springProfile name="dev">  
<springProperty name="LOG_HOME" source="logging.path" defaultValue="./logs"/>

### springboot 打包
* 默认打包 
默认打包,所有资源及依赖包都是在可运行的jar中,打包方式:
```xml
<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
```
**war支持**

1. 修改启动类,支持war启动
```java
public class SeedBootApplication  extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SeedBootApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SeedBootApplication.class);
    }


}
```
2. 修改打包方式为 war
3. 修改内置tomcat 为 provided
```xml
        <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-tomcat</artifactId>
			<scope>provided</scope>
		</dependency>
```
* 分离依赖打包(外部插件打包，推荐)
1. pom 配置
```xml
  <plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                 <!--分离lib-->
                <includes>
                    <include>
                        <groupId>nothing</groupId>
                        <artifactId>nothing</artifactId>
                     </include>
                  </includes>
                </configuration>
	            <executions>
	                <execution>
	                    <goals>
	                        <goal>repackage</goal>
	                    </goals>
	                </execution>
	            </executions>
            </plugin>
            <!-- 将分离的依赖,指定到打包后的清单文件(MANIFEST.MF)中 -->
             <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <configuration>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                            <classpathPrefix>lib/</classpathPrefix>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
            <!-- copy 项目依赖到target/lib中 -->
            <plugin>
                  <groupId>org.apache.maven.plugins</groupId>
                  <artifactId>maven-dependency-plugin</artifactId>
                  <executions>
                     <execution>
                        <id>copy-lib</id>
                        <phase>package</phase>
                        <goals>
                           <goal>copy-dependencies</goal>
                        </goals>
                        <configuration>
                           <outputDirectory>target/lib</outputDirectory>
                           <excludeTransitive>false</excludeTransitive>
                           <stripVersion>false</stripVersion>
                           <includeScope>runtime</includeScope>
                        </configuration>
                     </execution>
                  </executions>
            </plugin>
            <!-- 自定义打包-->
            <plugin>
                        <artifactId>maven-assembly-plugin</artifactId>
                        <configuration>
                            <appendAssemblyId>false</appendAssemblyId>
                            <finalName>${project.artifactId}-${project.version}-${profiles.active}</finalName>
                            <outputDirectory>${build.directory}</outputDirectory>
                            <descriptors>
                                <descriptor>src/main/assembly/assembly.xml</descriptor>
                            </descriptors>
                        </configuration>
                        <executions>
                            <execution>
                                <id>make-assembly</id>
                                <phase>package</phase>
                                <goals>
                                    <goal>single</goal>
                                </goals>
                            </execution>
                        </executions>
            </plugin>
		</plugins>
```
2. 打包后的清单  
.
├── config # 配置文件
├── lib    #依赖包
├── xx.jar # 主程序包
├── jar.sh #程序脚本 命令参数:Usage: {start|stop|isRun|restart}

* springboot 实验特性(spring-boot-thin-maven-plugin)打包
```xml
 <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <dependencies>
                    <dependency>
                        <!-- 加入该配置，使用maven package，依赖jar不会打包在项目最终jar文件内
                        启动jar 加入参数  -Dthin.root=. -jar
                        -->
                        <groupId>org.springframework.boot.experimental</groupId>
                        <artifactId>spring-boot-thin-layout</artifactId>
                        <version>1.0.21.RELEASE</version>
                    </dependency>
                </dependencies>
            </plugin>
            <plugin>
                <!-- 加入该配置，maven package执行时会在target目录整理好依赖包 -->
                <groupId>org.springframework.boot.experimental</groupId>
                <artifactId>spring-boot-thin-maven-plugin</artifactId>
                <version>1.0.21.RELEASE</version>
                <executions>
                    <execution>
                        <id>resolve</id>
                        <goals>
                            <goal>resolve</goal>
                        </goals>
                        <inherited>false</inherited>
                    </execution>
                </executions>
            </plugin>

        </plugins>
```

备注 也可结合 assembly 构建部署包，启动加入参数：-Dthin.root=. -jar
打包后的清单   
target/thin/root
├── repository # 依赖包
├── xx.jar # 主程序包


* 可执行程序打包

pom 配置
```xml
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                    <configuration>
                    <executable>true</executable>
                </configuration>
            </plugin>
```
具体流程参见: [springboot官方文档](https://docs.spring.io/spring-boot/docs/2.1.11.RELEASE/reference/html/deployment-install.html#deployment-script-customization)

原理简介:即在jar包前部分嵌入springboot自带的启动脚本,脚本内容可以使用文本文件打开,executable jar不可以用压缩包打开  

* layout ZIP打包(外置classpath)

**assembly 配置**

```xml
<assembly xmlns="http://maven.apache.org/ASSEMBLY/2.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/ASSEMBLY/2.0.0 http://maven.apache.org/xsd/assembly-2.0.0.xsd">
<id>package</id>
<formats>
    <format>tar.gz</format>
</formats>
<includeBaseDirectory>true</includeBaseDirectory>
<fileSets>

   <!-- claspath 配置 复制到config-->
    <fileSet>
        <directory>${build.outputDirectory}</directory>
        <includes>
            <include>*.properties</include>
            <include>*.yml</include>
        </includes>
        <outputDirectory>loader/config</outputDirectory>
    </fileSet>
    <!-- 激活配置到 config-->
     <fileSet>
        <directory>${basedir}/profiles/${profiles.active}</directory>
        <outputDirectory>loader/config</outputDirectory>
    </fileSet>

     <fileSet>
        <directory>${basedir}/src/main/resources</directory>
        <includes>
            <include>mybatis/**</include>
            <include>public/**</include>
            <include>static/**</include>
            <include>templates/**</include>
            <include>logback-spring.xml</include>
        </includes>
        <outputDirectory>loader</outputDirectory>
    </fileSet>
    <!-- 程序脚本 复制到/ 并具有可执行权限 -->
    <fileSet>
        <directory>${basedir}/bin</directory>
        <includes>
            <include>*.sh</include>
        </includes>
        <fileMode>0755</fileMode>
        <outputDirectory>/</outputDirectory>
    </fileSet>

    <!-- 依赖包 -->
    <fileSet>
        <directory>${build.directory}/lib</directory>
        <outputDirectory>loader/lib</outputDirectory>
        <fileMode>0666</fileMode>
    </fileSet>
    <!-- 主程序 -->
    <fileSet>
        <directory>${build.directory}</directory>
        <outputDirectory>/</outputDirectory>
        <includes>
            <include>*.jar</include>
        </includes>
        <fileMode>0666</fileMode>
    </fileSet>
</fileSets>
</assembly>
```

**pom打包配置**  
<font color=red>**profiles配置(简介):**</font>  
1. dev（local） 打包正常包含resources文件,以便ide main方式启动
2. prod，test,qa 打包去除jar包中resources 除配置文件外的其他文件
3. assembly 打包时copy src/main/resources 目录下的相关文件到目录下loader

```xml
  <profiles>
    <!--
    dev 开发环境
    test 测试环境
    prod 生产环境
    -->
          <profile>
      <id>dev</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <profiles.active>dev</profiles.active>
      </properties>
              <build>
                     <resources>
      <resource>
        <directory>src/main/resources</directory>
          <filtering>true</filtering>
      </resource>
      <resource>
        <directory>${project.basedir}/profiles/${profiles.active}</directory>
      </resource>
    </resources>
              </build>
    </profile>

    <profile>
      <id>test</id>
      <properties>
        <profiles.active>test</profiles.active>
      </properties>
               <build>
                     <resources>
      <resource>
        <directory>src/main/resources</directory>
          <filtering>true</filtering>
          <excludes>
              <exclude>mybatis</exclude>
               <exclude>public</exclude>
               <exclude>static</exclude>
               <exclude>templates</exclude>
               <exclude>logback-spring.xml</exclude>
          </excludes>
          <includes>
              <include>application.properties</include>
          </includes>
      </resource>

    </resources>
              </build>
    </profile>

    <profile>
      <id>prod</id>
      <properties>
        <profiles.active>prod</profiles.active>
      </properties>
       <build>
           <resources>
      <resource>
        <directory>src/main/resources</directory>
          <filtering>true</filtering>
          <excludes>
              <exclude>mybatis</exclude>
               <exclude>public</exclude>
               <exclude>static</exclude>
               <exclude>templates</exclude>
               <exclude>logback-spring.xml</exclude>
          </excludes>
          <includes>
              <include>application.properties</include>
          </includes>
      </resource>

    </resources>
              </build>
    </profile>
  </profiles>

  <build>
        <finalName>${project.artifactId}-${project.version}-${profiles.active}</finalName>
        <plugins>

			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <layout>ZIP</layout>
                      <includes>
                        <include>
                            <groupId>nothing</groupId>
                            <artifactId>nothing</artifactId>
                        </include>
                    </includes>
                </configuration>
	            <executions>
	                <execution>
	                    <goals>
	                        <goal>repackage</goal>
	                    </goals>
	                </execution>
	            </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <executions>
                    <execution>
                        <id>copy-lib</id>
                        <phase>package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>target/lib</outputDirectory>
                            <excludeTransitive>false</excludeTransitive>
                            <stripVersion>false</stripVersion>
                            <includeScope>runtime</includeScope>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

             <plugin>
                        <artifactId>maven-assembly-plugin</artifactId>
                        <configuration>
                            <appendAssemblyId>false</appendAssemblyId>
                            <outputDirectory>${build.directory}</outputDirectory>
                            <descriptors>
                                <descriptor>src/main/assembly/assembly-layoutzip.xml</descriptor>
                            </descriptors>
                        </configuration>
                        <executions>
                            <execution>
                                <id>make-assembly</id>
                                <phase>package</phase>
                                <goals>
                                    <goal>single</goal>
                                </goals>
                            </execution>
                        </executions>
            </plugin>
             </plugins>
</build>
```
<font color=red>注意增加启动参数:</font> <font color=black>**-Dloader.path=loader**</font>

**打包目录结构**   .
 ├── boot-1.0-SNAPSHOT-test.jar
 ├── jar.sh # boot脚本
 └── loader
     ├── config # 配置目录
     │   ├── application.properties
     │   └── application-test.properties
     ├── lib # 依赖包
     ├── logback-spring.xml # 日志目录
     ├── mybatis #mybatis目录
     ├── public # boot resources相关目录
     ├── static
     └── templates
## springboot打包部署分析
### 打包方式简介
1. jar包方式
    修改不太方便,但也可外置模板,静态资源,配置实现动态修改,
    也可用热部署解决
    jar 包结构:  
    .
    ├── BOOT-INF
    │   ├── classes
    │   │   ├── application-dev.properties
    │   │   ├── application.properties
    │   │   ├── logback-spring.xml
    │   │   ├── public
    │   │   ├── static
    │   │   └── templates
    │   └── lib #相关依赖包
    ├── META-INF  # 打包相关清单文件
    │   ├── MANIFEST.MF
    │   └── maven
    └── org
        └── springframework
            └── boot
                └── loader
                    ├── archive # springboot 启动相关类
   **jar包清单文件**

```yaml
Manifest-Version: 1.0
Archiver-Version: Plexus Archiver
Built-By: dushitaoyuan
Start-Class: com.ncs.single.boot.SeedBootApplication
Spring-Boot-Classes: BOOT-INF/classes/
Spring-Boot-Lib: BOOT-INF/lib/
Spring-Boot-Version: 2.2.2.RELEASE
Created-By: Apache Maven 3.3.9
Build-Jdk: 1.8.0_211
Main-Class: org.springframework.boot.loader.JarLauncher

```
内置容器调优需参考官方配置
2. war 方式 web容器部署
依靠具体web容器参数配置,局部资源替换方便  
war包结构:  
.
├── META-INF # 打包相关清单文件
│   ├── MANIFEST.MF
│   ├── maven 
├── org
│   └── springframework
│       └── boot
│           └── loader
│               ├── archive # springboot 启动相关类
└── WEB-INF # 传统war包结构
    ├── classes
    │   ├── application-dev.properties
    │   ├── application.properties
    │   ├── logback-spring.xml
    │   └── templates
    ├── lib #相关依赖包
    └── lib-provided #provided包
   
    

**war包清单文件**
```yaml
Manifest-Version: 1.0
Archiver-Version: Plexus Archiver
Built-By: dushitaoyuan
Start-Class: com.ncs.single.boot.SeedBootApplication
Spring-Boot-Classes: WEB-INF/classes/
Spring-Boot-Lib: WEB-INF/lib/
Spring-Boot-Version: 2.2.2.RELEASE
Created-By: Apache Maven 3.3.9
Build-Jdk: 1.8.0_211
Main-Class: org.springframework.boot.loader.WarLauncher

```

**但是随着容器化部署,自动化部署的普及,部分应用已经可以实现秒级发布,动态修改已发布的程序更显鸡肋.**

###  启动原理分析
* 部署包中启动类相关配置
```yaml
Start-Class: com.ncs.single.boot.SeedBootApplication
Main-Class: org.springframework.boot.loader.JarLauncher
```
**Launcher分类 **

1. JarLauncher
jar 包启动类  
2. WarLauncher 
war包启动类  
3. PropertiesLauncher 
配置启动 类 其实就是扩展了JarLauncher   
配合 启动参数:-Dloader.path=xxx 外置classpath地址
打包配置参见:springboot打包 

* 打包启动原理
1. springboot-maven-plugin 通过jarwritter构造springboot的包格式
   
    **writter主要流程**
    
    * repackage
    * 构造清单文件(Start-Class,Main-Class)
    * 写入MANIFEST.MF
    * 写入依赖到lib
    * 将springboot启动相关类写入程序包
    
2. spring-boot-loader 负责将指定的包格式启动  
    **启动主要包含以下几步**

    * 运行springboot启动类(Launcher)
    * 具体Launcher实现加载外部依赖包(archives 归档文件)
    * 按照配置优先级加载配置
    * 初始化bean容器,servlet容器



## springboot 适用场景

* 纯后台应用(纯接口应用,纯服务应用,前后端分离应用)
* 扩展性要求高应用(后续可转分布式,轻松对接各种应用场景),较mvc快

## 官方文档地址

1. [springboot代码仓库地址](https://github.com/spring-projects/spring-boot)  
2. [springboot官网地址](https://spring.io/projects/spring-boot/)  
3. [spring-boot-maven-plugin打包插件文档地址](https://docs.spring.io/spring-boot/docs/2.1.11.RELEASE/maven-plugin/)  
4. [springboot启动源码](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project/spring-boot-tools/spring-boot-loader)



















