package com.fpghoti.biscuit.commands.discord;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DivideCommand extends ClientCommand{
	
    public DivideCommand() {
        name = "Divide";
        description = "Divides two numbers.";
        usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "divide <Num 1> <Num 2>";
        minArgs = 2;
        maxArgs = 2;
        identifiers.add("divide");
        identifiers.add("div");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		BiscuitGuild b = BiscuitGuild.getBiscuitGuild(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -divide");
		if(args[0] != null && Util.isDeciDigit(args[0]) && args[1] != null && Util.isDeciDigit(args[1])) {
			double num = Double.parseDouble(args[0]);
			double num2 = Double.parseDouble(args[1]);
			String divide = Double.toString(num / num2);
			String end = divide.substring(Math.max(divide.length() - 2, 0));
			if(end.equals(".0")) {
				divide = divide.replace(".0","");
			}
			MessageText.send(event.getChannel().asTextChannel(), args[0] + " / " + args[1] + " is **" + divide + "**.");
		}
	}

}
