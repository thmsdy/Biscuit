package com.fpghoti.biscuit.commands.discord.music;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ClearUserSongsCommand extends MusicClientCommand{

	public ClearUserSongsCommand() {
		name = "Clear User Songs";
		description = "Clears all upcoming songs from specified user.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "clearusersongs <@user>";
		minArgs = 1;
		maxArgs = 1;
		identifiers.add("clearusersongs");
		identifiers.add("cus");
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		BiscuitGuild b = BiscuitGuild.getBiscuitGuild(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -clearusersongs " + args[0]);
		if(PermUtil.hasMusicControl(event.getMember())) {
			for(Member m : event.getMessage().getMentions().getMembers()) {
				MessageText.send(event.getChannel().asTextChannel(), "Clearing all upcoming tracks added by **" + m.getEffectiveName() + "**.");
				b.getAudioScheduler().getQueue().removeUserTracks(m.getId());
			}
		}
	}

}
