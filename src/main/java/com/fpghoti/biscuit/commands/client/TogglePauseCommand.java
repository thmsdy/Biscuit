package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

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
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -togglepause");
		if(PermUtil.isMod(event.getMember())) {
			if(b.getAudioPlayer().isPaused()) {
				b.getAudioPlayer().setPaused(false);
				event.getChannel().sendMessage("Unpaused the current track.").queue();
			}else {
				b.getAudioPlayer().setPaused(true);
				event.getChannel().sendMessage("Paused the current track.").queue();
			}
		}
	}

}
