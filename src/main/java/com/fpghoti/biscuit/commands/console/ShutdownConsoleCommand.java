package com.fpghoti.biscuit.commands.console;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.ConsoleCommand;

public class ShutdownConsoleCommand extends ConsoleCommand{

    public ShutdownConsoleCommand() {
        name = "Shutdown (Console)";
        description = "Shuts down the bot.";
        usage = "[CONSOLE] shutdown";
        minArgs = 0;
        maxArgs = 0;
        identifiers.add("shutdown");
    }

	public void execute(String[] args) {
		if(args.length == 0) {
			Main.shutdown();
		}else{
			Main.getLogger().info("INCORRECT USAGE! TRY: say <channel-name> <message>");
		}
	}

}
