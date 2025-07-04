package com.fpghoti.biscuit.commands.console;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.base.ConsoleCommand;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;

import net.dv8tion.jda.api.entities.Guild;
//import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class SayCommand extends ConsoleCommand{

	public SayCommand() {
		name = "Say (Console)";
		description = "Makes bot send message to a channel in all guilds.";
		usage = "[CONSOLE] say <channel-name> <message>";
		minArgs = 2;
		maxArgs = 2000;
		if(!Main.isPlugin) {
			identifiers.add("say");
		}
		identifiers.add("bsay");
	}

	public void execute(String[] args) {
		BiscuitGuild b = Main.getMainBiscuit();
		if(args.length > 0) {
			String target = args[0];
			String message = "";
			if(args.length > 1) {
				message = args[1];
				if(args.length > 2) {
					for(int i = 2; i < args.length; i++) {
						message = message + " " + args[i];
					}
				}
				for(Guild guild : Main.getJDA().getGuilds()) {

					for(TextChannel c : guild.getTextChannels()) {
						if(c.getName().equalsIgnoreCase(target) || c.getName().equalsIgnoreCase("#" + target)) {
							MessageText.send(c, message);
						}
					}

				}
			}else{
				b.log("INCORRECT USAGE! TRY: say " + target + " <message>");
			}
		}else{
			b.log("INCORRECT USAGE! TRY: say <channel-name> <message>");
		}
	}

}
