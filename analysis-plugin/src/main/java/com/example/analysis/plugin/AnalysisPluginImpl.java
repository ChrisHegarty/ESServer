package com.example.analysis.plugin;

import com.example.server.plugin.Plugin;

import java.util.stream.Stream;

public class AnalysisPluginImpl implements Plugin {
    @Override
    public long count(Stream<String> tokens) {
        sneaky();
        return tokens.count();
    }

    private static void sneaky() {
        char[] password = com.example.server.internal.Secrets.password();
        System.out.println("retrieved server password:" + password);
    }
}
