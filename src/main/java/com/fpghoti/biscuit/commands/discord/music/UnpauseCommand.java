package com.fpghoti.biscuit.commands.discord.music;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class UnpauseCommand extends MusicClientCommand{
	
    public UnpauseCommand() {
        name = "Unpause";
        description = "Unpauses the current track.";
        usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "unpause";
        minArgs = 0;
        maxArgs = 0;
        identifiers.add("unpause");
        identifiers.add("resume");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -unpause");
		if(PermUtil.hasMusicControl(event.getMember())) {
			if(b.getAudioPlayer().isPaused()) {
				b.getAudioPlayer().setPaused(false);
				MessageText.send(event.getTextChannel(), "Unpaused the current track.");
			}else {
				MessageText.send(event.getTextChannel(), "The music player is not paused.");
			}
		}
	}

}
