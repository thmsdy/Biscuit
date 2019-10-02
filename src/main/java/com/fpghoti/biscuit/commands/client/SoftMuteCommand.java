package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Biscuit;
import com.fpghoti.biscuit.api.API;
import com.fpghoti.biscuit.commands.ClientCommand;
import com.fpghoti.biscuit.global.SpamRecords;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SoftMuteCommand extends ClientCommand{

    public SoftMuteCommand() {
        name = "Soft Mute";
        description = "Soft mutes a user. In this state, they will only be able to send a message every two minutes.";
        usage = "-softmute @<mention-user>";
        minArgs = 1;
        maxArgs = 1;
        identifiers.add("softmute");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = API.getBiscuit();
		b.log(event.getAuthor().getName() + " issued a command: -softmute " + args[0]);
		for(Member m : event.getMessage().getMentionedMembers()){
			User u = m.getUser();
			String s = u.getAsMention();
			if(event.getChannel().getName().equals("public-softmute-test") || (PermUtil.isMod(event.getMember()) || PermUtil.canMute(event.getMember()))) {
				SpamRecords.softmute.add(u);
				u.openPrivateChannel().queue();
				event.getTextChannel().sendMessage(s+ " is now soft-muted. They will now be only able to send one message every two minutes.").queue();
			}
		}
	}

}
