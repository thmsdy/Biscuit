package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PauseCommand extends MusicClientCommand{
	
    public PauseCommand() {
        name = "Pause";
        description = "Pauses the current track.";
        usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "pause";
        minArgs = 0;
        maxArgs = 0;
        identifiers.add("pause");
    }

	@Override
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -pause");
		if(PermUtil.isMod(event.getMember())) {
			if(!b.getAudioPlayer().isPaused()) {
				b.getAudioPlayer().setPaused(true);
				event.getChannel().sendMessage("Paused the current track.").queue();
			}else {
				event.getChannel().sendMessage("The music player is already paused.").queue();
			}
		}
	}

}
