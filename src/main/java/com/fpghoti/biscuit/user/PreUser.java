package com.fpghoti.biscuit.user;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.PluginCore;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.util.PermUtil;
import com.github.cage.Cage;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class PreUser {

	public static CopyOnWriteArrayList<PreUser> testusers = new CopyOnWriteArrayList<PreUser>();

	public static PreUser getTestUser(User u) {
		for(PreUser pu : testusers) {
			if(pu.getUser().getId().equals(u.getId())) {
				return pu;
			}
		}
		return null;
	}

	public static boolean hasTestUser(User u) {
		return getTestUser(u) != null;
	}

	private User user;
	private String token;
	private int timeLeft;
	private boolean done;
	private boolean test;
	private Biscuit biscuit;

	public PreUser(User user, Biscuit biscuit) {
		this(user, biscuit, false);
	}

	public PreUser(User user, Biscuit biscuit, boolean test) {
		this.test = test;
		this.user = user;
		this.token = null;
		this.biscuit = biscuit;
		this.done = false;
		this.timeLeft = biscuit.getProperties().noCaptchaKickTime() + 1;
		if(!test) {
			biscuit.addPreUser(this);
		}
	}

	public Biscuit getBiscuit() {
		return biscuit;
	}

	public User getUser() {
		return this.user;
	}

	public String getToken() {
		return this.token;
	}

	public void genToken() {
		Cage cage = biscuit.getCage();
		token = cage.getTokenGenerator().next();
	}

	public void setDone() {
		this.done = true;
	}

	public int getTimeLeft() {
		return timeLeft;
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
			if(biscuit.getProperties().noCaptchaKick()) {
				timeLeft = timeLeft - 1;
				if(timeLeft <= 0) {
					Member m = biscuit.getGuild().getMember(user);
					biscuit.log(user.getName() + " " + user.getAsMention() + " waited too long to complete the captcha. Kicking...");
					biscuit.captchaLog("``" + user.getName() +"`` " + user.getAsMention() + " waited too long to complete the captcha! Kicking...");

					if(m != null && m.getRoles().size() == 1 && PermUtil.hasDefaultRole(m) && !PermUtil.hasRewardRole(m)) {
						if(biscuit.getProperties().dmBeforeKick()) {
							String msg = "You did not complete the captcha in **" 
									+ " " + biscuit.getGuild().getName() + "**! If you believe this is a mistake, rejoin the server"
									+ ", complete the captcha, and inform an administrator.";
							String invite = biscuit.getProperties().getKickDMInvite().replace(" ", "");
							if(!invite.equals("")) {
								msg = msg + " " + invite;
							}
							final String fmsg = msg;
							user.openPrivateChannel().flatMap(channel -> channel.sendMessage(fmsg)).complete();
						}
						biscuit.getGuild().kick(user.getId()).submit();
					}

					remove();
				}
			}
		}

	}

	public boolean shareGuild() {
		JDA jda = biscuit.getJDA();
		for(Guild g : jda.getGuilds()) {
			if(g.isMember(user)){
				return true;
			}
		}
		return false;
	}

	public ArrayList<Guild> getGuilds(){
		ArrayList<Guild> guilds = new ArrayList<Guild>();
		JDA jda = biscuit.getJDA();
		for(Guild g : jda.getGuilds()) {
			if(g.isMember(user)){
				guilds.add(g);
			}
		}
		return guilds;
	}

	public void remove() {
		setDone();
		biscuit.log("Removing captcha data for user " + user.getName() + " " + user.getAsMention());
		File captcha;
		if(!Main.isPlugin) {
			captcha = new File("captcha/" + user.getId() + ".jpg");
		}else {
			captcha = new File(PluginCore.plugin.getDataFolder(), "captcha/" + user.getId() + ".jpg");
		}
		token = null;
		captcha.delete();
		biscuit.removePreUser(this);
		testusers.remove(this);
	}

}
