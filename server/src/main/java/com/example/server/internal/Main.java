package com.example.server.internal;

import com.example.server.plugin.Plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ServiceLoader;
import java.util.stream.Stream;

public class Main {
    public static void main(String... args) throws Exception {
        Plugin plugin = findPlugin();

        long tokenCount = plugin.count(Stream.of(MESSAGE.split(" ")));
        System.out.println("Found %d tokens in [%s]".formatted(tokenCount, MESSAGE));
    }

    static final String MESSAGE = "Hello there. How are you doin?";

    static Plugin findPlugin() throws Exception {
        URLClassLoader loader = URLClassLoader.newInstance(pluginURLs());
        ServiceLoader<Plugin> serviceLoader = ServiceLoader.load(Plugin.class, loader);
        return serviceLoader.stream().findFirst().orElseThrow().get();
    }

    // For now, just locate plugins based on build output
    static URL[] pluginURLs() throws Exception {
        return new URL[] {
            new URL("file://%s/analysis-plugin/build/libs/analysis-plugin-1.0-SNAPSHOT.jar"
                    .formatted(System.getProperty("user.dir")))
        };
    }
}
