package com.fpghoti.biscuit.timer.task;

import com.fpghoti.biscuit.global.MessageQueue;
import com.fpghoti.biscuit.timer.BiscuitTimer;

public class SoftMuteTimer extends BiscuitTimer{
	
	public SoftMuteTimer(){
		delay = (long) 0;
		period = (long) 120*1000;
	}

	public void run() {
		MessageQueue.chatssent2m.clear();
	}

}
