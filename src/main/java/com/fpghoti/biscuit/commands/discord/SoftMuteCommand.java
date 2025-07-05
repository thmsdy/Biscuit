package com.fpghoti.biscuit.commands.discord;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Member;
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
		BiscuitGuild b = BiscuitGuild.getBiscuitGuild(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -softmute " + args[0]);
		for(Member m : event.getMessage().getMentions().getMembers()){
			User u = m.getUser();
			String s = u.getAsMention();
			if(b.getMessageStore().isSoftmuted(u)) {
				MessageText.sendTimed(event.getChannel().asTextChannel(), s + " is already softmuted.", 3);
				return;
			}
			if(event.getChannel().getName().equals("public-softmute-test") || (PermUtil.isMod(event.getMember()))) {
				b.getMessageStore().addSoftmuted(u);
				u.openPrivateChannel().queue();
				MessageText.send(event.getChannel().asTextChannel(), s + " is now soft-muted. They will now be only able to send one message every two minutes.");
			}
		}
	}

}
