package com.fpghoti.biscuit.commands.discord;

import java.util.List;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.logging.BColor;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SaveConfigCommand extends ClientCommand{

	public SaveConfigCommand() {
		name = "Save Config";
		description = "Saves the config of the current guild.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "saveconfig (with file upload)";
		minArgs = 0;
		maxArgs = 0;
		identifiers.add("saveconfig");
	}

	@Override
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -saveconfig");
		List<Attachment> attch = event.getMessage().getAttachments();
		if(PermUtil.isAdmin(event.getMember())) {
			if(!attch.isEmpty()) {
				if(attch.size() == 1) {
					for(Attachment a : attch) {
						b.getConfig().replaceConfig(a, event.getChannel());
						b.remove();
						b = Biscuit.loadGuild(event.getGuild());
					}
				}else {
					MessageText.sendTimed(event.getChannel(), event.getAuthor().getAsMention() + " Too many attachments added! "
							+ "Please only include the config file you want to save.", 5);
				}
			}else {
				MessageText.sendTimed(event.getChannel(), event.getAuthor().getAsMention() + " You need to send "
						+ "a file in order to save the config.", 5);
			}
		}else {
			b.log(BColor.MAGENTA_BOLD + event.getAuthor().getName() + " lacks permission to save the config!");
			MessageText.sendTimed(event.getChannel(), event.getAuthor().getAsMention() + " You do not have "
					+ "permission to save the config.", 5);
		}
	}

}
