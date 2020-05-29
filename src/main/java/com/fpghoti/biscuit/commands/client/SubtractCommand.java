package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Biscuit;
import com.fpghoti.biscuit.api.API;
import com.fpghoti.biscuit.commands.ClientCommand;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SubtractCommand extends ClientCommand{
	
    public SubtractCommand() {
        name = "Subtract";
        description = "Subtracts two numbers.";
        usage = PropertiesRetrieval.getCommandSignifier() + "subtract <Num1> <Num2>";
        minArgs = 2;
        maxArgs = 2;
        identifiers.add("subtract");
        identifiers.add("sub");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = API.getBiscuit();
		b.log(event.getAuthor().getName() + " issued a command: -subtract");
		if(args[0] != null && Util.isDeciDigit(args[0]) && args[1] != null && Util.isDeciDigit(args[1])) {
			double num = Double.parseDouble(args[0]);
			double num2 = Double.parseDouble(args[1]);
			String sub = Double.toString(num - num2);
			String end = sub.substring(Math.max(sub.length() - 2, 0));
			if(end.equals(".0")) {
				sub = sub.replace(".0","");
			}
			event.getTextChannel().sendMessage(args[0] + " - " + args[1] + " is **" + sub + "**.").queue();
		}
	}

}
