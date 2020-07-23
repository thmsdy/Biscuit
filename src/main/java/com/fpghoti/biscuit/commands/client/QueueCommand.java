package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.audio.queue.AudioQueue;
import com.fpghoti.biscuit.audio.queue.QueuedTrack;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.util.Util;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class QueueCommand extends MusicClientCommand{

	public QueueCommand() {
		name = "Queue";
		description = "Displays the upcoming songs in the music queue.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "queue [Page #]";
		minArgs = 0;
		maxArgs = 1;
		identifiers.add("queue");
	}

	@Override
	public void execute(String[] args, GuildMessageReceivedEvent event) {

		Biscuit biscuit = Biscuit.getBiscuit(event.getGuild());
		biscuit.log(event.getAuthor().getName() + " issued a command: -queue");
		int pg = 1;
		if (args.length > 0) {
			if(Util.isDigit(args[0])) {
				pg = Integer.parseInt(args[0]);
			}else {
				event.getChannel().sendMessage("Usage: ``" + usage + "``").queue();
			}
		}

		AudioQueue queue = biscuit.getAudioScheduler().getQueue();

		int pageCount = (int) Math.ceil((double) queue.size() / 8);
		if (pg > pageCount) {
			pg = pageCount;
		}
		
		if(queue.size() == 0) {
			if(biscuit.getAudioPlayer().getPlayingTrack() == null) {
				event.getChannel().sendMessage("There is currently no song playing.").queue();
			}else {
				event.getChannel().sendMessage("Nothing is queued to play after the current track.").queue();
			}
			return;
		}

		event.getChannel().sendMessage("**Use " + Main.getMainBiscuit().getProperties().getCommandSignifier() + "queue [Page #] to navigate the different pages.**").queue();
		event.getChannel().sendMessage("[" + Integer.toString(pg) + "/" + Integer.toString(pageCount) + "] ** Upcoming Music Tracks:**").queue();
		String msg = "";
		for (int i = 0; i < 8; i++) {
			int index = (pg - 1) * 8 + i;
			int count = index + 1;
			String line = "";
			if (index < queue.size()) {
				QueuedTrack track = queue.getTrack(index);
				AudioTrack a = track.getTrack();
				Member m = track.getMember();
				line = "**" + count + ".** " + a.getInfo().title + " [" + Util.getTime(a.getInfo().length) + "]";
				if(m != null) {
					line = line + " Added by: **" + m.getEffectiveName() + "**";
				}
			}
			if(!(index + 1 >= queue.size() || index == 7)) {
				line = line + "\n";
			}
			if (index < queue.size()) {
				msg = msg + line;
			}
		}
		event.getChannel().sendMessage(msg).queue();

	}


}
