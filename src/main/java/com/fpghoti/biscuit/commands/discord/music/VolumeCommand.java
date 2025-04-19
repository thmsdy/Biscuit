package com.fpghoti.biscuit.commands.discord.music;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class VolumeCommand extends MusicClientCommand{
	
    public VolumeCommand() {
        name = "Volume";
        description = "Changes the player volume or displays the current volume.";
        usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "volume [0-150]";
        minArgs = 0;
        maxArgs = 1;
        identifiers.add("volume");
        identifiers.add("vol");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		
		if(args.length < 1) {
			b.log(event.getAuthor().getName() + " issued a command: -volume");
			MessageText.send(event.getChannel().asTextChannel(), "The current volume is: **" + b.getAudioPlayer().getVolume() + "**.");
			return;
		}
		
		b.log(event.getAuthor().getName() + " issued a command: -volume " + args[0]);
		
		if(PermUtil.hasMusicControl(event.getMember())) {
			if(Util.isDigit(args[0])) {
				int vol = Integer.parseInt(args[0]);
				if(vol < 0) {
					vol = 0;
				}else if(vol > 150) {
					vol = 150;
				}
				b.getAudioPlayer().setVolume(vol);
				MessageText.send(event.getChannel().asTextChannel(), "The volume was set to **" + b.getAudioPlayer().getVolume() + "**.");
			}
		}
	}

}
