package com.fpghoti.biscuit.timer.task;

import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.timer.BiscuitTimer;

public class ChatCountTimer extends BiscuitTimer {
	
	public ChatCountTimer(BiscuitGuild b){
		biscuit = b;
		delay = (long) 0;
		period = (long) 10*1000;
	}
	
	public void run() {
		biscuit.getMessageStore().forgetChats();
	}

}
