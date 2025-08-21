package com.fpghoti.biscuit.rss;

import java.awt.Color;

import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class YTEntry {	

	private String id;
	private String title;
	private String author;
	private String description;
	private String thumbnail;
	
	public YTEntry(String id, String title, String author, String description, String thumbnail) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.description = description;
		this.thumbnail = thumbnail;
	}
	
	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getThumbnail() {
		return thumbnail;
	}
	
	public String getURL() {
		return "https://www.youtube.com/watch?v=" + id;
	}
	
	public MessageEmbed getEmbedMessage() {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(title, getURL());	
		embed.setColor(Color.RED);
		
		String descPreview = description.substring(0, Math.min(description.length(), 200));
		if(descPreview.length() < description.length()) {
			descPreview += "...";
		}

		embed.setDescription(descPreview);
		embed.setAuthor(author, null, null);
		embed.setThumbnail(thumbnail);
		return embed.build();
	}
	
}
