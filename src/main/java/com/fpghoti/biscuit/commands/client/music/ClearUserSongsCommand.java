package com.fpghoti.biscuit.commands.client.music;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

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
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -clearusersongs " + args[0]);
		if(PermUtil.hasMusicControl(event.getMember())) {
			for(Member m : event.getMessage().getMentionedMembers()) {
				MessageText.send(event.getChannel(), "Clearing all upcoming tracks added by **" + m.getEffectiveName() + "**.");
				b.getAudioScheduler().getQueue().removeUserTracks(m.getId());
			}
		}
	}

}
