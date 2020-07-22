package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class WipeQueueCommand extends MusicClientCommand{

	public WipeQueueCommand() {
		name = "Wipe Queue";
		description = "Removes all upcoming tracks from the queue.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "wipequeue";
		minArgs = 0;
		maxArgs = 0;
		identifiers.add("wipequeue");
		identifiers.add("wipeq");
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -wipequeue");
		if(PermUtil.isMod(event.getMember())) {
			b.getAudioScheduler().wipeQueue();
			event.getChannel().sendMessage("Removed upcoming songs from the music queue.").queue();
		}
	}

}
