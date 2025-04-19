package com.fpghoti.biscuit.commands.discord.music;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -pause");
		if(PermUtil.hasMusicControl(event.getMember())) {
			if(!b.getAudioPlayer().isPaused()) {
				b.getAudioPlayer().setPaused(true);
				MessageText.send(event.getChannel().asTextChannel(), "Paused the current track.");
			}else {
				MessageText.send(event.getChannel().asTextChannel(), "The music player is already paused.");
			}
		}
	}

}
