package com.fpghoti.biscuit.commands.discord.music;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ForceSkipToCommand extends MusicClientCommand{

	public ForceSkipToCommand() {
		name = "Force Skip To";
		description = "Force skips all songs until the specified track.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "forceskipto <Track #>";
		minArgs = 1;
		maxArgs = 1;
		identifiers.add("forceskipto");
		identifiers.add("fst");
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		BiscuitGuild b = BiscuitGuild.getBiscuitGuild(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -forceskipto " + args[0]);
		if(PermUtil.hasMusicControl(event.getMember())) {
			if(Util.isDigit(args[0])) {
				int place = Integer.parseInt(args[0]);
				MessageText.send(event.getChannel().asTextChannel(), "Force skipping to queue position **" + place + "**.");
				b.getAudioScheduler().getQueue().removeTracksBefore(place);
				b.getAudioScheduler().skip(event.getChannel().asTextChannel());
			}
		}
	}

}
