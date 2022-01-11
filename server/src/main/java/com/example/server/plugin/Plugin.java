package com.example.server.plugin;

import java.util.stream.Stream;

public interface Plugin {

    long count(Stream<String> tokens);
}
