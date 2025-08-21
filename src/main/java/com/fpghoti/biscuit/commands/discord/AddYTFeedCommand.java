package com.fpghoti.biscuit.commands.discord;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
//import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AddYTFeedCommand extends ClientCommand{

	public AddYTFeedCommand() {
		name = "Add YouTube Feed";
		description = "Sets bot to post YouTube uploads for a specified YouTube channel RSS feed inside the text channel the command was run in";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "addytfeed <alias> <channel-rss-url> <message>";
		minArgs = 1;
		maxArgs = 1000000;
		identifiers.add("addytfeed");
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		System.out.println("0");
		BiscuitGuild b = BiscuitGuild.getBiscuitGuild(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -addytfeed " + args[0]);
		System.out.println("A");
		if((PermUtil.isAdmin(event.getMember()))) {
			System.out.println("B");
			Guild g = event.getGuild();
			TextChannel c = event.getChannel().asTextChannel();
			String alias = args[0];
			String channelURL = args[1];
			String message = Util.getArgsMessage(2, args);
			System.out.println("C");
			boolean success = b.addYoutubeFeed(alias, c, channelURL, message);
			System.out.println("D");
			if(success) {
				MessageText.send(event.getChannel().asTextChannel(), "YouTube Feed has been created.");
			}else {
				MessageText.send(event.getChannel().asTextChannel(), "There was an error creating the YouTube feed.");
			}		
		}else {
			MessageText.send(event.getChannel().asTextChannel(), "You do not have permission to create a YouTube feed.");
		}
	}

}
