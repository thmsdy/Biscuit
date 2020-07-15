package com.fpghoti.biscuit.timer;

import java.util.TimerTask;

import com.fpghoti.biscuit.biscuit.Biscuit;

public abstract class BiscuitTimer extends TimerTask{
	
	protected Long delay;
	protected Long period;
	
	protected Biscuit biscuit;
	
	public long getDelay() {
		if(delay != null) {
			return delay;
		}else {
			return 0;
		}
	}
	
	public long getPeriod() {
		if(period != null) {
			return period;
		}else {
			return 0;
		}
	}

}
