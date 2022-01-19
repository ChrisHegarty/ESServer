module com.example.analysis.plugin.main {
    requires com.example.server;
    requires com.google.common;

    provides com.example.server.plugin.Plugin with com.example.analysis.plugin.AnalysisPluginImpl;
}