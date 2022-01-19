package com.example.analysis.plugin;

import com.example.server.plugin.Plugin;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class AnalysisPluginImpl implements Plugin {

    static {
        Module module = AnalysisPluginImpl.class.getModule();
        if (module.isNamed() == false) {
            throw new RuntimeException("Expected named module got: " + module);
        }
    }

    @Override
    public long count(Stream<String> tokens) {
        someGuavaUsage();
        return tokens.count();
    }

    static void assertModuleBoundaries() {
        // uncommenting this line SHOULD produce a compiler/IDE error
        // * Gradle compile will fail as expected - good
        // * IDEA in-IDE compiler will also notice - good
        // unless we add '--add-exports', 'com.example.server/com.example.server.internal=com.example.analysis.plugin.main'
        //com.example.server.internal.Secrets.password();
    }

    // Just some arbitrary use of Guava, to assert compilation
    static void someGuavaUsage() {
        Map items = ImmutableMap.of("coin", 3, "glass", 4, "pencil", 1);
        if (items.size() != 3) {
            throw new RuntimeException("expected 3, got:" + items.size());
        }
        List<String> fruit = Lists.newArrayList("orange", "banana", "kiwi", "mandarin", "quince");
        if (fruit.size() != 5) {
            throw new RuntimeException("expected 5, got:" + fruit.size());
        }
        if (fruit.get(1) != "banana") {
            throw new RuntimeException("expected banana, got:" + fruit.get(1));
        }
    }
}
