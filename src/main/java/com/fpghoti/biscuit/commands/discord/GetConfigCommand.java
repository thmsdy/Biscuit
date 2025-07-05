package com.fpghoti.biscuit.commands.discord;

import java.io.File;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.logging.BColor;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;

public class GetConfigCommand extends ClientCommand{

	public GetConfigCommand() {
		name = "Get Config";
		description = "Gets the config of the current guild.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "getconfig";
		minArgs = 0;
		maxArgs = 0;
		identifiers.add("getconfig");
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		BiscuitGuild b = BiscuitGuild.getBiscuitGuild(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -getconfig");
		if(PermUtil.isAdmin(event.getMember())) {
			File config = b.getConfig().getFile();
			FileUpload confUpload = FileUpload.fromData(config);
			confUpload.setName("config-" + b.getProperties().getGuildCode() + ".properties");
			//event.getChannel().sendFile(config, "config-" + b.getProperties().getGuildCode() + ".properties").queue();
			event.getChannel().asTextChannel().sendFiles(confUpload).queue();
		}else {
			b.log(BColor.MAGENTA_BOLD + event.getAuthor().getName() + " lacks permission to view the config!");
			MessageText.sendTimed(event.getChannel().asTextChannel(), event.getAuthor().getAsMention() + " You do not have "
					+ "permission to view the config.", 5);
		}
	}

}
