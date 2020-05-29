package com.fpghoti.biscuit.commands;

public abstract class ConsoleCommand extends BaseCommand{
	
	public abstract void execute(String[] args);
	
	public CommandType getType() {
		return CommandType.CONSOLE;
	}

}
