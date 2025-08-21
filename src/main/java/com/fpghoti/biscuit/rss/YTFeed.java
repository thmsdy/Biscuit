package com.fpghoti.biscuit.rss;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

public class YTFeed {

	private BiscuitGuild guild;
	private String alias;
	private TextChannel channel;
	private String channelURL;
	private String message;
	private String lastVideo;

	public YTFeed(String alias, TextChannel channel, String channelURL, String message) {
		this.guild = BiscuitGuild.getBiscuitGuild(channel.getGuild());
		this.alias = alias;
		this.channel = channel;
		this.channelURL = channelURL;
		this.message = message;
		lastVideo = "";
	}

	public String getAlias() {
		return alias;
	}

	public TextChannel getTextChannel() {
		return channel;
	}

	public String getChannelURL() {
		return channelURL;
	}

	public String getMesage() {
		return message;
	}

	public String getLastVideo() {
		return lastVideo;
	}

	public void setLastVideo(String link) {
		lastVideo = link;
	}

	public void post(){
		List<YTEntry> ytentries = getEntries();
		int index = 0;
		int lastVidIndex = -1;
		for(YTEntry entry : ytentries) {
			String link = entry.getURL();
			if(link.equals(lastVideo)) {
				lastVidIndex = index;
			}
			index++;
		}
		index = 0;
		for(YTEntry entry : ytentries) {
			if(index > lastVidIndex) {
				String link = entry.getURL();
				lastVideo = link;
				MessageText.send(channel, message);
				MessageText.send(channel, entry.getEmbedMessage());
			}
			index++;
		}
	}

	public List<YTEntry> getEntries() {
		List<YTEntry> ytentries = new ArrayList<YTEntry>();	

		Builder builder = new Builder();
		Document doc = null;
		try {
			doc = builder.build(channelURL);
		} catch (ParsingException | IOException e) {
			guild.error("Unable to parse feed: " + channelURL);
			MessageText.send(channel, "Unable to parse feed: " + channelURL);
		}

		Element rss = doc.getRootElement();
		Elements entries = rss.getChildElements();
		for(Element entry : entries){
			if(entry.getLocalName().equals("entry")) {
				
				String id = "";
				String title = "";
				String author = "";
				String description = "";
				String thumbnail = "";
				
				Elements values = entry.getChildElements();
				for(Element value : values){
					if(value.getLocalName().equals("videoId")) {
						id = value.getValue();
					}
					if(value.getLocalName().equals("title")) {
						title = value.getValue();
					}
					if(value.getLocalName().equals("author")) {
						Elements subvalues = value.getChildElements();
						for(Element subvalue : subvalues) {
							if(subvalue.getLocalName().equals("name")) {
								author = subvalue.getValue();
							}
						}
					}
					if(value.getLocalName().equals("group")) {
						Elements subvalues = value.getChildElements();
						for(Element subvalue : subvalues) {
							if(subvalue.getLocalName().equals("description")) {
								description = subvalue.getValue();
							}
							if(subvalue.getLocalName().equals("thumbnail")) {
								thumbnail = subvalue.getAttributeValue("url");
							}
						}
					}
				}
				
				ytentries.add(new YTEntry(id, title, author, description, thumbnail));
			}
		}
		
		return ytentries.reversed();

	}

}
