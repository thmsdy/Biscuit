package com.fpghoti.biscuit.commands.discord;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class NotSpammerCommand extends ClientCommand{

    public NotSpammerCommand() {
        name = "Not Spammer";
        description = "Delists user as spammer.";
        usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "notspammer @<mention-user>";
        minArgs = 1;
        maxArgs = 1;
        identifiers.add("notspammer");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -notspammer " + args[0]);
		for(Member m : event.getMessage().getMentions().getMembers()){
			User u = m.getUser();
			String s = u.getAsMention();
			if(event.getChannel().getName().equals("public-spam-test") || (PermUtil.isMod(event.getMember()))) {
				b.getMessageStore().removeSpammer(u);
				MessageText.send(event.getChannel().asTextChannel(), s + " is no longer flagged as spam.");
			}
		}
	}

}
