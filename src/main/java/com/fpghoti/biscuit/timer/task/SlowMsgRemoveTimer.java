package com.fpghoti.biscuit.timer.task;

import com.fpghoti.biscuit.global.MessageQueue;
import com.fpghoti.biscuit.timer.BiscuitTimer;

import net.dv8tion.jda.api.exceptions.ErrorResponseException;

public class SlowMsgRemoveTimer extends BiscuitTimer{
	
	public SlowMsgRemoveTimer(){
		delay = (long) 0;
		period = (long) 5*1000;
	}

	public void run() {
		for(String m : MessageQueue.slowremovemessages.keySet()){
			try{
				MessageQueue.slowremovemessages.get(m).deleteMessageById(m).complete();
			}catch(ErrorResponseException ex){
			}
		}
		MessageQueue.slowremovemessages.clear();
	}


}
