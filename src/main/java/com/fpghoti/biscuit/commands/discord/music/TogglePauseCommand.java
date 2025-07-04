package com.fpghoti.biscuit.commands.discord.music;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TogglePauseCommand extends MusicClientCommand{
	
    public TogglePauseCommand() {
        name = "Toggle Pause";
        description = "Toggles the pause status of the current track.";
        usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "togglepause";
        minArgs = 0;
        maxArgs = 0;
        identifiers.add("togglepause");
        identifiers.add("tp");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		BiscuitGuild b = BiscuitGuild.getBiscuitGuild(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -togglepause");
		if(PermUtil.hasMusicControl(event.getMember())) {
			if(b.getAudioPlayer().isPaused()) {
				b.getAudioPlayer().setPaused(false);
				MessageText.send(event.getChannel().asTextChannel(), "Unpaused the current track.");
			}else {
				b.getAudioPlayer().setPaused(true);
				MessageText.send(event.getChannel().asTextChannel(), "Paused the current track.");
			}
		}
	}

}
