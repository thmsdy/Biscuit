package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.audio.AudioHandler;
import com.fpghoti.biscuit.audio.AudioResultHandler;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand extends MusicClientCommand{

	public PlayCommand() {
		name = "Play";
		description = "Plays the specified song or plays a song found using search parameters.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "play <YouTube link OR Search Phrase>";
		minArgs = 1;
		maxArgs = 2000;
		identifiers.add("play");
	}

	@Override
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		Guild guild = event.getGuild();
		Biscuit b = Biscuit.getBiscuit(guild);
		TextChannel tchannel = event.getChannel();

		String searchPhrase = args[0];
		if(args.length > 1) {
			for(int i = 1; i < args.length; i++) {
				searchPhrase = searchPhrase + " " + args[i];
			}
		}

		b.log(event.getAuthor().getName() + " issued a command: -play " + searchPhrase);

		String vcname = "";
		if(!event.getMember().getVoiceState().inVoiceChannel()) {
			tchannel.sendMessage("You must be in a voice channel to do this!").queue();
			return;
		}
		vcname = event.getMember().getVoiceState().getChannel().getName();
		String[] musicChannels = b.getProperties().getMusicChannels();
		if(musicChannels.length >= 1) {	
			boolean found = false;
			for(String cname : musicChannels) {
				if(event.getMember().getVoiceState().getChannel().getName().equalsIgnoreCase(cname)) {
					found = true;
					break;
				}
			}
			if(found == false) {
				tchannel.sendMessage("You are not in a channel that is authorized to use the music player.").queue();
				return;
			}
		}

		if(b.getAudioPlayer().getPlayingTrack() != null && guild.getAudioManager().isConnected() && !guild.getAudioManager().getConnectedChannel().getMembers().contains(event.getMember())) {
			tchannel.sendMessage("Music is already playing in a voice channel. Connect to "
					+ "that channel, then queue your song.").queue();
			return;
		}

		VoiceChannel channel = guild.getVoiceChannelsByName(vcname, true).get(0);
		AudioManager manager = guild.getAudioManager();

		manager.setSendingHandler(new AudioHandler(b.getAudioPlayer()));
		manager.openAudioConnection(channel);	

		AudioPlayerManager playerManager = Main.getPlayerManager();
		String vidid = getID(event.getMessage().getContentRaw().split(" ")[1]);

		playerManager.loadItemOrdered(guild, vidid, new AudioResultHandler(event.getAuthor().getId(), tchannel, false, searchPhrase, false, false));
	}

	private String getID(String url) {
		String[] s = url.split("=");
		if(s.length > 1) {
			return s[1].split("&")[0];
		}
		return url;
	}

}
