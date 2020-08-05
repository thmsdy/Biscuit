package com.fpghoti.biscuit.captcha;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.PluginCore;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.logging.BColor;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.user.CaptchaUser;
import com.fpghoti.biscuit.user.PreUser;
import com.github.cage.Cage;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class Captcha {

	public static Captcha getUpdatedCaptcha(PrivateMessageReceivedEvent event) {
		CaptchaUser c = CaptchaUser.getCaptchaUser(event.getAuthor());
		if(c.getCaptcha() == null) {
			Captcha captcha = new Captcha(event);
			c.setCaptcha(captcha);
			return captcha;
		}else {
			Captcha captcha = c.getCaptcha();
			captcha.setEvent(event);
			c.setCaptcha(captcha);
			return captcha;
		}
	}

	private CaptchaUser user;
	private PrivateMessageReceivedEvent event;
	private PrivateChannel channel;
	private User author;
	private String token;

	private Captcha(PrivateMessageReceivedEvent event) {
		this.user = CaptchaUser.getCaptchaUser(event.getAuthor());
		this.event = event;
		this.channel = event.getChannel();
		this.author = event.getAuthor();
		this.token = null;
	}

	public void setEvent(PrivateMessageReceivedEvent event) {
		this.event = event;
	}

	public User getAuthor() {
		return author;
	}
	
	public CaptchaUser getCaptchaUser() {
		return user;
	}

	public PrivateChannel getChannel() {
		return channel;
	}

	public String getToken() {
		return this.token;
	}

	public void genToken() {
		Cage cage = Main.getCage();
		token = cage.getTokenGenerator().next();
	}

	public void handleResponse() {
		if(user.isEmpty() && !user.inTestMode()) {
			return;
		}

		String response = leeway(event.getMessage().getContentDisplay());
		
		if(token != null && !response.equalsIgnoreCase(token)) {
			respond("Sorry! That's not quite right! Please try again.");
			return;
		}

		if(token == null) {
			
			log("Generating captcha challenge for user " + author.getName() + " " + author.getAsMention() + "...");

			genToken();
			generateImage();
			File captcha = getImageFile();

			respond("Respond with the exact text in this image.");
			channel.sendFile(captcha).submit();

		}else {
			boolean disable = false;
			if(user.inTestMode()) {
				disable = true;
			}else {
				doCaptchaReward();
			}
			respond("Well done, " + author.getAsMention() + "!");
			if(disable) {
				user.disableTestMode();
			}
		}
	}

	public void generateImage() {
		Cage cage = Main.getCage();
		OutputStream os;

		try {
			if(!Main.isPlugin) {
				//If Biscuit is running standalone output to this directory
				os = new FileOutputStream("captcha/" + author.getId() + ".jpg", false);
			}else {
				//If Biscuit is running as a Spigot plugin output to this directory
				File c = new File(PluginCore.plugin.getDataFolder(), "captcha/" + author.getId() + ".jpg");
				os = new FileOutputStream(c, false);
			}
		} catch (FileNotFoundException e) {
			Main.getMainBiscuit().error("Cannot retrieve captcha image directory.");
			e.printStackTrace();
			return;
		}

		//Draw captcha image
		try {
			cage.draw(token, os);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	public File getImageFile() {
		if(!Main.isPlugin) {
			return new File("captcha/" + author.getId() + ".jpg");
		}else {
			return new File(PluginCore.plugin.getDataFolder(), "captcha/" + author.getId() + ".jpg");
		}
	}

	public void doCaptchaReward() {
		for(PreUser p : user) {
			//mark the PreUser as "done"
			p.setDone();			

			Biscuit biscuit = p.getBiscuit();

			if(biscuit == null) {
				Main.getMainBiscuit().error("CAPTCHA ERROR: Null Biscuit");
				return;
			}

			Guild g = biscuit.getGuild();	

			if(g == null) {
				biscuit.error("CAPTCHA ERROR: Null Guild");
				return;
			}

			Role newrole = biscuit.getCaptchaRewardRole();
			Role defaultrole = biscuit.getDefaultRole();

			if(newrole == null) {
				biscuit.error("Cannot find captcha reward role!");
				return;
			}

			if(defaultrole == null) {
				biscuit.error("Cannot find captcha default role!");
				return;
			}

			Member member = g.getMemberById(author.getId());

			g.addRoleToMember(member, newrole).complete();
			g.removeRoleFromMember(member, defaultrole).complete();
			p.remove();
			token = null;

			log(BColor.YELLOW_BOLD + author.getName() + " successfully completed a captcha challenge. Granting role.");
			biscuit.captchaLog(" ``" + author.getName() +"`` " + author.getAsMention() + " successfully completed a captcha challenge. Granting role.");
		}
	}

	public void removeFiles() {
		File captcha;
		if(!Main.isPlugin) {
			//Biscuit is running standalone. Remove file from this directory
			captcha = new File("captcha/" + user.getUser().getId() + ".jpg");
		}else {
			//Biscuit is running as Spigot plugin. Remove file from this directory
			captcha = new File(PluginCore.plugin.getDataFolder(), "captcha/" + user.getUser().getId() + ".jpg");
		}
		captcha.delete();
	}

	//More characters may be replaced/removed here based on
	//How many/what type of errors users commonly make
	private String leeway(String s) {
		s = s.replace("0", "O");
		return s;
	}

	private void log(String msg) {
		String prefix = "";
		if(user.inTestMode()) {
			prefix = "[TEST] ";
		}
		Main.getMainBiscuit().log(prefix + msg);
	}

	private void respond(String msg) {
		String prefix = "";
		if(user.inTestMode()) {
			prefix = "[TEST] ";
		}
		MessageText.send(channel, prefix + msg);
	}

}
