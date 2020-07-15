package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.ClientCommand;
import com.fpghoti.biscuit.util.PermUtil;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MakeInviteCommand extends ClientCommand{

	public MakeInviteCommand() {
		name = "Make Invite";
		description = "Creates an invite in the specified channel";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "makeinvite <channel-name> [max-age-hours]";
		minArgs = 1;
		maxArgs = 2;
		identifiers.add("makeinvite");
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		double doubAge = 0;
		if(args.length == 2 && Util.isDeciDigit(args[1])) {
			doubAge = Double.parseDouble(args[1]) * 3600;
		}
		int maxAge = (int)Math.round(doubAge);
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -makeinvite " + args[0]);
		if((PermUtil.isAdmin(event.getMember()))) {
			Guild g = event.getGuild();
			TextChannel c = null;
			for(TextChannel t : g.getTextChannels()) {
				if(t.getName().equalsIgnoreCase(args[0])) {
					c = t;
				}
			}
			if(doubAge > 86400) {
				event.getChannel().sendMessage("That length is longer than what Discord allows. Please try again. (Max 24 hours)").queue();
				return;
			}
			final double db = doubAge;
			if(c != null) {
				c.createInvite().setMaxAge(maxAge).queue((i) -> {
					String exp = "Never";
					if(db > 0) {
						exp = args[1] + " hour(s)";
					}
					event.getChannel().sendMessage("Created invite **" + i.getCode() + "** Expiration: **" + exp + "**.").queue();
				});
			}
		}
	}

}
