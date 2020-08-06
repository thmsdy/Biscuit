package com.fpghoti.biscuit.commands.client.music;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.audio.PlayCommandUtil;
import com.fpghoti.biscuit.audio.request.youtube.YTRequest;
import com.fpghoti.biscuit.audio.result.YTResultHandler;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

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

		String searchPhrase = PlayCommandUtil.getSearchPhrase(args);

		b.log(event.getAuthor().getName() + " issued a command: -play " + searchPhrase);

		boolean connected = PlayCommandUtil.connectToChannel(event);
		if(!connected) {
			return;
		}

		YTRequest request = new YTRequest(event.getMessage(), searchPhrase);
		Main.getPlayerManager().loadItemOrdered(guild, PlayCommandUtil.getID(event),  new YTResultHandler(request));
	}

}
