package com.fpghoti.biscuit.listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.slf4j.Logger;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.PluginCore;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.fpghoti.biscuit.user.PreUser;
import com.fpghoti.biscuit.util.PermUtil;
import com.github.cage.Cage;

import net.dv8tion.jda.api.JDA;
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

	private static ArrayList<User> testers = new ArrayList<User>();

	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		if (event.isFromType(ChannelType.PRIVATE) && !event.getAuthor().isBot()) {
			String content = event.getMessage().getContentDisplay();
			User user = event.getAuthor();
			boolean isTest = false;
			boolean found = false;
			if(testers.size() > 0) {
				for(User u : testers) {
					if(user.getId().equals(u.getId())) {
						isTest = true;
						found = true;
					}
				}
			}
			if(content.equalsIgnoreCase("captcha pls") || content.equalsIgnoreCase("cpls")) {
				JDA jda = Main.getBiscuit().getJDA();
				for(Guild g : jda.getGuilds()) {
					if(g.isMember(user)) {
						Member m = g.getMember(user);
						if(PermUtil.isAdmin(m)) {
							isTest = true;
							if(!found) {
								PreUser.testusers.add(new PreUser(user,true));
								testers.add(user);
							}
						}
					}
				}
			}
			if(PropertiesRetrieval.logChat()) {
				log.info( "NEW PRIVATE MESSAGE - MSGID: " + event.getMessageId() + "- @" + user.getName() + " " + event.getAuthor().getAsMention() + " - " + content);
			}
			handleCaptcha(event, isTest);
		}
	}

	private void handleCaptcha(MessageReceivedEvent event, boolean isTest) {
		PreUser preu;
		PrivateChannel channel = event.getPrivateChannel();
		User author = event.getAuthor();
		if(PreUser.getPreUser(author) != null || isTest) {
			if(isTest) {
				preu = PreUser.getTestUser(author);
			}else {
				preu = PreUser.getPreUser(author);
			}
			
			
			String response = leeway(event.getMessage().getContentDisplay());
			
			if(preu.getToken() == null || !response.equalsIgnoreCase(preu.getToken())) {
				String tlabel = "";
				if(isTest) {
					tlabel = "[TEST] ";
				}
				if(preu.getToken() != null) {
					channel.sendMessage(tlabel + "Sorry! That's not quite right! Please try again.").queue();
				}
				Main.log.info(tlabel + "Generating captcha challenge for user " + author.getName() + " " + author.getAsMention() + "...");

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
				channel.sendMessage(tlabel+ "Respond with the exact text in this image.").queue();
				channel.sendFile(captcha).queue();

			}else {
				String tlabel = "";
				if(isTest) {
					tlabel = "[TEST] ";
				}
				preu.setDone();
				Main.log.info(tlabel + author.getName() + " successfully completed a captcha challenge. Granting role.");

				Role newrole = null;
				Role defaultrole = null;

				if(isTest) {
					testers.remove(author);
					preu.remove();
				}else {
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
				}
				channel.sendMessage(tlabel + "Well done, " + author.getAsMention() + "!").queue();
			}

		}
	}
	
	private String leeway(String s) {
		s = s.replace("l", "1");
		s = s.replace("g", "9");
		s = s.replace("0", "O");
		return s;
	}
	

}
