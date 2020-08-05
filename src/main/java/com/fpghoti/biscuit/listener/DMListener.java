package com.fpghoti.biscuit.listener;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.captcha.Captcha;
import com.fpghoti.biscuit.logging.BColor;
import com.fpghoti.biscuit.user.CaptchaUser;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DMListener extends ListenerAdapter{

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event){
		if (!event.getAuthor().isBot()) {

			//Log DM content to console
			if(Main.getMainBiscuit().getProperties().logChat()) {
				Main.getMainBiscuit().log("[" + BColor.CYAN_BOLD + "DM" + BColor.RESET + "] " + BColor.YELLOW + "ID: " + BColor.RESET +
						event.getMessageId() + BColor.YELLOW + " Sender: " + BColor.RESET +  event.getAuthor().getAsMention());
				Main.getMainBiscuit().log(BColor.YELLOW + event.getAuthor().getName() + ": " + BColor.WHITE_BOLD + event.getMessage().getContentDisplay());
			}

			String content = event.getMessage().getContentDisplay();
			Captcha captcha = Captcha.getUpdatedCaptcha(event);
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
	}

}
