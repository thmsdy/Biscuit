package com.fpghoti.biscuit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.fpghoti.biscuit.commands.CommandManager;

public class PluginCore extends JavaPlugin{

	public static PluginCore plugin;

	public void onEnable(){
		plugin = this;
		Main.isPlugin = true;
		String[] args = {};
		Main.main(args);
	}

	public void onDisable() {
		Main.shutdown();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(!p.hasPermission("biscuit.admin")) {
				return false;
			}
		}
		return CommandManager.dispatch(label,args);
	}

}