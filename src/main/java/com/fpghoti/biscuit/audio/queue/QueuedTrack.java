package com.fpghoti.biscuit.audio.queue;

import java.awt.Color;

import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.util.Util;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class QueuedTrack {

	private Biscuit biscuit;
	private AudioTrack track;
	private String userId;
	private TextChannel channel;
	private boolean triedAlternative;

	public QueuedTrack(Biscuit biscuit, AudioTrack track, String userId, TextChannel channel) {
		this.biscuit = biscuit;
		this.track = track;
		this.userId = userId;
		this.channel = channel;
		this.triedAlternative = false;
	}

	public Biscuit getBiscuit() {
		return biscuit;
	}

	public AudioTrack getTrack() {
		return track;
	}

	public String getUserId() {
		return userId;
	}

	public Member getMember() {
		return biscuit.getGuild().getMemberById(userId);
	}

	public TextChannel getCommandChannel() {
		return channel;
	}

	public MessageEmbed getEmbedMessage(String prefix) {
		return getEmbedMessage(prefix, false);
	}

	public MessageEmbed getEmbedMessage(String prefix, boolean showRemaining) {
		EmbedBuilder embed = new EmbedBuilder();

		String title = track.getInfo().title;
		if(prefix != null && !prefix.equals("")) {
			title = prefix + " " + title;
		}

		embed.setTitle(title, track.getInfo().uri);
		embed.setColor(Color.CYAN);

		String desc = "Author: " + track.getInfo().author + "\nLength: " + Util.getTime(track.getDuration());

		if(showRemaining) {
			desc = desc + "\nTime Remaining: " + Util.getTime(track.getDuration() - track.getPosition());
		}

		embed.setDescription(desc);

		String name = biscuit.getGuild().getSelfMember().getEffectiveName();
		String avatar = biscuit.getGuild().getSelfMember().getUser().getEffectiveAvatarUrl();

		Member m = getMember();
		if(m != null) {
			name = m.getEffectiveName();
			avatar = m.getUser().getEffectiveAvatarUrl();
		}

		embed.setAuthor(name, null, avatar);

		if(isYouTube()) {
			embed.setThumbnail("https://img.youtube.com/vi/" + track.getIdentifier() + "/mqdefault.jpg");
		}

		return embed.build();
	}
	

	public boolean triedAlternative() {
		return triedAlternative;
	}

	//Indicates an attempt has been made to search for another video
	//Prevent and endless loop
	public void useAttempt() {
		triedAlternative = true;
	}
	
	public boolean isYouTube() {
		return track.getInfo().uri.contains("https://www.youtube.com/watch?v=");
	}

}
