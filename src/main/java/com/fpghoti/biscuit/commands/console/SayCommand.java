package com.fpghoti.biscuit.commands.console;

import com.fpghoti.biscuit.Biscuit;
import com.fpghoti.biscuit.api.API;
import com.fpghoti.biscuit.commands.ConsoleCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class SayCommand extends ConsoleCommand{

    public SayCommand() {
        name = "Say";
        description = "Makes bot send message on specified channel.";
        usage = "say <channel-name> <message>";
        minArgs = 2;
        maxArgs = 2000;
        identifiers.add("say");
    }

	public void execute(String[] args) {
		Biscuit b = API.getBiscuit();
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
				for(Guild guild : b.getJDA().getGuilds()) {

					for(TextChannel c : guild.getTextChannels()) {
						if(c.getName().equalsIgnoreCase(target) || c.getName().equalsIgnoreCase("#" + target)) {
							c.sendMessage(message).queue();
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
