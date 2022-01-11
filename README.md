
This project demonstrates an issue with Intellij IDEA, whereby the in-IDE internal compiler does not match that of the
Gradle build. The in-IDE internal compiler is not respecting and identifying the Java Module boundaries of a dependent
Intellij module (generated from a Gradle subproject within the same root project).

This project a minimal reproducer that roughly represents a layout and configuration of a much larger project, namely
the Modular Elasticsearch server. Elasticsearch has much custom gradle plugins and configuration, but the behaviour
encountered by this minimal reproducer project is substantially the same. 

Version: IntelliJ IDEA 2021.3.1 (Ultimate Edition)

#### Reproducer

The source contains two gradle subprojects:

1. server: a minimal server that finds and loads plugins. The server creates and loads the plugins in their own
classloader (a URLClassLoader). The server is a Java Module; it has one exported package, which exports the `Plugin`
interface, and one non-exported (concealed) package. The concealed package contains sensitive secrets which should not
be accessible to code outside the server module. For this reason, the server module is deployed on the module-path.

2. analysis-plugin: A trivial plugin implementation that counts tokens in a given stream of strings (implementation 
details are not relevant). From a project perspective, analysis-plugin has a compile dependency on server (since it
provides a concrete implementation of the `Plugin` interface).

The analysis-plugin has "bad" code tries to access the servers secrets (from within the server's concealed package).
This correctly fails to build from Gradle (since the code is not accessible from the plugin), but the IDE does not
show any "red squiggly" lines for this code, since the imported analysis-plugin IDEA module has a "compile" dependency 
on server - as is generated during the Gradle import. The in-IDE internal compile would seem to put the server classes
on the compile classpath, when performing the in-IDE analysis, but since server is a Java module it should be on the 
module-path (rather than on the classpath), which would then result in the in-IDE compiler detecting and reporting Java
module boundaries.

To reproduce, simply:
```
$ git clone https://github.com/ChrisHegarty/ESServer.git
...
$ cd ESServer
$ ./gradlew build
```

You should see the following output:
```
> Task :analysis-plugin:compileJava FAILED
/Users/chegar/git/examples/ESServer/analysis-plugin/src/main/java/com/example/analysis/plugin/AnalysisPluginImpl.java:15: error: package com.example.server.internal is not visible
char[] password = com.example.server.internal.Secrets.password();
^
(package com.example.server.internal is declared in module com.example.server, which does not export it)
1 error
```

Open the project in the IDE, and navigate to AnalysisPluginImpl.java. The IDE shows no issues.

What we expect is that the IDE would have "red squiggly" lines under `com.example.server.internal.Secrets.password`
reporting that _Secrets_ is not accessible from outside the server module.