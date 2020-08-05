package com.fpghoti.biscuit.user;

import java.util.ArrayList;
import java.util.Iterator;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.captcha.Captcha;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class CaptchaUser implements Iterable<PreUser>{

	private static ArrayList<CaptchaUser> captchaUsers = new ArrayList<CaptchaUser>();

	public static void wipeCaptchaUsers() {
		captchaUsers = new ArrayList<CaptchaUser>();
	}

	public static boolean contains(User u) {
		for(CaptchaUser c : captchaUsers) {
			if(c.equals(u)) {
				return true;
			}
		}
		return false;
	}

	public static boolean contains(PreUser u) {
		for(CaptchaUser c : captchaUsers) {
			if(c.equals(u)) {
				return true;
			}
		}
		return false;
	}
	public static boolean contains(CaptchaUser u) {
		for(CaptchaUser c : captchaUsers) {
			if(c.equals(u)) {
				return true;
			}
		}
		return false;
	}

	public static CaptchaUser getCaptchaUser(User u) {
		if(contains(u)) {
			for(CaptchaUser c : captchaUsers) {
				if(c.equals(u)) {
					return c;
				}
			}
		}else {
			CaptchaUser c = new CaptchaUser(u);
			captchaUsers.add(c);
			return c;
		}
		Main.getMainBiscuit().error("Could not get Captcha User.");
		return null;
	}

	public static void remove(CaptchaUser u) {
		ArrayList<CaptchaUser> cu = new ArrayList<CaptchaUser>(captchaUsers);
		for(CaptchaUser c : cu) {
			if(c.equals(u)) {
				captchaUsers.remove(c);
			}
		}
	}

	private User user;
	private boolean testMode;
	private Captcha captcha;
	private ArrayList<PreUser> preUsers;

	private CaptchaUser(User user) {
		this.user = user;
		this.testMode = false;
		this.captcha = null;
		this.preUsers = new ArrayList<PreUser>();
	}

	@Override
	public Iterator<PreUser> iterator() {
		return new ArrayList<PreUser>(preUsers).iterator();
	}

	public User getUser() {
		return user;
	}

	public Captcha getCaptcha(PrivateMessageReceivedEvent event) {
		captcha = Captcha.getUpdatedCaptcha(event);
		return captcha;
	}

	public void setCaptcha(Captcha c) {
		captcha = c;
	}

	public boolean equals(User u) {
		return user.getId().equals(u.getId());
	}

	public boolean equals(PreUser u) {
		return user.getId().equals(u.getUser().getId());
	}

	public boolean equals(CaptchaUser u) {
		return user.getId().equals(u.getUser().getId());
	}

	public boolean contains(Guild g) {
		if(g == null) {
			return false;
		}
		for(PreUser u : preUsers) {
			if(u.getBiscuit().getGuild().getId().equals(g.getId())) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(Biscuit b) {
		return contains(b.getGuild());
	}
	
	public boolean isEmpty() {
		return preUsers.isEmpty();
	}
	
	public boolean inTestMode() {
		return testMode;
	}
	
	public void enableTestMode() {
		testMode = true;
	}
	
	public void disableTestMode() {
		testMode = false;
	}

	public boolean shareGuild() {
		JDA jda = Main.getJDA();
		for(Guild g : jda.getGuilds()) {
			if(g.isMember(user)){
				return true;
			}
		}
		return false;
	}

	public ArrayList<Guild> getSharedGuilds(){
		ArrayList<Guild> guilds = new ArrayList<Guild>();
		JDA jda = Main.getJDA();
		for(Guild g : jda.getGuilds()) {
			if(g.isMember(user)){
				guilds.add(g);
			}
		}
		return guilds;
	}

	public void add(PreUser u) {
		if(u.getBiscuit() == null || u.getBiscuit().getGuild() == null) {
			return;
		}
		if(contains(u.getBiscuit())) {
			return;
		}
		preUsers.add(u);
	}

	public void remove(PreUser u) {
		ArrayList<PreUser> ps = new ArrayList<PreUser>(preUsers);
		for(PreUser p : ps) {
			if(p.equals(u)) {
				preUsers.remove(p);
			}
		}
	}

	public PreUser get(Guild g) {
		if(g == null) {
			return null;
		}
		for(PreUser u : preUsers) {
			if(u.getBiscuit().getGuild().getId().equals(g.getId())) {
				return u;
			}
		}
		return null;
	}

	public PreUser get(Biscuit b) {
		return get(b.getGuild());
	}

	//	public PreUser getTestUser() {
	//		if(testUser == null) {
	//			PreUser preu = PreUser.makeTestUser(this);
	//			testUser = preu;
	//		}
	//		return testUser;
	//	}


	public Captcha getCaptcha() {
		return captcha;
	}

}
