package com.fpghoti.biscuit.user;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.PluginCore;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class PreUser {

	public static CopyOnWriteArrayList<PreUser> users = new CopyOnWriteArrayList<PreUser>();

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

	public PreUser(User user) {
		this.user = user;
		this.token = null;
		this.done = false;
		this.timeLeft = PropertiesRetrieval.noCaptchaKickTime() + 1;
		users.add(this);
	}

	public User getUser() {
		return this.user;
	}

	public String getToken() {
		return this.token;
	}

	//Going to use custom string gen for more char types in captcha
	public void genToken() {
		//token = Main.getBiscuit().getCage().getTokenGenerator().next();
		token = Util.randomString(6);
	}

	public void setDone() {
		this.done = true;
	}

	public void decrementTime() {
		if(!shareGuild()) {
			remove();
			return;
		}

		if(!done) {
			if(PropertiesRetrieval.noCaptchaKick()) {
				timeLeft = timeLeft - 1;
				if(timeLeft <= 0) {
					for(Guild g : getGuilds()) {
						Main.log.info(user.getName() + " " + user.getAsMention() + " waited too long to complete the captcha! Kicking...");
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
	}

}
