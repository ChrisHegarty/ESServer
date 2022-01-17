package com.example.server.internal;

import com.example.server.plugin.Plugin;

import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Stream;

public class Main {
    public static void main(String... args) throws Exception {
        Plugin plugin = findPlugin();

        long tokenCount = plugin.count(Stream.of(MESSAGE.split(" ")));
        System.out.println("Found %d tokens in [%s]".formatted(tokenCount, MESSAGE));
    }

    static final String MESSAGE = "Hello there. How are you doin?";

    static final Path pluginPath = Path.of(System.getProperty("user.dir")).resolve("analysis-plugin/build/libs/");
    static final Path dependPath = Path.of(System.getProperty("user.dir")).resolve("analysis-plugin/build/deps/libs/");

    // -- Plugin metadata, likely read from the plugin properties file
    static final String pluginModuleName = "ESServer.analysis.plugin.main";
    //--

    static Plugin findPlugin() throws Exception {
        assert Files.notExists(pluginPath);
        assert Files.notExists(dependPath);

        ServiceLoader<Plugin> serviceLoader;
        if (isModularPlugin()) {
            ModuleFinder moduleFinder = ModuleFinder.of(pluginPath, dependPath);
            ModuleLayer parent = ModuleLayer.boot();
            Configuration cf = parent.configuration().resolve(moduleFinder, ModuleFinder.of(), Set.of(pluginModuleName));
            ClassLoader scl = ClassLoader.getSystemClassLoader();
            ModuleLayer layer = parent.defineModulesWithOneLoader(cf, scl);
            serviceLoader = ServiceLoader.load(layer, Plugin.class);
        } else {
            URLClassLoader loader = URLClassLoader.newInstance(pluginURLs());
            serviceLoader = ServiceLoader.load(Plugin.class, loader);
        }
        return serviceLoader.stream().findFirst().orElseThrow().get();
    }

    // For now, just locate plugins based on build output
    static URL[] pluginURLs() throws Exception {
        return new URL[] {
            new URL("file://%s/analysis-plugin/build/libs/analysis-plugin-1.0-SNAPSHOT.jar"
                    .formatted(System.getProperty("user.dir")))
        };
    }

    static boolean isModularPlugin() {
        return pluginModuleName != null && !pluginModuleName.isEmpty();
    }
}
