package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class UnSoftMuteCommand extends ClientCommand{

    public UnSoftMuteCommand() {
        name = "Un Soft Mute";
        description = "Removes a soft mute from a user.";
        usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "unsoftmute @<mention-user>";
        minArgs = 1;
        maxArgs = 1;
        identifiers.add("unsoftmute");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -unsoftmute " + args[0]);
		for(Member m : event.getMessage().getMentionedMembers()){
			User u = m.getUser();
			String s = u.getAsMention();
			if(event.getChannel().getName().equals("public-softmute-test") || (PermUtil.isMod(event.getMember()))) { 
				b.getMessageStore().removeSoftmuted(u);
				event.getTextChannel().sendMessage(s+ " is no longer soft-muted.").queue();
			}
		}
	}

}
