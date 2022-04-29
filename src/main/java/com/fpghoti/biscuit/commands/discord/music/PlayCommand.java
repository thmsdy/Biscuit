package com.fpghoti.biscuit.commands.discord.music;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.audio.PlayCommandUtil;
import com.fpghoti.biscuit.audio.request.PlayRequest;
import com.fpghoti.biscuit.audio.request.RequestType;
import com.fpghoti.biscuit.audio.request.soundcloud.SCRequest;
import com.fpghoti.biscuit.audio.request.youtube.YTRequest;
import com.fpghoti.biscuit.audio.result.ResultHandler;
import com.fpghoti.biscuit.audio.result.SCResultHandler;
import com.fpghoti.biscuit.audio.result.YTResultHandler;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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
		Biscuit b = Biscuit.getBiscuit(guild);
		
		int searchArg = 0;
		boolean scSearch = false;
		
		if(args[0].equalsIgnoreCase("-sc")) {
			scSearch = true;
			searchArg = 1;
		}

		String searchPhrase = PlayCommandUtil.getSearchPhrase(args, searchArg);

		b.log(event.getAuthor().getName() + " issued a command: -play " + searchPhrase);

		boolean connected = PlayCommandUtil.connectToChannel(event);
		if(!connected) {
			return;
		}
		
		PlayRequest request;
		ResultHandler handler;
		
		if(scSearch) {
			request = PlayRequest.createPlayRequest(RequestType.SOUNDCLOUD, event.getMessage(), searchPhrase);
			handler = new SCResultHandler((SCRequest)request);
		}else {
			request = PlayRequest.createPlayRequest(event.getMessage(), searchPhrase);
			handler = new YTResultHandler((YTRequest)request);
		}
		
		Main.getPlayerManager().loadItemOrdered(guild, PlayCommandUtil.getID(event),  handler);
	}

}
