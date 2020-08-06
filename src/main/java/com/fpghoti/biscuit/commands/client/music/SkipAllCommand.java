package com.fpghoti.biscuit.commands.client.music;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SkipAllCommand extends MusicClientCommand{

	public SkipAllCommand() {
		name = "Skip All";
		description = "Forces the all songs to be skipped.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "skipall";
		minArgs = 0;
		maxArgs = 0;
		identifiers.add("skipall");
	}

	@Override
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -skipall");
		if(PermUtil.hasMusicControl(event.getMember())) {
			MessageText.send(event.getChannel(), "Force skipping all songs.");
			b.getAudioScheduler().getQueue().clear();
			b.getAudioScheduler().skip(event.getChannel());
		}
	}

}
