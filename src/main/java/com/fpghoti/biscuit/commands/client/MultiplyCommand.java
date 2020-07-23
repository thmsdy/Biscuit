package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MultiplyCommand extends ClientCommand{
	
    public MultiplyCommand() {
        name = "Multiply";
        description = "Multiplies two numbers.";
        usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "multiply <Num1> <Num2>";
        minArgs = 2;
        maxArgs = 2;
        identifiers.add("multiply");
        identifiers.add("mul");
    }

	@Override
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -multiply");
		if(args[0] != null && Util.isDeciDigit(args[0]) && args[1] != null && Util.isDeciDigit(args[1])) {
			double num = Double.parseDouble(args[0]);
			double num2 = Double.parseDouble(args[1]);
			String prod = Double.toString(num * num2);
			String end = prod.substring(Math.max(prod.length() - 2, 0));
			if(end.equals(".0")) {
				prod = prod.replace(".0","");
			}
			event.getChannel().sendMessage(args[0] + " x " + args[1] + " is **" + prod + "**.").queue();
		}
	}

}
