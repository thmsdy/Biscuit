package com.fpghoti.biscuit.user;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class PreUser {
	
	public static PreUser getPreUser(CaptchaUser capUser, Biscuit biscuit) {
		if(capUser == null) {
			Main.getMainBiscuit().error("Cannot get PreUser (Invalid Captcha User).");
			return null;
		}
		if(biscuit == null) {
			Main.getMainBiscuit().error("Cannot get PreUser (Invalid Biscuit Instance).");
			return null;
		}
		if(capUser.contains(biscuit)) {
			return capUser.get(biscuit);
		}
		PreUser preu = new PreUser(capUser, biscuit, false);
		capUser.add(preu);
		return preu;
	}

	private CaptchaUser capUser;
	private User user;
	private int timeLeft;
	private boolean done;
	private boolean test;
	private Biscuit biscuit;

	private PreUser(CaptchaUser capUser, Biscuit biscuit, boolean test) {
		this.test = test;
		this.capUser = capUser;
		this.user = capUser.getUser();
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
	
	public boolean isTestUser() {
		return test;
	}
	
	public CaptchaUser getCaptchaUser() {
		return this.capUser;
	}

	public void setDone() {
		this.done = true;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void decrementTime() {
		if(!capUser.shareGuild()) {
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
							user.openPrivateChannel().flatMap(channel -> channel.sendMessage(fmsg)).submit().whenComplete((message, error) -> {
								if (error != null) {
									biscuit.log("Unable to private message user " + user.getName() +".");
								}
								kick();
							});
						}else {
							kick();
						}
					}
				}
			}
		}

	}

	public void kick() {
		remove();
		biscuit.getGuild().kick(user.getId()).submit();
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

	public void remove() {
		setDone();
		biscuit.log("Removing captcha data for user " + user.getName() + " " + user.getAsMention());
		if(capUser.getCaptcha() != null) {
			capUser.getCaptcha().removeFiles();
		}
		biscuit.removePreUser(this);
		if(biscuit.preUserExists(user)) {
			biscuit.error("CAPTCHA ERROR: PreUser exists after removal");
		}
		capUser.remove(this);
	}

}
