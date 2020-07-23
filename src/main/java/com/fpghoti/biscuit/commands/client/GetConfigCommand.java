package com.fpghoti.biscuit.commands.client;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.logging.BColor;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

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
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -getconfig");
		if(PermUtil.isAdmin(event.getMember())) {
			event.getChannel().sendFile(b.getConfig().getFile(), "config-" + b.getProperties().getGuildCode() + ".properties").queue();
		}else {
			b.log(BColor.MAGENTA_BOLD + event.getAuthor().getName() + " lacks permission to view the config!");
			event.getChannel().sendMessage(event.getAuthor().getAsMention() + " You do not have "
					+ "permission to view the config.").queue(new Consumer<Message>()
			{
				@Override
				public void accept(Message msg){
					msg.delete().submitAfter(5, TimeUnit.SECONDS);
				}
			});
		}
	}

}
