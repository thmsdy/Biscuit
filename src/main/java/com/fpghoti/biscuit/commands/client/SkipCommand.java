package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SkipCommand extends MusicClientCommand{

	public SkipCommand() {
		name = "Skip";
		description = "Sends a vote to skip the song that is currently playing.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "skip";
		minArgs = 0;
		maxArgs = 0;
		identifiers.add("skip");
	}

	@Override
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		
		//TODO Redo vote skipping. It does not function correctly.
		
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -skip");

		if(b.getAudioPlayer().getPlayingTrack() == null) {
			event.getChannel().sendMessage("There is not a song playing for you to skip!").queue();
			return;
		}

		if(!event.getGuild().getAudioManager().getConnectedChannel().getMembers().contains(event.getMember())) {
			event.getChannel().sendMessage("You need to be in the same voice channel in order to skip!").queue();
			return;
		}

		if(b.getAudioScheduler().getVotes() >= (event.getGuild().getAudioManager().getConnectedChannel().getMembers().size() - 1) / 2) {
			event.getChannel().sendMessage("Skip vote status: **[" + ( b.getAudioScheduler().getVotes() + 1) + "/" 
					+ (event.getGuild().getAudioManager().getConnectedChannel().getMembers().size() - 1) + "]**"
					+ "\nSkipping current track.").queue();
			if(b.getAudioScheduler().getQueue().getNext() != null ) {
				MessageEmbed next = b.getAudioScheduler().getQueue().getNext().getEmbedMessage("Now Playing:");
				event.getChannel().sendMessage(next).queue();
			}
			b.getAudioScheduler().skip();
			return;
		}
		if(b.getAudioScheduler().voteSkip(event.getAuthor().getId())) {
			event.getChannel().sendMessage("You voted to skip the current track.").queue();
		}else {
			event.getChannel().sendMessage("You cannot vote to skip this track again."
					+ "\nSkip vote status: **[" + b.getAudioScheduler().getVotes() + "/" 
					+ (event.getGuild().getAudioManager().getConnectedChannel().getMembers().size() - 1) + "]**").queue();
		}

		if(b.getAudioScheduler().getVotes() >= (event.getGuild().getAudioManager().getConnectedChannel().getMembers().size() - 1) / 2) {
			event.getChannel().sendMessage("Skip vote status: **[" + b.getAudioScheduler().getVotes() + "/" 
					+ event.getGuild().getAudioManager().getConnectedChannel().getMembers().size() + "]**"
					+ "\nSkipping current track.").queue();
			if(b.getAudioScheduler().getQueue().getNext() != null ) {
				MessageEmbed next = b.getAudioScheduler().getQueue().getNext().getEmbedMessage("Now Playing:");
				event.getChannel().sendMessage(next).queue();
			}
			b.getAudioScheduler().skip();
		}

	}

}
