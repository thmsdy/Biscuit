package com.fpghoti.biscuit.listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.PluginCore;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.fpghoti.biscuit.user.PreUser;
import com.github.cage.Cage;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DMListener extends ListenerAdapter{

	Logger log = Main.log;

	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		if (event.isFromType(ChannelType.PRIVATE) && !event.getAuthor().isBot()) {
			if(PropertiesRetrieval.logChat()) {
				log.info( "NEW PRIVATE MESSAGE - MSGID: " + event.getMessageId() + "- @" + event.getAuthor().getName() + " " + event.getAuthor().getAsMention() + " - " + event.getMessage().getContentDisplay());
			}
			handleCaptcha(event);
		}
	}

	private void handleCaptcha(MessageReceivedEvent event) {
		PrivateChannel channel = event.getPrivateChannel();
		User author = event.getAuthor();
		if(PreUser.getPreUser(author) != null) {
			PreUser preu = PreUser.getPreUser(author);
			if(preu.getToken() == null || !event.getMessage().getContentDisplay().equals(preu.getToken())) {

				if(preu.getToken() != null) {
					channel.sendMessage("Sorry! That's not quite right! Please try again.").queue();
				}
				Main.log.info("Generating captcha challenge for user " + author.getName() + " " + author.getAsMention() + "...");

				Cage cage = Main.getBiscuit().getCage();

				preu.genToken();

				OutputStream os;
				try {
					//os = new FileOutputStream("captcha/" + author.getId() + ".jpg", false);
					if(!Main.isPlugin) {
						os = new FileOutputStream("captcha/" + author.getId() + ".jpg", false);
					}else {
						File c = new File(PluginCore.plugin.getDataFolder(), "captcha/" + author.getId() + ".jpg");
						os = new FileOutputStream(c, false);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return;
				}
				try {
					cage.draw(preu.getToken() , os);
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
				File captcha;
				if(!Main.isPlugin) {
					captcha = new File("captcha/" + author.getId() + ".jpg");
				}else {
					captcha = new File(PluginCore.plugin.getDataFolder(), "captcha/" + author.getId() + ".jpg");
				}
				channel.sendMessage("Respond with the exact text in this image (case-sensitive)").queue();
				channel.sendFile(captcha).queue();

			}else {
				preu.setDone();
				Main.log.info(author.getName() + " successfully completed a captcha challenge. Granting role.");

				Role newrole = null;
				Role defaultrole = null;

				for(Guild g : preu.getGuilds()) {
					for(Role r : g.getRoles()) {
						if(r.getName().toLowerCase().contains(PropertiesRetrieval.getCaptchaReward().toLowerCase())) {
							newrole = r;
						}else if(r.getName().toLowerCase().contains(PropertiesRetrieval.getDefaultRole().toLowerCase())) {
							defaultrole = r;
						}
					}
					if(newrole == null) {
						Main.log.error("Cannot find captcha reward role!");
						return;
					}

					if(defaultrole == null) {
						Main.log.error("Cannot find captcha default role!");
						return;
					}

					Member member = g.getMemberById(author.getId());

					g.addRoleToMember(member, newrole).queue();
					g.removeRoleFromMember(member, defaultrole).queue();
					preu.remove();
				}
				channel.sendMessage("Well done, " + author.getAsMention() + "!").queue();
			}

		}
	}

}
