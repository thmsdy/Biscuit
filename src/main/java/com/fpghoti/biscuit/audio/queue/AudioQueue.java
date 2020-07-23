package com.fpghoti.biscuit.audio.queue;

import java.util.ArrayList;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class AudioQueue {

	private ArrayList<QueuedTrack> tracks;
	private ArrayList<QueuedTrack> previousTracks;

	public AudioQueue() {
		this.tracks = new ArrayList<QueuedTrack>();
		this.previousTracks = new ArrayList<QueuedTrack>();
	}

	public ArrayList<QueuedTrack> getUserTracks(String userId){
		ArrayList<QueuedTrack> tracks = new ArrayList<QueuedTrack>();
		for(QueuedTrack qt : tracks) {
			if(qt.getUserId().equals(userId)) {
				tracks.add(qt);
			}
		}
		return tracks;
	}

	public void removeUserTracks(String userId) {
		for(QueuedTrack qt : getUserTracks(userId)) {
			tracks.remove(qt);
		}
	}

	public void playNext() {
		QueuedTrack next = tracks.iterator().next();
		tracks.remove(next);
		addPreviousTrack(next);
		next.getBiscuit().getAudioPlayer().playTrack(next.getTrack());
	}

	public QueuedTrack getTrack(int index){
		return tracks.get(index);
	}

	public QueuedTrack getTrack(AudioTrack track) {
		for(QueuedTrack qt : tracks) {
			if(qt.getTrack() == track) {
				return qt;
			}
		}
		return null;
	}

	public QueuedTrack getPreviousTrack(AudioTrack track) {
		for(QueuedTrack qt : previousTracks) {
			if(qt.getTrack() == track) {
				return qt;
			}
		}
		return null;
	}

	public QueuedTrack getLastTrack() {
		if(previousTracks.isEmpty()) {
			return null;
		}
		return previousTracks.get(previousTracks.size() - 1);
	}

	public void addPreviousTrack(QueuedTrack track) {
		//AudioQueue will remember up to three
		//previous tracks for the purposes of
		//reporting errors ect.
		previousTracks.add(track);
		if(previousTracks.size() > 3) {
			previousTracks.remove(0);
		}
	}

	public void add(QueuedTrack track) {
		sendQueueMessage(track);
		tracks.add(track);
	}

	//Goes by viewable place rather than index for
	//easy implementation into command.
	public boolean addAtPlace(QueuedTrack track, int place) {
		if(place < 1) {
			return false;
		}
		int index = place - 1;
		if(index > tracks.size()) {
			index = tracks.size();
		}
		sendQueueMessage(track);
		tracks.add(index, track);
		return true;
	}
	
	public void sendQueueMessage(QueuedTrack track) {
		if(track.getCommandChannel() != null) {
			TextChannel c = track.getCommandChannel();
			MessageEmbed m = track.getEmbedMessage("Queued:");
			c.sendMessage(m).queue();
		}
	}

	//Goes by viewable place rather than index for
	//easy implementation into command.
	public boolean moveToPlace(int oldPlace, int newPlace) {
		QueuedTrack track = tracks.get(oldPlace - 1);
		if(track == null) {
			return false;
		}
		tracks.remove(track);
		return addAtPlace(track, newPlace);
	}

	//Goes by viewable place rather than index for
	//easy implementation into command.
	public boolean removeTrack(int place) {
		QueuedTrack track = tracks.get(place - 1);
		if(track == null) {
			return false;
		}
		tracks.remove(track);
		return true;
	}

	public QueuedTrack getNext() {
		if(isEmpty()) {
			return null;
		}
		return tracks.get(0);
	}

	public boolean isEmpty() {
		return tracks.isEmpty();
	}

	public void clear() {
		tracks.clear();
	}

	public int size() {
		return tracks.size();
	}

}
