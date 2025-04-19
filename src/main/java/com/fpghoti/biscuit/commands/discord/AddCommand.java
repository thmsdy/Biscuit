package com.fpghoti.biscuit.commands.discord;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AddCommand extends ClientCommand{
	
    public AddCommand() {
        name = "Add";
        description = "Finds sum of two numbers.";
        usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "add <Num1> <Num2>";
        minArgs = 2;
        maxArgs = 2;
        identifiers.add("add");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -add");
		if(args[0] != null && Util.isDeciDigit(args[0]) && args[1] != null && Util.isDeciDigit(args[1])) {
			double num = Double.parseDouble(args[0]);
			double num2 = Double.parseDouble(args[1]);
			String sum = Double.toString(num + num2);
			String end = sum.substring(Math.max(sum.length() - 2, 0));
			if(end.equals(".0")) {
				sum = sum.replace(".0","");
			}
			MessageText.send(event.getChannel().asTextChannel(), args[0] + " + " + args[1] + " is **" + sum + "**.");
		}
	}

}
