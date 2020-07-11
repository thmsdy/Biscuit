package com.fpghoti.biscuit;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import org.slf4j.Logger;

import com.fpghoti.biscuit.captcha.BCage;
import com.fpghoti.biscuit.commands.CommandManager;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.fpghoti.biscuit.timer.BiscuitTimer;
import com.fpghoti.biscuit.user.PreUser;
import com.github.cage.Cage;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class Biscuit {

	private JDA jda;
	private Logger logger;
	private CommandManager commandManager;
	private Timer timer;
	private List<BiscuitTimer> timers;
	private File captchaDir;
	private Cage cage;
	private HashMap<String, Integer> inviteUses;

	public Biscuit(JDA jda, Logger log) {
		this.jda = jda;
		this.logger = log;

		commandManager = new CommandManager();
		timer = new Timer();
		timers = new ArrayList<BiscuitTimer>();
		if(!Main.isPlugin) {
			captchaDir = new File("captcha");
		}else {
			captchaDir = new File(PluginCore.plugin.getDataFolder(), "captcha");
		}
		captchaDir.mkdir();
		cage = new BCage();
		wipeCaptchaDir();
		loadPreUsers();
		inviteUses = new HashMap<String, Integer>();
		if(canManageServer() && PropertiesRetrieval.checkJoinInvite()) {
			for(Guild g : jda.getGuilds()) {
				g.retrieveInvites().queue((invs) -> {
					indexInvites(invs);
				});
			}
		}
	}

	public boolean canManageServer() {
		for(Guild g : jda.getGuilds()) {
			if(g.getSelfMember().hasPermission(Permission.MANAGE_SERVER)) {
				return true;
			}
		}
		return false;
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

	public void wipeCaptchaDir() {
		Main.log.info("Wiping captcha files...");
		boolean good = true;
		File[] files = captchaDir.listFiles();
		for (File file : files){
			if (!file.delete()){
				good = false;
				Main.log.error("Could not remove captcha file: " + file);
			}
		}
		if(good) {
			Main.log.info("All captcha files successfully removed!");
		}else {
			Main.log.error("Some captcha file(s) could not be removed!");
		}
	}

	public Cage getCage() {
		return this.cage;
	}
	
	public ArrayList<TextChannel> getCaptchaLogChannels() {
		ArrayList<TextChannel> ch = new ArrayList<TextChannel>();
		for(Guild g : jda.getGuilds()) {
			for(TextChannel t : g.getTextChannels()) {
				if(t.getName().equalsIgnoreCase(PropertiesRetrieval.getCaptchaLogChannel())) {
					ch.add(t);
				}
			}
		}
		return ch;
	}
	
	public void captchaLog(String msg) {
		if(PropertiesRetrieval.logCaptcha()) {
			for(TextChannel t : getCaptchaLogChannels()) {
				t.sendMessage(msg).queue();
			}
		}
	}

	private void loadPreUsers() {
		for(Guild g : jda.getGuilds()) {
			for(Member m : g.getMembers()) {
				User u = m.getUser();
				if(!PreUser.preUserExists(u)) {
					if(m.getRoles().size() == 1) {
						for(Role role : m.getRoles()){
							if(role.getName().equalsIgnoreCase(PropertiesRetrieval.getDefaultRole())){
								PreUser.users.add(new PreUser(u));
							}
						}
					}
				}
			}
		}
	}


	private void indexInvites(List<Invite> invs) {
		for(Invite i : invs) {
			String code = i.getCode();
			int uses = i.getUses();
			inviteUses.put(code, uses);
		}
	}

	public HashMap<String, Integer> getInviteUses(){
		return inviteUses;
	}

	public void setInviteUses(HashMap<String, Integer> c) {
		inviteUses = c;
	}

	public void clearInviteUses() {
		inviteUses.clear();
	}

}
