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
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.entities.User;

public class Captcha {

	public static Captcha getUpdatedCaptcha(User user, MessageChannel channel, String response) {
		CaptchaUser c = CaptchaUser.getCaptchaUser(user);
		if(c.getCaptcha() == null) {
			Captcha captcha = new Captcha(user, channel, response);
			c.setCaptcha(captcha);
			return captcha;
		}else {
			Captcha captcha = c.getCaptcha();
			captcha.setChannel(channel);
			captcha.setResponse(response);
			c.setCaptcha(captcha);
			return captcha;
		}
	}

	private CaptchaUser captchaUser;
	private MessageChannel channel;
	private User user;
	private String response;
	private String token;

	private Captcha(User user, MessageChannel channel, String response) {
		this.captchaUser = CaptchaUser.getCaptchaUser(user);
		this.channel = channel;
		this.user = user;
		this.response = response;
		this.token = null;
	}

	private void setChannel(MessageChannel channel) {
		this.channel = channel;
	}
	
	private void setResponse(String response) {
		this.response = response;
	}

	public User getUser() {
		return user;
	}

	public CaptchaUser getCaptchaUser() {
		return captchaUser;
	}

	public MessageChannel getChannel() {
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
		if(captchaUser.isEmpty() && !captchaUser.inTestMode()) {
			return;
		}

		String captchaString = leeway(response);

		if(token != null && !captchaString.equalsIgnoreCase(token)) {
			respond("Sorry! That's not quite right! Please try again.");
			return;
		}

		if(token == null) {

			log("Generating captcha challenge for user " + user.getName() + " " + user.getAsMention() + "...");

			genToken();
			generateImage();
			File captcha = getImageFile();
			FileUpload capUpload = FileUpload.fromData(captcha);

			respond("Respond with the exact text in this image.");
			//channel.sendFile(captcha).submit();
			channel.sendFiles(capUpload);
		}else {
			boolean disable = false;
			if(captchaUser.inTestMode()) {
				disable = true;
			}else {
				doCaptchaReward();
			}
			respond("Well done, " + user.getAsMention() + "!");
			if(disable) {
				captchaUser.disableTestMode();
			}
		}
	}

	public void generateImage() {
		Cage cage = Main.getCage();
		OutputStream os;

		try {
			if(!Main.isPlugin) {
				//If Biscuit is running standalone output to this directory
				os = new FileOutputStream("captcha/" + user.getId() + ".jpg", false);
			}else {
				//If Biscuit is running as a Spigot plugin output to this directory
				File c = new File(PluginCore.plugin.getDataFolder(), "captcha/" + user.getId() + ".jpg");
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
			return new File("captcha/" + user.getId() + ".jpg");
		}else {
			return new File(PluginCore.plugin.getDataFolder(), "captcha/" + user.getId() + ".jpg");
		}
	}

	public void doCaptchaReward() {
		for(PreUser p : captchaUser) {
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

			Member member = g.getMemberById(user.getId());

			g.addRoleToMember(member, newrole).complete();
			g.removeRoleFromMember(member, defaultrole).complete();
			p.remove();
			token = null;

			log(BColor.YELLOW_BOLD + user.getName() + " successfully completed a captcha challenge. Granting role.");
			biscuit.eventLog(" ``" + user.getName() +"`` " + user.getAsMention() + " successfully completed a captcha challenge. Granting role.");
		}
	}

	public void removeFiles() {
		File captcha;
		if(!Main.isPlugin) {
			//Biscuit is running standalone. Remove file from this directory
			captcha = new File("captcha/" + captchaUser.getUser().getId() + ".jpg");
		}else {
			//Biscuit is running as Spigot plugin. Remove file from this directory
			captcha = new File(PluginCore.plugin.getDataFolder(), "captcha/" + captchaUser.getUser().getId() + ".jpg");
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
		if(captchaUser.inTestMode()) {
			prefix = "[TEST] ";
		}
		Main.getMainBiscuit().log(prefix + msg);
	}

	private void respond(String msg) {
		String prefix = "";
		if(captchaUser.inTestMode()) {
			prefix = "[TEST] ";
		}
		if(channel instanceof TextChannel) {
			MessageText.send((TextChannel)channel, prefix + msg);
		}else if(channel instanceof PrivateChannel) {
			MessageText.send((PrivateChannel)channel, prefix + msg);
		}
	}

}
