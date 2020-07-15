package com.fpghoti.biscuit.timer.task;

import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.timer.BiscuitTimer;

public class SoftMuteTimer extends BiscuitTimer{
	
	public SoftMuteTimer(Biscuit b){
		biscuit = b;
		delay = (long) 0;
		period = (long) 120*1000;
	}

	public void run() {
		biscuit.getMessageStore().allowSoftMutedMessage();
	}

}
