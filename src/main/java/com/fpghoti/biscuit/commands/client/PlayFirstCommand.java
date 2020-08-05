package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.audio.AudioHandler;
import com.fpghoti.biscuit.audio.AudioResultHandler;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayFirstCommand extends MusicClientCommand{

	public PlayFirstCommand() {
		name = "Play First";
		description = "Places specified song at the front of the queue.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "playfirst <YouTube link OR Search Phrase>";
		minArgs = 1;
		maxArgs = 2000;
		identifiers.add("playfirst");
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
		
		if(!PermUtil.isMod(event.getMember())) {
			MessageText.send(tchannel, "You do not have permission to do this!");
			return;
		}
		
		if(!event.getMember().getVoiceState().inVoiceChannel()) {
			MessageText.send(tchannel, "You must be in a voice channel to do this!");
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
				MessageText.send(tchannel, "You are not in a channel that is authorized to use the music player.");
				return;
			}
		}

		if(b.getAudioPlayer().getPlayingTrack() != null && guild.getAudioManager().isConnected() && !guild.getAudioManager().getConnectedChannel().getMembers().contains(event.getMember())) {
			MessageText.send(tchannel, "Music is already playing in a voice channel. Connect to "
					+ "that channel, then queue your song.");
			return;
		}

		VoiceChannel channel = guild.getVoiceChannelsByName(vcname, true).get(0);
		AudioManager manager = guild.getAudioManager();

		manager.setSendingHandler(new AudioHandler(b.getAudioPlayer()));
		manager.openAudioConnection(channel);	

		AudioPlayerManager playerManager = Main.getPlayerManager();
		String vidid = getID(event.getMessage().getContentRaw().split(" ")[1]);

		playerManager.loadItemOrdered(guild, vidid, new AudioResultHandler(event.getAuthor().getId(), tchannel, false, searchPhrase, true, false));
	}

	private String getID(String url) {
		String[] s = url.split("=");
		if(s.length > 1) {
			return s[1].split("&")[0];
		}
		return url;
	}

}
