package com.fpghoti.biscuit.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BiscuitLog {

	private final Logger console = LoggerFactory.getLogger("Biscuit");
	private final Logger file = LoggerFactory.getLogger("B-File");

	public void debug(String msg) {
		console.debug(BColor.MAGENTA_BOLD + msg + BColor.RESET);
		file.debug(BColor.clear(msg));
	}

	public void error(String msg) {
		console.error(BColor.RED + msg + BColor.RESET);
		file.error(BColor.clear(msg));
	}

	public void info(String msg) {
		console.info(msg + BColor.RESET);
		file.info(BColor.clear(msg));
	}

	public void trace(String msg) {
		console.trace(BColor.WHITE_BOLD + msg + BColor.RESET);
		file.trace(BColor.clear(msg));
	}

	public void warn(String msg) {
		console.warn(BColor.YELLOW + msg + BColor.RESET);
		file.warn(BColor.clear(msg));
	}

}
