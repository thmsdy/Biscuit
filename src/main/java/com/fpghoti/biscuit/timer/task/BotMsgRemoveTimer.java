package com.fpghoti.biscuit.timer.task;

import com.fpghoti.biscuit.global.MessageQueue;
import com.fpghoti.biscuit.timer.BiscuitTimer;

import net.dv8tion.jda.api.exceptions.ErrorResponseException;

public class BotMsgRemoveTimer extends BiscuitTimer{

	public BotMsgRemoveTimer(){
		delay = (long) 0;
		period = (long) 5*1000;
	}

	public void run() {
		for(String m : MessageQueue.removemessages.keySet()){
			try{
				MessageQueue.removemessages.get(m).deleteMessageById(m).complete();
			}catch(ErrorResponseException ex){
			}
		}

		MessageQueue.removemessages.clear();

	}


}
