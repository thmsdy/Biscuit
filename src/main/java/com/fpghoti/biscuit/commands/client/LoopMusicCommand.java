package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LoopMusicCommand extends MusicClientCommand{

	public LoopMusicCommand() {
		name = "Loop Music";
		description = "Enables looping of all songs played.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "loopmusic";
		minArgs = 0;
		maxArgs = 0;
		identifiers.add("loopmusic");
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -loopmusic");
		if(PermUtil.isMod(event.getMember())) {
			if(!b.getAudioScheduler().isLooping()) {
				event.getChannel().sendMessage("Setting all music to loop.").queue();
				b.getAudioScheduler().setLooping(true);
			}else {
				event.getChannel().sendMessage("Disabling music looping.").queue();
				b.getAudioScheduler().setLooping(false);
			}

		}
	}

}
