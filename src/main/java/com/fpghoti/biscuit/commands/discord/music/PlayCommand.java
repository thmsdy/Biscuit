package com.fpghoti.biscuit.commands.discord.music;

import java.io.File;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.audio.AudioHandler;
import com.fpghoti.biscuit.audio.request.PlayRequest;
import com.fpghoti.biscuit.audio.request.RequestType;
import com.fpghoti.biscuit.audio.request.file.FileRequest;
import com.fpghoti.biscuit.audio.request.soundcloud.SCRequest;
import com.fpghoti.biscuit.audio.request.youtube.YTRequest;
import com.fpghoti.biscuit.audio.result.FileResultHandler;
import com.fpghoti.biscuit.audio.result.ResultHandler;
import com.fpghoti.biscuit.audio.result.SCResultHandler;
import com.fpghoti.biscuit.audio.result.YTResultHandler;
import com.fpghoti.biscuit.audio.util.AudioUtil;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand extends MusicClientCommand{

	public PlayCommand() {
		name = "Play";
		description = "Plays the specified song or plays a song found using search parameters.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "play [-sc] <Link OR Search Phrase>";
		minArgs = 1;
		maxArgs = 2000;
		identifiers.add("play");
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Guild guild = event.getGuild();
		BiscuitGuild b = BiscuitGuild.getBiscuitGuild(guild);

		int searchArg = 0;

		boolean playFirst = false;
		boolean scSearch = false;
		boolean isFile = false;

		if(args[0].equalsIgnoreCase("-first")) {
			searchArg += 1;
			if(!PermUtil.hasMusicControl(event.getMember())) {
				MessageText.send(event.getChannel().asTextChannel(), "You do not have permission to do this!");
				return;
			}
			playFirst = true;
		}

		if(args[searchArg].equalsIgnoreCase("-sc")) {
			scSearch = true;
			searchArg += 1;
		}else if (args[searchArg].equalsIgnoreCase("-file") || args[searchArg].equalsIgnoreCase("-fileid") || args[searchArg].equalsIgnoreCase("-fid")) {
			isFile = true;
			searchArg += 1;
		}

		String searchPhrase = getSearchPhrase(args, searchArg);

		b.log(event.getAuthor().getName() + " issued a command: -play " + searchPhrase);

		boolean connected = connectToChannel(event);
		if(!connected) {
			return;
		}

		PlayRequest request;
		ResultHandler handler;

		if(scSearch) {
			if(playFirst) {
				request = PlayRequest.createPlayRequest(RequestType.SOUNDCLOUD_PRIORITY, event.getMessage(), searchPhrase, 1);
			} else {
				request = PlayRequest.createPlayRequest(RequestType.SOUNDCLOUD, event.getMessage(), searchPhrase);
			}
			handler = new SCResultHandler((SCRequest)request);
		}else if(isFile) {
			File file = AudioUtil.getAudioFile(searchPhrase);
			if(file == null) {
				MessageText.send(event.getChannel().asTextChannel(), "Unable to locate file.");
				if(b.getAudioScheduler().getQueue().isEmpty()) {
					b.getGuild().getAudioManager().closeAudioConnection();
				}
				return;
			}
			String filename = file.getPath();
			if(playFirst) {
				request = PlayRequest.createPlayRequest(RequestType.FILE_PRIORITY, event.getMessage(), filename, 1);
			}else {
				request = PlayRequest.createPlayRequest(RequestType.FILE, event.getMessage(), filename);
			}
			
			handler = new FileResultHandler((FileRequest)request);

			Main.getPlayerManager().loadItemOrdered(guild, filename,  handler);
			return;
		}else {
			if(playFirst) {
				request = PlayRequest.createPlayRequest(event.getMessage(), searchPhrase, 1);
			}else {
				request = PlayRequest.createPlayRequest(event.getMessage(), searchPhrase);
			}
			handler = new YTResultHandler((YTRequest)request);
		}

		Main.getPlayerManager().loadItemOrdered(guild, getID(event),  handler);
	}
	
	public static boolean connectToChannel(MessageReceivedEvent event) {
		if(!event.isFromGuild()) {
			return false;
		}
		Guild guild = event.getGuild();
		BiscuitGuild biscuit = BiscuitGuild.getBiscuitGuild(guild);
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
	
	private String getSearchPhrase(String[] args, int startArg) {
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
	
	private String getID(MessageReceivedEvent event) {
		return getID(event.getMessage().getContentRaw().split(" ")[1]);
	}
	
	private String getID(String url) {
		String[] s = url.split("=");
		if(s.length > 1) {
			return s[1].split("&")[0];
		}
		return url;
	}

}
