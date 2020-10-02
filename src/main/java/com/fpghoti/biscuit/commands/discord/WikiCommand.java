package com.fpghoti.biscuit.commands.discord;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class WikiCommand extends ClientCommand{

	public WikiCommand() {
		name = "Wiki";
		description = "Sends a link to the Biscuit wiki. Look there for config help.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "wiki";
		minArgs = 0;
		maxArgs = 0;
		identifiers.add("wiki");
	}

	@Override
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -wiki");
		if(PermUtil.isMod(event.getMember())) {
			MessageText.send(event.getChannel(), "https://git.fpghoti.com/thmsdy/Biscuit/wiki");
		}
	}

}
