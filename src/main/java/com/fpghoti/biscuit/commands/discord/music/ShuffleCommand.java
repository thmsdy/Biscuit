package com.fpghoti.biscuit.commands.discord.music;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.rest.MessageText;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

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
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -shuffle");
		if(b.getAudioScheduler().getQueue().getLastTrack() != null ) {
			b.getAudioScheduler().getQueue().shuffleUserTracks(event.getAuthor().getId());
			MessageText.send(event.getChannel(), "All songs that you have added to the queue have been shuffled.");
		}else {
			MessageText.send(event.getChannel(), "No song is currently playing.");
		}
	}

}
