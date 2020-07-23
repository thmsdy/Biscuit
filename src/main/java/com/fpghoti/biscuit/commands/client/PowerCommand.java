package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PowerCommand extends ClientCommand{
	
    public PowerCommand() {
        name = "Power";
        description = "Finds Num1 ^ Num2";
        usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "power <Num1> <Num2>";
        minArgs = 2;
        maxArgs = 2;
        identifiers.add("power");
        identifiers.add("pow");
    }

	@Override
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -power");
		if(args[0] != null && Util.isDeciDigit(args[0]) && args[1] != null && Util.isDeciDigit(args[1])) {
			double num = Double.parseDouble(args[0]);
			double num2 = Double.parseDouble(args[1]);
			String pow = Double.toString( Math.pow(num,num2));
			String end = pow.substring(Math.max(pow.length() - 2, 0));
			if(end.equals(".0")) {
				pow = pow.replace(".0","");
			}
			event.getChannel().sendMessage(args[0] + "^" + args[1] + " is **" + pow + "**.").queue();
		}
	}

}
