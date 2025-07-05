package com.fpghoti.biscuit.commands.discord.music;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ClearCommand extends MusicClientCommand{

	public ClearCommand() {
		name = "Clear";
		description = "Clears all upcoming songs from the queue.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "clear";
		minArgs = 0;
		maxArgs = 0;
		identifiers.add("clear");
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		BiscuitGuild b = BiscuitGuild.getBiscuitGuild(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -clear");
		if(PermUtil.hasMusicControl(event.getMember())) {
			MessageText.send(event.getChannel().asTextChannel(), "Cleared all upcoming songs from the queue.");
			b.getAudioScheduler().getQueue().clear();
		}
	}

}
