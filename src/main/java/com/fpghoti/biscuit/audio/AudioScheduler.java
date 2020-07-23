package com.fpghoti.biscuit.audio;

import java.util.ArrayList;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.audio.queue.AudioQueue;
import com.fpghoti.biscuit.audio.queue.QueuedTrack;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.api.entities.TextChannel;

public class AudioScheduler extends AudioEventAdapter {

	private Biscuit biscuit;
	private AudioQueue queue;
	private ArrayList<String> skips;
	private boolean loop;

	public AudioScheduler(Biscuit biscuit) {
		this.biscuit = biscuit;
		this.queue = new AudioQueue();
		this.skips = new ArrayList<String>();
		this.loop = false;
	}

	@Override
	public void onPlayerPause(AudioPlayer player) {
		log("The current track was paused.");
	}

	@Override
	public void onPlayerResume(AudioPlayer player) {
		log("The current track was unpaused.");
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		log("Starting track " + track.getInfo().title + ".");
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

		QueuedTrack qt = queue.getLastTrack();
		String title = track.getInfo().title;

		switch(endReason) {

		case LOAD_FAILED:
			warn("Something went wrong while trying to load the current track. Trying alternate track.");
			if(!qt.triedAlternative()) {
				qt.useAttempt();
				TextChannel channel = qt.getCommandChannel();
				channel.sendMessage("The video selected cannot be played through the music player. An alternate track will be played if available.").queue();
				title = title.toLowerCase().replace("vevo", "") + " lyrics";
				Main.getPlayerManager().loadItemOrdered(biscuit.getGuild(),"ytsearch:" + title, new AudioResultHandler(qt.getUserId(), channel, true, title, true, !queue.isEmpty()));
				return;
			}
			break;
		case CLEANUP:
			log("A track stopped playing due to audio player cleanup.");
			if(qt != null && qt.getCommandChannel() != null)
				qt.getCommandChannel().sendMessage("Track **" + track.getInfo().title + "** stopped playing due to audio player cleanup.").queue();
			break;
		case FINISHED:
			log("Finished playing track " + title + ".");
			break;
		case REPLACED:
			log("Skipped track " + title + ".");
			break;
		case STOPPED:
			log("Stopped playing track " + title + ".");
			break;
		default:
			break;

		}

		if (endReason.mayStartNext) {
			if(loop) {
				queue(track.makeClone(), qt.getUserId(), qt.getCommandChannel(), 1);
			}
			startPlaying();
		}
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		QueuedTrack qt = queue.getPreviousTrack(track);
		if(!qt.triedAlternative()) {
			return;
		}
		warn("No alternative track found. This means the video is most likely unavailable. The next track will be played instead if available.");
		startPlaying();
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
		QueuedTrack qt = queue.getPreviousTrack(track);
		if(qt != null && qt.getCommandChannel() != null) {
			qt.getCommandChannel().sendMessage("The current track has become stuck and will be skipped.").queue();
		}
	}

	public void queue(AudioTrack track, String uid, TextChannel channel) {
		queue(track, uid, channel, null);
	}

	public void queue(AudioTrack track, String uid, TextChannel channel, Integer place) {
		if(queue.isEmpty() && biscuit.getAudioPlayer().getPlayingTrack() == null) {
			QueuedTrack qt = new QueuedTrack(biscuit, track, uid, channel);
			queue.sendQueueMessage(qt);
			queue.addPreviousTrack(qt);
			biscuit.getAudioPlayer().playTrack(track);
		}else {
			if(place != null) {
				queue.addAtPlace(new QueuedTrack(biscuit, track, uid, channel), place);
			}else {
				queue.add(new QueuedTrack(biscuit, track, uid, channel));
			}
		}
	}

	public void startPlaying() {
		if(queue.isEmpty()) {
			if(loop) {
				return;
			}			
			biscuit.getGuild().getAudioManager().closeAudioConnection();
			skips.clear();
			return;
		}
		queue.playNext();
		skips.clear();
	}

	public void setLooping(boolean b) {
		loop = b;
	}

	public boolean isLooping() {
		return loop;
	}

	public boolean hasSkipped(String id) {
		return skips.contains(id);
	}

	public boolean voteSkip(String id) {
		if(!hasSkipped(id)) {
			skips.add(id);
			return true;
		}
		return false;
	}

	public int getVotes() {
		return skips.size();
	}

	public AudioQueue getQueue(){
		return queue;
	}

	public void skip() {
		biscuit.getAudioPlayer().stopTrack();
		startPlaying();
	}

	public void wipeQueue() {
		queue.clear();
	}

	public Biscuit getBiscuit() {
		return biscuit;
	}

	public void log(String message) {
		if(biscuit.getProperties().logMusicPlayer()) {
			biscuit.log(message);
		}
	}

	public void warn(String message) {
		if(biscuit.getProperties().logMusicPlayer()) {
			biscuit.warn(message);
		}
	}

}