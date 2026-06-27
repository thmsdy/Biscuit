package com.fpghoti.biscuit.plugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;

public class PluginController implements Iterable<BiscuitPlugin> {

    private PluginManager pluginManager;
    private ArrayList<BiscuitPlugin> plugins;

    public PluginController() {
        pluginManager = new DefaultPluginManager();
        plugins = new ArrayList<>();
    }

    @Override
    public Iterator<BiscuitPlugin> iterator() {
        return new ArrayList<>(plugins).iterator();
    }

    public void registerPlugin(BiscuitPlugin plugin) {
        if (!contains(plugin)) {
            plugins.add(plugin);
        }
    }

    public void loadPlugins() {
        pluginManager.loadPlugins();
    }

    public void startPlugins() {
        pluginManager.startPlugins();
    }

    public void stopPlugins() {
        List<PluginWrapper> startedPlugins = new ArrayList<>(pluginManager.getStartedPlugins());

        for (PluginWrapper plugin : startedPlugins) {
            try {
                pluginManager.stopPlugin(plugin.getPluginId());
            } catch (Exception e) {
                System.err.println("Failed to stop plugin: " + plugin.getPluginId());
                e.printStackTrace();
            }
        }
    }

    public void unloadPlugins() {
        List<PluginWrapper> loadedPlugins = new ArrayList<>(pluginManager.getPlugins());

        for (PluginWrapper plugin : loadedPlugins) {
            try {
                pluginManager.unloadPlugin(plugin.getPluginId());
            } catch (Exception e) {
                System.err.println("Failed to unload plugin: " + plugin.getPluginId());
                e.printStackTrace();
            }
        }
    }

    public void shutdownPlugins() {
        stopPlugins();
        unloadPlugins();

        plugins.clear();
    }

    public boolean contains(BiscuitPlugin plugin) {
        for (BiscuitPlugin pl : plugins) {
            if (plugin.getID().equals(pl.getID())) {
                return true;
            }
        }
        return false;
    }
}