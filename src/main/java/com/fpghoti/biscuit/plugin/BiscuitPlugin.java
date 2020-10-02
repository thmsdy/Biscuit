package com.fpghoti.biscuit.plugin;

import java.util.ArrayList;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.logging.BiscuitLog;
import com.github.cage.Cage;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public abstract class BiscuitPlugin extends Plugin {
	
	private String id;
	private String version;
	private String author;
	private String description;

    public BiscuitPlugin(PluginWrapper wrapper) {
        super(wrapper);
        this.id = wrapper.getPluginId();
        this.version = wrapper.getDescriptor().getVersion();
        this.author = wrapper.getDescriptor().getProvider();
        this.description = wrapper.getDescriptor().getPluginDescription();
        Main.getPluginController().registerPlugin(this);
    }
    
    public String getID() {
    	return id;
    }
    
    public String getVersion() {
    	return version;
    }
    
    public String getAuthor() {
    	return author;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public BiscuitPlugin getPlugin() {
    	return this;
    }
    
    public ArrayList<Biscuit> getBiscuits() {
		return Main.getBiscuits();
	}
    
    public Cage getCage() {
    	return Main.getCage();
    }
    
    public AudioPlayerManager getPlayerManager() {
    	return Main.getPlayerManager();
    }
    
    public BiscuitLog getLogger() {
    	return Main.getLogger();
    }

}