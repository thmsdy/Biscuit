package com.fpghoti.biscuit.audio;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.util.Util;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.TextChannel;

public class AudioResultHandler implements AudioLoadResultHandler {

	private Biscuit biscuit;
	private String uid;
	private TextChannel channel;
	private String searchPhrase;
	private boolean search;
	private boolean first;
	private boolean stop;
	
	public AudioResultHandler(Biscuit biscuit, String uid, TextChannel channel, boolean search,
			String searchPhrase, boolean first, boolean stop) {
		this.biscuit = biscuit;
		this.uid = uid;
		this.channel = channel;
		this.search = search;
		this.searchPhrase = searchPhrase;
		this.first = first;
		this.stop = stop;
	}

	public AudioResultHandler(Biscuit biscuit, String uid, TextChannel channel, boolean search,
			String searchPhrase, boolean first) {
		this.biscuit = biscuit;
		this.uid = uid;
		this.channel = channel;
		this.search = search;
		this.searchPhrase = searchPhrase;
		this.first = first;
		this.stop = false;
	}

	public AudioResultHandler(Biscuit biscuit, String uid, TextChannel channel, boolean search, String searchPhrase) {
		this.biscuit = biscuit;
		this.uid = uid;
		this.channel = channel;
		this.search = search;
		this.searchPhrase = searchPhrase;
		this.first = false;
		this.stop = false;
	}

	@Override
	public void trackLoaded(AudioTrack track) {
		if(!stop) {
			//TODO fix vevo
			//Try to avoid Vevo results if possible.
			String title = track.getInfo().title;
			if(title.toLowerCase().contains("vevo") || track.getInfo().author.toLowerCase().contains("vevo")) {
				title = title.toLowerCase().replace("vevo", "") + " lyrics";
				channel.sendMessage("You tried to load a Vevo video. These are not compatible with the music player."
						+ " Trying to find alternate song...").queue();
				Main.getPlayerManager().loadItemOrdered(biscuit.getGuild(),"ytsearch:" + title, new AudioResultHandler(biscuit, uid, channel, true, title, first, true));
				return;
			}
		}
		
		channel.sendMessage("**Adding to queue: **\n```" +  track.getInfo().title + "\nBy: "
				+ track.getInfo().author + "\nLength: " + Util.getTime(track.getDuration()) +"```").queue();
		if(first) {
			biscuit.getAudioScheduler().queueFirst(track, uid, channel);
		}else {
			biscuit.getAudioScheduler().queue(track, uid, channel);
		}
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		AudioTrack track = playlist.getTracks().get(0);
		channel.sendMessage("**Adding to queue: **\n" + track.getInfo().uri + "\n```" +  track.getInfo().title + "\nBy: "
				+ track.getInfo().author + "\nLength: " + Util.getTime(track.getDuration()) +"```").queue();
		biscuit.getAudioScheduler().queue(track, uid);
	}


	@Override
	public void noMatches() {
		if(!search) {
			biscuit.log("Exact match not found. Searching instead...");
			Main.getPlayerManager().loadItemOrdered(biscuit.getGuild(),"ytsearch:" + searchPhrase, new AudioResultHandler(biscuit, uid, channel, true, searchPhrase));
		}else {
			channel.sendMessage("Song match not found.").queue();
		}
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		exception.printStackTrace();
		channel.sendMessage("An error was encountered while attempting to load audio.").queue();
	}

}
