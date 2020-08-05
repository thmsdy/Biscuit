package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ForceSkipCommand extends MusicClientCommand{

	public ForceSkipCommand() {
		name = "Force Skip";
		description = "Forces the current song to be skipped.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "forceskip";
		minArgs = 0;
		maxArgs = 0;
		identifiers.add("forceskip");
	}

	@Override
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -forceskip");
		if(PermUtil.isMod(event.getMember())) {
			MessageText.send(event.getChannel(), "Force skipping current song.");
			if(b.getAudioScheduler().getQueue().getNext() != null ) {
				MessageEmbed next = b.getAudioScheduler().getQueue().getNext().getEmbedMessage("Now Playing:");
				MessageText.send(event.getChannel(), next);
			}
			b.getAudioScheduler().skip();
		}
	}

}
