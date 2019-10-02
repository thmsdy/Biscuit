package com.fpghoti.biscuit;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.slf4j.Logger;

import com.fpghoti.biscuit.commands.CommandManager;
import com.fpghoti.biscuit.timer.BiscuitTimer;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

public class Biscuit {
	
	private JDA jda;
	private Logger logger;
	private CommandManager commandManager;
	private Timer timer;
	private List<BiscuitTimer> timers;
	
	public Biscuit(JDA jda, Logger log) {
		this.jda = jda;
		this.logger = log;
		
		commandManager = new CommandManager();
		timer = new Timer();
		timers = new ArrayList<BiscuitTimer>();
	}
	
	public void log(String message) {
		logger.info(message);
	}

	public void warn(String message) {
		logger.warn(message);
	}
	
	public void error(String message) {
		logger.error(message);
	}
	
	public JDA getJDA() {
		return jda;
	}
	
	public CommandManager getCommandManager() {
		return commandManager;
	}
	
	public void say(TextChannel channel, String message) {
		channel.sendMessage(message).queue();
	}
	
	
	public void loadTimers() {
		timer.cancel();
		timer = new Timer();
		for(BiscuitTimer t : timers) {
			timer.schedule(t,t.getDelay(), t.getPeriod());
		}
	}
	
	public void addTimer(BiscuitTimer bt) {
		timers.add(bt);
	}
	
	public void removeTimer(BiscuitTimer bt) {
		timers.remove(bt);
	}

	
	
}
