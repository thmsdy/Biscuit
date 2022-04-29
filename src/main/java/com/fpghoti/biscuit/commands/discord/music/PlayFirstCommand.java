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
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlayFirstCommand extends MusicClientCommand{

	public PlayFirstCommand() {
		name = "Play First";
		description = "Places specified song at the front of the queue.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "playfirst <Link OR Search Phrase>";
		minArgs = 1;
		maxArgs = 2000;
		identifiers.add("playfirst");
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Guild guild = event.getGuild();
		Biscuit b = Biscuit.getBiscuit(guild);
		TextChannel tchannel = event.getTextChannel();

		int searchArg = 0;
		boolean scSearch = false;
		
		if(args[0].equalsIgnoreCase("-sc")) {
			scSearch = true;
			searchArg = 1;
		}

		String searchPhrase = PlayCommandUtil.getSearchPhrase(args, searchArg);

		b.log(event.getAuthor().getName() + " issued a command: -playfirst " + searchPhrase);
		
		if(!PermUtil.hasMusicControl(event.getMember())) {
			MessageText.send(tchannel, "You do not have permission to do this!");
			return;
		}
		
		boolean connected = PlayCommandUtil.connectToChannel(event);
		if(!connected) {
			return;
		}
		
		PlayRequest request;
		ResultHandler handler;
		
		if(scSearch) {
			request = PlayRequest.createPlayRequest(RequestType.SOUNDCLOUD_PRIORITY, event.getMessage(), searchPhrase, 1);
			handler = new SCResultHandler((SCRequest)request);
		}else {
			request = PlayRequest.createPlayRequest(event.getMessage(), searchPhrase, 1);
			handler = new YTResultHandler((YTRequest)request);
		}
		
		Main.getPlayerManager().loadItemOrdered(guild, PlayCommandUtil.getID(event),  handler);
	}

}