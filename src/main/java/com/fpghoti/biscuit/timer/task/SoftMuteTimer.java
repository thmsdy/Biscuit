package com.fpghoti.biscuit.timer.task;

import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.timer.BiscuitTimer;

public class SoftMuteTimer extends BiscuitTimer{
	
	public SoftMuteTimer(BiscuitGuild b){
		biscuit = b;
		delay = (long) 0;
		period = (long) 120*1000;
	}

	public void run() {
		biscuit.getMessageStore().allowSoftMutedMessage();
	}

}
