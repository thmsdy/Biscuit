package com.fpghoti.biscuit.commands.client.music;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.audio.PlayCommandUtil;
import com.fpghoti.biscuit.audio.request.youtube.YTPriorityRequest;
import com.fpghoti.biscuit.audio.request.youtube.YTRequest;
import com.fpghoti.biscuit.audio.result.YTResultHandler;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

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

		String searchPhrase = PlayCommandUtil.getSearchPhrase(args);

		b.log(event.getAuthor().getName() + " issued a command: -playfirst " + searchPhrase);
		
		if(!PermUtil.hasMusicControl(event.getMember())) {
			MessageText.send(tchannel, "You do not have permission to do this!");
			return;
		}
		
		boolean connected = PlayCommandUtil.connectToChannel(event);
		if(!connected) {
			return;
		}

		YTRequest request = new YTPriorityRequest(event.getMessage(), searchPhrase, 1);
		Main.getPlayerManager().loadItemOrdered(guild, PlayCommandUtil.getID(event),  new YTResultHandler(request));
	}

}
