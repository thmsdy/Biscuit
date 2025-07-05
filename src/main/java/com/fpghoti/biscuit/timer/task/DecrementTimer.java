package com.fpghoti.biscuit.timer.task;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.timer.BiscuitTimer;
import com.fpghoti.biscuit.user.PreUser;

public class DecrementTimer extends BiscuitTimer{

	public DecrementTimer(BiscuitGuild b){
		biscuit = b;
		delay = (long) 0;
		period = (long) 60*1000;
	}

	public void run() {
		if(Main.ready) {
			for(PreUser p : biscuit.getPreUsers()) {
				p.decrementTime();
			}
		}
	}

}
