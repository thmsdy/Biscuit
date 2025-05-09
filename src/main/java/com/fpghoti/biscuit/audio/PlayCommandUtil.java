package com.fpghoti.biscuit.audio;

import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.rest.MessageText;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommandUtil {
	
	public static boolean connectToChannel(MessageReceivedEvent event) {
		if(!event.isFromGuild()) {
			return false;
		}
		Guild guild = event.getGuild();
		Biscuit biscuit = Biscuit.getBiscuit(guild);
		TextChannel textChannel = event.getChannel().asTextChannel();
		
		String vcname = "";
		if(!event.getMember().getVoiceState().inAudioChannel()) {
			MessageText.send(textChannel, "You must be in a voice channel to do this!");
			return false;
		}
		vcname = event.getMember().getVoiceState().getChannel().getName();
		String[] musicChannels = biscuit.getProperties().getMusicChannels();
		if(musicChannels.length >= 1) {	
			boolean found = false;
			for(String cname : musicChannels) {
				if(event.getMember().getVoiceState().getChannel().getName().equalsIgnoreCase(cname)) {
					found = true;
					break;
				}
			}
			if(found == false) {
				MessageText.send(textChannel, "You are not in a channel that is authorized to use the music player.");
				return false;
			}
		}

		if(biscuit.getAudioPlayer().getPlayingTrack() != null && guild.getAudioManager().isConnected() && !guild.getAudioManager().getConnectedChannel().getMembers().contains(event.getMember())) {
			MessageText.send(textChannel, "Music is already playing in a voice channel. Connect to "
					+ "that channel, then queue your song.");
			return false;
		}

		VoiceChannel voiceChannel = guild.getVoiceChannelsByName(vcname, true).get(0);
		AudioManager manager = guild.getAudioManager();

		manager.setSendingHandler(new AudioHandler(biscuit.getAudioPlayer()));
		manager.openAudioConnection(voiceChannel);	
		return true;
	}
	
	public static String getSearchPhrase(String[] args) {
		return getSearchPhrase(args, 0);
	}
	
	public static String getSearchPhrase(String[] args, int startArg) {
		if(startArg >= args.length) {
			return "";
		}
		String searchPhrase = args[startArg];
		if(args.length > startArg + 1) {
			for(int i = startArg + 1; i < args.length; i++) {
				searchPhrase = searchPhrase + " " + args[i];
			}
		}
		return searchPhrase;
	}
	
	public static String getID(MessageReceivedEvent event) {
		return getID(event.getMessage().getContentRaw().split(" ")[1]);
	}
	
	public static String getID(String url) {
		String[] s = url.split("=");
		if(s.length > 1) {
			return s[1].split("&")[0];
		}
		return url;
	}

}
