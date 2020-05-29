package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Biscuit;
import com.fpghoti.biscuit.api.API;
import com.fpghoti.biscuit.commands.ClientCommand;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SquareRootCommand extends ClientCommand{
	
    public SquareRootCommand() {
        name = "Square Root";
        description = "Finds square root.";
        usage = PropertiesRetrieval.getCommandSignifier() + "squareroot <Num>";
        minArgs = 1;
        maxArgs = 1;
        identifiers.add("squareroot");
        identifiers.add("sqrt");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = API.getBiscuit();
		b.log(event.getAuthor().getName() + " issued a command: -squareroot");
		if(args[0] != null && Util.isDeciDigit(args[0])) {
			double num = Double.parseDouble(args[0]);
			String root = Double.toString(Math.sqrt(num));
			String end = root.substring(Math.max(root.length() - 2, 0));
			if(end.equals(".0")) {
				root = root.replace(".0","");
			}
			event.getTextChannel().sendMessage("The sqaure root of " + args[0] + " is **" + root + "**.").queue();
		}
	}

}
