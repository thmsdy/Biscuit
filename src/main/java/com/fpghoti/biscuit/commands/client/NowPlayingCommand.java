package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

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
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -nowplaying");
		if(b.getAudioPlayer().getPlayingTrack() != null) {
			AudioTrack track = b.getAudioPlayer().getPlayingTrack();
			event.getChannel().sendMessage(b.getAudioScheduler().getMessage(track, true)).queue();
		}else {
			event.getChannel().sendMessage("No song is currently playing.").queue();
		}
	}

}
