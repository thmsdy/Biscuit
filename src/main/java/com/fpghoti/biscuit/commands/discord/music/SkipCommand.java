package com.fpghoti.biscuit.commands.discord.music;

import java.util.ArrayList;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.audio.AudioScheduler;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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
	public void execute(String[] args, MessageReceivedEvent event) {

		Guild guild = event.getGuild();
		BiscuitGuild biscuit = BiscuitGuild.getBiscuitGuild(guild);
		biscuit.log(event.getAuthor().getName() + " issued a command: -skip");

		TextChannel cmdChannel = event.getChannel().asTextChannel();

		//Bot is not connected to voice channel. Do nothing.
		if(!guild.getAudioManager().isConnected()) {
			MessageText.send(event.getChannel().asTextChannel(), "The music player is not connected to a voice channel!");
			return;
		}

		AudioChannel voice = guild.getAudioManager().getConnectedChannel();

		//Bot is connected to a voice channel in the guild, and the command sender is not in the channel.
		//Do nothing
		if(!voice.getMembers().contains(event.getMember())) {
			MessageText.send(cmdChannel, "You need to be in the same voice channel in order to skip!");
			return;
		}

		//No song is playing. Do nothing.
		if(biscuit.getAudioPlayer().getPlayingTrack() == null) {
			MessageText.send(cmdChannel, "There is not a song playing for you to skip!");
			return;
		}

		AudioScheduler sched = biscuit.getAudioScheduler();
		boolean skip = false;

		//Get number of users connected to the channel
		int count = nonBotCount(voice);
		
		//Determine amount of votes needed to skip
		int votesNeeded = count / 2;
		if(votesNeeded * 2 < count) {
			votesNeeded++;
		}

		int votes = 1;
		
		//If sender alone
		if(count == 1) {
			skip = true; //approve skip
		}else{
			String id = event.getAuthor().getId();
			
			//Check if command sender has already voted
			if(sched.hasSkipped(id)) {
				MessageText.send(cmdChannel, "You have already voted to skip!");
				return;
			}

			//Save vote
			sched.voteSkip(id);

			//Get updated vote count
			votes = sched.getVotes();
			
			//Check if enough to skip
			if(votes >= votesNeeded) {
				skip = true; //approve skip
			}
		}

		if(skip) {
			//Skip has been approved, so skip
			MessageText.send(cmdChannel, "**" + votes + "/" + count + "** voted to skip. Skipping current track.");
			sched.skip(cmdChannel);
		}else {
			//Skip has not been approved, so send vote update message
			int remaining = votesNeeded - votes;
			
			String words = " vote is ";
			if(remaining != 1) { //make word singular/plural as needed
				words = " votes are ";
			}
			
			MessageText.send(cmdChannel, "**" + votes + "/" + count + "** voted to skip. **" + remaining + "** more" + words + "needed to"
					+ " skip the current track.");
		}

	}

	private int nonBotCount(AudioChannel c) {
		int count = 0;
		ArrayList<Member> members = new ArrayList<Member>(c.getMembers());
		for(Member m : members) {
			if(!m.getUser().isBot()) {
				count++;
			}
		}
		return count;
	}
	
}
