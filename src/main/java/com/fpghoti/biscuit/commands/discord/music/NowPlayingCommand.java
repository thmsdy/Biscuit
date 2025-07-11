package com.fpghoti.biscuit.commands.discord.music;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class NowPlayingCommand extends MusicClientCommand{
	
    public NowPlayingCommand() {
        name = "Now Playing";
        description = "Displays the currently playing song.";
        usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "nowplaying";
        minArgs = 0;
        maxArgs = 0;
        identifiers.add("nowplaying");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		BiscuitGuild b = BiscuitGuild.getBiscuitGuild(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -nowplaying");
		if(b.getAudioScheduler().getQueue().getLastTrack() != null ) {
			MessageEmbed next = b.getAudioScheduler().getQueue().getLastTrack().getEmbedMessage("Now Playing:", true);
			MessageText.send(event.getChannel().asTextChannel(), next);
		}else {
			MessageText.send(event.getChannel().asTextChannel(), "No song is currently playing.");
		}
	}

}
