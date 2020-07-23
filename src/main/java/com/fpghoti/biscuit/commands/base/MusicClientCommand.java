package com.fpghoti.biscuit.commands.base;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class MusicClientCommand extends ClientCommand{

	public abstract void execute(String[] args, GuildMessageReceivedEvent event);

}
