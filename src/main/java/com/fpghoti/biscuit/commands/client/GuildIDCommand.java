package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class GuildIDCommand extends ClientCommand{

	public GuildIDCommand() {
		name = "Guild ID";
		description = "Retrieves a guild's ID.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "guildid";
		minArgs = 0;
		maxArgs = 0;
		identifiers.add("guildid");
		identifiers.add("gid");
	}

	@Override
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -guildid");
		if(PermUtil.isMod(event.getMember())) {
			MessageText.send(event.getChannel(), event.getGuild().getId());
		}
	}

}
