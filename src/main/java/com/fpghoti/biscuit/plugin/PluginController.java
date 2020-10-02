package com.fpghoti.biscuit.plugin;

import java.util.ArrayList;
import java.util.Iterator;

import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

public class PluginController implements Iterable<BiscuitPlugin>{

	private PluginManager pluginManager;
	private ArrayList<BiscuitPlugin> plugins;
	
	public PluginController() {
		pluginManager = new DefaultPluginManager();
		plugins = new ArrayList<BiscuitPlugin>();
	}
	
	@Override
	public Iterator<BiscuitPlugin> iterator() {
		return new ArrayList<BiscuitPlugin>(plugins).iterator();
	}
	
	public void registerPlugin(BiscuitPlugin plugin) {
		if(!contains(plugin)) {
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
		pluginManager.stopPlugins();
	}
	
	public void unloadPlugins() {
		pluginManager.unloadPlugins();
	}
	
	public void shutdownPlugins() {
		stopPlugins();
		unloadPlugins();
		plugins.clear();
	}
	
	public boolean contains(BiscuitPlugin plugin) {
		for(BiscuitPlugin pl : plugins) {
			if(plugin.getID().equals(pl.getID())) {
				return true;
			}
		}
		return false;
	}
	
}
