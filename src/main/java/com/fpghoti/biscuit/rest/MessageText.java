package com.fpghoti.biscuit.rest;

import java.util.concurrent.TimeUnit;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class MessageText {

	/** Sends an async message. **/
	public static void send(TextChannel c, final String message) {
		if(c == null) {
			Main.getMainBiscuit().error("Message could not be sent to null channel: " + message);
			return;
		}
		final Biscuit b = Biscuit.getBiscuit(c.getGuild());
		c.sendMessage(message).submit().whenComplete((msg, error) -> {
			if(error != null) {
				b.warn("Message did not send: " + message);
			}
		});
	}
	
	/** Sends an async private message. **/
	public static void send(PrivateChannel c, final String message) {
		if(c == null) {
			Main.getMainBiscuit().error("Private message could not be sent to null channel: " + message);
			return;
		}
		c.sendMessage(message).submit().whenComplete((msg, error) -> {
			if(error != null) {
				Main.getMainBiscuit().warn("Private message did not send: " + message);
			}
		});
	}
	
	/** Sends an async message embed. **/
	public static void send(TextChannel c, final MessageEmbed message) {
		if(c == null) {
			Main.getMainBiscuit().error("Message embed could not be sent to null channel: " + message);
			return;
		}
		final Biscuit b = Biscuit.getBiscuit(c.getGuild());
		c.sendMessageEmbeds(message).submit().whenComplete((msg, error) -> {
			if(error != null) {
				b.warn("Message embed did not send: " + message);
			}
		});
	}

	/** Sends an async message that will be deleted after a specified number of seconds. **/
	public static void sendTimed(TextChannel c, final String message, final int seconds) {
		if(c == null) {
			Main.getMainBiscuit().error("Timed message could not be sent to null channel - Time: " + seconds + " Text: " + message);
			return;
		}
		final Biscuit b = Biscuit.getBiscuit(c.getGuild());
		c.sendMessage(message).submit()
		.whenComplete((msg, error) -> {
			if(error != null) {
				b.warn("Timed message did not send - Time: " + seconds + " Text: " + message);
			}
		})	
		.thenCompose((msg) -> msg.delete().submitAfter(seconds, TimeUnit.SECONDS)) 
		.whenComplete((msg, error) -> {
			if(error != null) {
				b.warn("Timed message did not delete - Time: " + seconds + " Text: " + message);
			}
		});
	}
	
	/** Sends an async message embed that will be deleted after a specified number of seconds. **/
	public static void sendTimed(TextChannel c, final MessageEmbed message, final int seconds) {
		if(c == null) {
			Main.getMainBiscuit().error("Timed message embed could not be sent to null channel - Time: " + seconds + " Text: " + message);
			return;
		}
		final Biscuit b = Biscuit.getBiscuit(c.getGuild());
		c.sendMessageEmbeds(message).submit()
		.whenComplete((msg, error) -> {
			if(error != null) {
				b.warn("Timed message embed did not send - Time: " + seconds + " Text: " + message);
			}
		})	
		.thenCompose((msg) -> msg.delete().submitAfter(seconds, TimeUnit.SECONDS)) 
		.whenComplete((msg, error) -> {
			if(error != null) {
				b.warn("Timed message embed did not delete - Time: " + seconds + " Text: " + message);
			}
		});
	}

}
