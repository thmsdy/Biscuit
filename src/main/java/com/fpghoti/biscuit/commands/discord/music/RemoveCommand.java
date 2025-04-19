package com.fpghoti.biscuit.commands.discord.music;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RemoveCommand extends MusicClientCommand{

	public RemoveCommand() {
		name = "Remove";
		description = "Removes song from specified position in queue.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "remove <Track #>";
		minArgs = 1;
		maxArgs = 1;
		identifiers.add("remove");
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -remove " + args[0]);
		if(PermUtil.hasMusicControl(event.getMember())) {
			if(Util.isDigit(args[0])) {
				int place = Integer.parseInt(args[0]);
				MessageText.send(event.getChannel().asTextChannel(), "Removing track at position **" + place + "**.");
				b.getAudioScheduler().getQueue().removeTrack(place);
			}
		}
	}

}
