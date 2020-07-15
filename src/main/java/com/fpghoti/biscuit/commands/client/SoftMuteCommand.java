package com.fpghoti.biscuit.commands.client;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.ClientCommand;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SoftMuteCommand extends ClientCommand{

    public SoftMuteCommand() {
        name = "Soft Mute";
        description = "Soft mutes a user. In this state, they will only be able to send a message every two minutes.";
        usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "softmute @<mention-user>";
        minArgs = 1;
        maxArgs = 1;
        identifiers.add("softmute");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -softmute " + args[0]);
		for(Member m : event.getMessage().getMentionedMembers()){
			User u = m.getUser();
			String s = u.getAsMention();
			if(b.getMessageStore().isSoftmuted(u)) {
				event.getTextChannel().sendMessage(s+ " is already softmuted.").queue(new Consumer<Message>()
				{
					@Override
					public void accept(Message msg){
						msg.delete().reason("Automatic bot message removal").submitAfter(3, TimeUnit.SECONDS);
					}
				});
				return;
			}
			if(event.getChannel().getName().equals("public-softmute-test") || (PermUtil.isMod(event.getMember()))) {
				b.getMessageStore().addSoftmuted(u);
				u.openPrivateChannel().queue();
				event.getTextChannel().sendMessage(s+ " is now soft-muted. They will now be only able to send one message every two minutes.").queue();
			}
		}
	}

}
