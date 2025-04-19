package com.fpghoti.biscuit.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fpghoti.biscuit.Main;

public class BiscuitLogger {

	private final Logger console = LoggerFactory.getLogger("Biscuit");
	private final Logger file = LoggerFactory.getLogger("B-File");

	public void debug(String msg) {
		console.debug(powerShellCompat("[" + BColor.MAGENTA_BOLD + "DEBUG" + BColor.RESET + "] " + BColor.MAGENTA + msg + BColor.RESET));
		if(!Main.isPlugin) {
			file.debug(BColor.clear(msg));
		}
	}

	public void error(String msg) {
		console.error(powerShellCompat("[" + BColor.RED_BOLD + "ERROR" + BColor.RESET + "] " + BColor.RED + msg + BColor.RESET));
		if(!Main.isPlugin) {
			file.error(BColor.clear(msg));
		}
	}

	public void info(String msg) {
		console.info(powerShellCompat(msg + BColor.RESET));
		if(!Main.isPlugin) {
			file.info(BColor.clear(msg));
		}
	}

	public void trace(String msg) {
		console.trace(powerShellCompat("[" + BColor.CYAN_BOLD + "TRACE" + BColor.RESET + "] " + BColor.CYAN + msg + BColor.RESET));
		if(!Main.isPlugin) {
			file.trace(BColor.clear(msg));
		}
	}

	public void warn(String msg) {
		console.warn(powerShellCompat("[" + BColor.YELLOW_BOLD + "WARN" + BColor.RESET + "] " + BColor.YELLOW + msg + BColor.RESET));
		if(!Main.isPlugin) {
			file.warn(BColor.clear(msg));
		}
	}
	
	private String powerShellCompat(String s) {
		if(Main.getMainBiscuit() == null || Main.getMainBiscuit().getProperties() == null) {
			return s;
		}
		if(Main.getMainBiscuit().getProperties().isPowerShell()) {
			s = s.replace(BColor.MAGENTA + "", BColor.RED_BOLD + "");
		}
		return s;
	}

}
