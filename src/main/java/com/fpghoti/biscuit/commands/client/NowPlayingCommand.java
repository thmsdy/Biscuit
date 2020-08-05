package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.rest.MessageText;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

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
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -nowplaying");
		if(b.getAudioScheduler().getQueue().getLastTrack() != null ) {
			MessageEmbed next = b.getAudioScheduler().getQueue().getLastTrack().getEmbedMessage("Now Playing:", true);
			MessageText.send(event.getChannel(), next);
		}else {
			MessageText.send(event.getChannel(), "No song is currently playing.");
		}
	}

}
