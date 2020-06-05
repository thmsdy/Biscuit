package com.fpghoti.biscuit.timer.task;

import com.fpghoti.biscuit.global.MessageQueue;
import com.fpghoti.biscuit.timer.BiscuitTimer;

import net.dv8tion.jda.api.exceptions.ErrorResponseException;

public class FastMsgRemoveTimer extends BiscuitTimer{
	
	public FastMsgRemoveTimer(){
		delay = (long) 0;
		//period = (long) 1*1000;
		period = (long) 250;
	}

	public void run() {
		for(String m : MessageQueue.fastremovemessages.keySet()){
			try{
				MessageQueue.fastremovemessages.get(m).deleteMessageById(m).complete();
			}catch(ErrorResponseException ex){
			}
		}
		MessageQueue.fastremovemessages.clear();
	}

}
