package com.fpghoti.biscuit.commands.discord;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SquareRootCommand extends ClientCommand{
	
    public SquareRootCommand() {
        name = "Square Root";
        description = "Finds square root.";
        usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "squareroot <Num>";
        minArgs = 1;
        maxArgs = 1;
        identifiers.add("squareroot");
        identifiers.add("sqrt");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -squareroot");
		if(args[0] != null && Util.isDeciDigit(args[0])) {
			double num = Double.parseDouble(args[0]);
			String root = Double.toString(Math.sqrt(num));
			String end = root.substring(Math.max(root.length() - 2, 0));
			if(end.equals(".0")) {
				root = root.replace(".0","");
			}
			MessageText.send(event.getChannel().asTextChannel(), "The sqaure root of " + args[0] + " is **" + root + "**.");
		}
	}

}
