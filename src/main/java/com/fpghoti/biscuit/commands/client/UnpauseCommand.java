package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class UnpauseCommand extends ClientCommand{
	
    public UnpauseCommand() {
        name = "Unpause";
        description = "Unpauses the current track.";
        usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "unpause";
        minArgs = 0;
        maxArgs = 0;
        identifiers.add("unpause");
    }

	@Override
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -unpause");
		if(PermUtil.isMod(event.getMember())) {
			if(b.getAudioPlayer().isPaused()) {
				b.getAudioPlayer().setPaused(false);
				MessageText.send(event.getChannel(), "Unpaused the current track.");
			}else {
				MessageText.send(event.getChannel(), "The music player is not paused.");
			}
		}
	}

}
