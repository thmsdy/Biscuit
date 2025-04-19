package com.fpghoti.biscuit.captcha;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.user.CaptchaUser;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public class HandleCaptcha {

	public static void handleCaptcha(User user, MessageChannel channel, String content) {
		//String content = event.getMessage().getContentDisplay();
		Captcha captcha = Captcha.getUpdatedCaptcha(user, channel, content);
		CaptchaUser capUser = captcha.getCaptchaUser();

		//User is requesting a captcha test
		if(content.equalsIgnoreCase("captcha pls") || content.equalsIgnoreCase("cpls")) {
			JDA jda = Main.getJDA();
			for(Guild g : jda.getGuilds()) {
				if(g.isMember(capUser.getUser())) {
					Member m = g.getMember(capUser.getUser());
					if(PermUtil.isAdmin(m)) {
						capUser.enableTestMode();
					}
				}
			}
		}
		captcha.handleResponse();
	}
	
	public static Boolean isCaptchaChannel(TextChannel c) {
		Biscuit biscuit = Biscuit.getBiscuit(c.getGuild());
		Boolean a = true;
		for(String s: biscuit.getProperties().getCaptchaChannels()) {
			if(c.getName().equalsIgnoreCase(s)) {
				a = false;
			}
		}
		return a;
	}
	
}
