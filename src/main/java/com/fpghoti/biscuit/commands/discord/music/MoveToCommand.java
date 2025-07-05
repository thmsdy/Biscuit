package com.fpghoti.biscuit.commands.discord.music;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MoveToCommand extends MusicClientCommand{

	public MoveToCommand() {
		name = "Move To";
		description = "Moves track to specified position.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "moveto <CurrentTrack #> <NewTrack #>";
		minArgs = 2;
		maxArgs = 2;
		identifiers.add("moveto");
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		BiscuitGuild b = BiscuitGuild.getBiscuitGuild(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -moveto " + args[0] + " " + args[1]);
		if(PermUtil.hasMusicControl(event.getMember())) {
			if(Util.isDigit(args[0]) && Util.isDigit(args[1])) {
				int oldPlace = Integer.parseInt(args[0]);
				int newPlace = Integer.parseInt(args[1]);
				MessageText.send(event.getChannel().asTextChannel(), "The specified track was moved." );
				b.getAudioScheduler().getQueue().moveToPlace(oldPlace, newPlace);
			}
		}
	}

}
