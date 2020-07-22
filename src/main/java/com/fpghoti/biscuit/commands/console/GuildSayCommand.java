package com.fpghoti.biscuit.commands.console;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ConsoleCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class GuildSayCommand extends ConsoleCommand{

	public GuildSayCommand() {
		name = "Guild Say (Console)";
		description = "Makes bot send message to a channel in the specified guild.";
		usage = "[CONSOLE] guildsay <guild-code> <channel-name> <message>";
		minArgs = 3;
		maxArgs = 2000;
		identifiers.add("guildsay");
		identifiers.add("gsay");
	}

	public void execute(String[] args) {
		if(args.length > 0) {
			String guildcode = args[0];
			Biscuit b = Biscuit.getBiscuit(guildcode);
			if(b == null) {
				Main.getMainBiscuit().log("INVALID GUILD! TRY: guildsay <guild-code> <channel-name> <message>");
				return;
			}
			Guild guild = b.getGuild();
			if(args.length > 1) {
				String channel = args[1];
				String message = "";
				if(args.length > 2) {
					message = args[2];
					if(args.length > 3) {
						for(int i = 3; i < args.length; i++) {
							message = message + " " + args[i];
						}
					}

					for(TextChannel c : guild.getTextChannels()) {
						if(c.getName().equalsIgnoreCase(channel) || c.getName().equalsIgnoreCase("#" + channel)) {
							c.sendMessage(message).queue();
						}
					}

				}else{
					b.log("INCORRECT USAGE! TRY: guildsay " + guildcode + " " + channel + " <message>");
				}

			}else{
				b.log("INCORRECT USAGE! TRY: guildsay " + guildcode + " <channel-name> <message>");
			}
		}else{
			Main.getMainBiscuit().log("INCORRECT USAGE! TRY: guildsay <guild-code> <channel-name> <message>");
		}
	}

}
