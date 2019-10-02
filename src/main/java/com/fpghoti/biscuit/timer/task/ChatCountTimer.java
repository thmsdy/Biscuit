package com.fpghoti.biscuit.timer.task;

import com.fpghoti.biscuit.global.MessageQueue;
import com.fpghoti.biscuit.timer.BiscuitTimer;

public class ChatCountTimer extends BiscuitTimer {
	
	public ChatCountTimer(){
		delay = (long) 0;
		period = (long) 10*1000;
	}
	
	public void run() {
		MessageQueue.chatssent.clear();
		MessageQueue.spammsgs.clear();
		MessageQueue.chatssent10s.clear();
	}

}
