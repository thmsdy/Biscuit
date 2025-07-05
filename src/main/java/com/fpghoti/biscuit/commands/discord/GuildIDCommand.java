package com.fpghoti.biscuit.commands.discord;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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
	public void execute(String[] args, MessageReceivedEvent event) {
		BiscuitGuild b = BiscuitGuild.getBiscuitGuild(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -guildid");
		if(PermUtil.isMod(event.getMember())) {
			MessageText.send(event.getChannel().asTextChannel(), event.getGuild().getId());
		}
	}

}
