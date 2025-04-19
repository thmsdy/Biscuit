package com.fpghoti.biscuit.audio.queue;

import java.util.ArrayList;
import java.util.Collections;

import com.fpghoti.biscuit.audio.request.RequestType;
import com.fpghoti.biscuit.rest.MessageText;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class AudioQueue {

	private ArrayList<QueuedTrack> tracks;
	private ArrayList<QueuedTrack> previousTracks;

	public AudioQueue() {
		this.tracks = new ArrayList<QueuedTrack>();
		this.previousTracks = new ArrayList<QueuedTrack>();
	}

	public ArrayList<QueuedTrack> getUserTracks(String userId){
		ArrayList<QueuedTrack> qts = new ArrayList<QueuedTrack>();
		for(QueuedTrack qt : tracks) {
			if(qt.getUserId().equals(userId)) {
				qts.add(qt);
			}
		}
		return qts;
	}

	public void removeUserTracks(String userId) {
		for(QueuedTrack qt : getUserTracks(userId)) {
			tracks.remove(qt);
		}
	}
	
	public void shuffleUserTracks(String userId) {
		ArrayList<QueuedTrack> qts = getUserTracks(userId);
		Collections.shuffle(qts);
		int index = 0;
		for(int i = 0; i < tracks.size(); i++) {
			QueuedTrack track = tracks.get(i);
			if(track.getUserId().equals(userId)) {
				tracks.set(i, qts.get(index));
				index++;
			}
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

	public QueuedTrack getPreviousTrack(int fromEnd) {
		if(previousTracks.isEmpty()) {
			return null;
		}
		int index = previousTracks.size() - fromEnd;
		if(index < 0) {
			return null;
		}
		return previousTracks.get(index);
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
			MessageText.send(c, m);
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
	
	//Goes by viewable place rather than index for
	//easy implementation into command.
	public boolean removeTracksBefore(int place) {
		int index = place - 1;
		if(index < 1) {
			return false;
		}
		ArrayList<QueuedTrack> trs = new ArrayList<QueuedTrack>(tracks);
		int count = 0;
		for(QueuedTrack t : trs) {
			if(count < index) {
				tracks.remove(t);
			}else {
				return true;
			}
			count++;
		}
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
	
	public boolean isStuckLooping() {
		QueuedTrack last = getLastTrack();
		if(last == null) {
			return false;
		}
		if(last.getType() == RequestType.YOUTUBE_IMMEDIATE) {
			QueuedTrack beforeLast = getPreviousTrack(2);
			if(beforeLast == null) {
				return false;
			}
			if(beforeLast.getTrack().getIdentifier().equals(last.getTrack().getIdentifier())) {
				return true;
			}
		}
		return false;
	}

}
