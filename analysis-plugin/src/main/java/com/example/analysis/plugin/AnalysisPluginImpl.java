package com.example.analysis.plugin;

import com.example.server.plugin.Plugin;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class AnalysisPluginImpl implements Plugin {
    @Override
    public long count(Stream<String> tokens) {
        someGuavaUsage();
        return tokens.count();
    }

    static void assertModuleBoundaries() {
        // uncommenting this line should produce a compiler/IDE error
        // unless we add '--add-exports', 'com.example.server/com.example.server.internal=ESServer.analysis.plugin.main'
        //com.example.server.internal.Secrets.password();
    }

    // Just some arbitrary use of Guava, to assert compilation
    static void someGuavaUsage() {
        Map items = ImmutableMap.of("coin", 3, "glass", 4, "pencil", 1);
        items.entrySet().stream().forEach(System.out::println);

        List<String> fruits = Lists.newArrayList("orange", "banana", "kiwi",
                "mandarin", "date", "quince");
        fruits.stream().forEach(System.out::println);
    }
}
