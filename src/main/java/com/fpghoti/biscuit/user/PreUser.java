package com.fpghoti.biscuit.user;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.PluginCore;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.github.cage.Cage;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class PreUser {

	public static CopyOnWriteArrayList<PreUser> testusers = new CopyOnWriteArrayList<PreUser>();
	public static CopyOnWriteArrayList<PreUser> users = new CopyOnWriteArrayList<PreUser>();

	public static PreUser getTestUser(User user) {
		for(PreUser u : testusers) {
			if(u.getUser().getId().equals(user.getId())) {
				return u;
			}
		}
		return null;
	}
	
	public static PreUser getPreUser(User user) {
		for(PreUser u : users) {
			if(u.getUser().getId().equals(user.getId())) {
				return u;
			}
		}
		return null;
	}

	public static boolean preUserExists(User user) {
		return getPreUser(user) != null;
	}

	private User user;
	private String token;
	private int timeLeft;
	private boolean done;
	private boolean test;

	public PreUser(User user) {
		this(user, false);
	}
	
	public PreUser(User user, boolean test) {
		this.test = test;
		this.user = user;
		this.token = null;
		this.done = false;
		this.timeLeft = PropertiesRetrieval.noCaptchaKickTime() + 1;
		if(!test) {
			users.add(this);
		}
	}

	public User getUser() {
		return this.user;
	}

	public String getToken() {
		return this.token;
	}

	public void genToken() {
		Cage cage = Main.getBiscuit().getCage();
		token = cage.getTokenGenerator().next();
	}

	public void setDone() {
		this.done = true;
	}

	public void decrementTime() {
		if(!shareGuild()) {
			remove();
			return;
		}
		
		if(test) {
			return;
		}

		if(!done) {
			if(PropertiesRetrieval.noCaptchaKick()) {
				timeLeft = timeLeft - 1;
				if(timeLeft <= 0) {
					for(Guild g : getGuilds()) {
						Main.log.info(user.getName() + " " + user.getAsMention() + " waited too long to complete the captcha. Kicking...");
						Main.getBiscuit().captchaLog("``" + user.getName() +"`` " + user.getAsMention() + " waited too long to complete the captcha! Kicking...");
						g.kick(user.getId()).queue();
						remove();
					}
				}
			}
		}

	}

	public boolean shareGuild() {
		JDA jda = Main.getBiscuit().getJDA();
		for(Guild g : jda.getGuilds()) {
			if(g.isMember(user)){
				return true;
			}
		}
		return false;
	}

	public ArrayList<Guild> getGuilds(){
		ArrayList<Guild> guilds = new ArrayList<Guild>();
		JDA jda = Main.getBiscuit().getJDA();
		for(Guild g : jda.getGuilds()) {
			if(g.isMember(user)){
				guilds.add(g);
			}
		}
		return guilds;
	}

	public void remove() {
		setDone();
		Main.log.info("Removing captcha data for user " + user.getName() + " " + user.getAsMention());
		File captcha;
		if(!Main.isPlugin) {
			captcha = new File("captcha/" + user.getId() + ".jpg");
		}else {
			captcha = new File(PluginCore.plugin.getDataFolder(), "captcha/" + user.getId() + ".jpg");
		}
		token = null;
		captcha.delete();
		users.remove(this);
		testusers.remove(this);
	}

}
