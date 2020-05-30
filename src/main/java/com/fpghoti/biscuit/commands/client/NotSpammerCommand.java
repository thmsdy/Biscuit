package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Biscuit;
import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.ClientCommand;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.fpghoti.biscuit.global.SpamRecords;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class NotSpammerCommand extends ClientCommand{

    public NotSpammerCommand() {
        name = "Not Spammer";
        description = "Delists user as spammer.";
        usage = PropertiesRetrieval.getCommandSignifier() + "notspammer @<mention-user>";
        minArgs = 1;
        maxArgs = 1;
        identifiers.add("notspammer");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Main.getBiscuit();
		b.log(event.getAuthor().getName() + " issued a command: -notspammer " + args[0]);
		for(Member m : event.getMessage().getMentionedMembers()){
			User u = m.getUser();
			String s = u.getAsMention();
			if(event.getChannel().getName().equals("public-spam-test") || (PermUtil.isMod(event.getMember()) || PermUtil.canMute(event.getMember()))) {
				SpamRecords.spammers.remove(u);
				event.getTextChannel().sendMessage(s+ " is no longer flagged as spam.").queue();
			}
		}
	}

}
