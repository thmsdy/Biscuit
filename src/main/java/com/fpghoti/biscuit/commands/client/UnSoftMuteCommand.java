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

public class UnSoftMuteCommand extends ClientCommand{

    public UnSoftMuteCommand() {
        name = "Un Soft Mute";
        description = "Removes a soft mute from a user.";
        usage = PropertiesRetrieval.getCommandSignifier() + "unsoftmute @<mention-user>";
        minArgs = 1;
        maxArgs = 1;
        identifiers.add("unsoftmute");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Main.getBiscuit();
		b.log(event.getAuthor().getName() + " issued a command: -unsoftmute " + args[0]);
		for(Member m : event.getMessage().getMentionedMembers()){
			User u = m.getUser();
			String s = u.getAsMention();
			if(event.getChannel().getName().equals("public-softmute-test") || (PermUtil.isMod(event.getMember()))) { 
				SpamRecords.softmute.remove(u);
				event.getTextChannel().sendMessage(s+ " is no longer soft-muted.").queue();
			}
		}
	}

}
