package com.fpghoti.biscuit.commands.discord.music;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ShuffleCommand extends MusicClientCommand{
	
    public ShuffleCommand() {
        name = "Shuffle";
        description = "Shuffles the sender's songs in the queue.";
        usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "shuffle";
        minArgs = 0;
        maxArgs = 0;
        identifiers.add("shuffle");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		BiscuitGuild b = BiscuitGuild.getBiscuitGuild(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -shuffle");
		if(b.getAudioScheduler().getQueue().getLastTrack() != null ) {
			b.getAudioScheduler().getQueue().shuffleUserTracks(event.getAuthor().getId());
			MessageText.send(event.getChannel().asTextChannel(), "All songs that you have added to the queue have been shuffled.");
		}else {
			MessageText.send(event.getChannel().asTextChannel(), "No song is currently playing.");
		}
	}

}
