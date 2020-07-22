package com.fpghoti.biscuit.commands.base;

import com.fpghoti.biscuit.commands.BaseCommand;
import com.fpghoti.biscuit.commands.CommandType;

public abstract class ConsoleCommand extends BaseCommand{
	
	public abstract void execute(String[] args);
	
	public CommandType getType() {
		return CommandType.CONSOLE;
	}

}
