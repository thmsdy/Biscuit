package com.fpghoti.biscuit.rss;

import java.awt.Color;
import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
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
	private String lastVideoTimestamp;

	public YTFeed(BiscuitGuild guild, String alias, TextChannel channel, String channelURL, String message) {
	    this.guild = guild;
	    this.alias = alias;
	    this.channel = channel;
	    this.channelURL = channelURL;
	    this.message = message;
	    lastVideo = "";
	    lastVideoTimestamp = "";
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
	
	public String getLastVideoTimestamp() {
		return lastVideoTimestamp;
	}
	
	public Instant getLastInstant() {
		return parseTimestamp(lastVideoTimestamp);
	}
	
	public void setLastVideo(String link) {
		lastVideo = link;
	}

	public void setLastVideoTimestamp(String link) {
		lastVideoTimestamp = link;
	}
	
	public void post(YTFeedConfig config) {
	    List<YTEntry> ytentries = getEntries();

	    if (ytentries == null || ytentries.isEmpty()) {
	        return;
	    }

	    YTEntry newestEntry = ytentries.getLast();

	    if (newestEntry.getTimestamp() == null || newestEntry.getTimestamp().trim().isEmpty()) {
	        guild.error("Could not retrieve YouTube feed newest timestamp for " + alias + "!");
	        return;
	    }

	    Instant lastInstant = getLastInstant();

	    if (lastInstant == null) {
	        guild.log("Recovering missing YouTube feed last timestamp for " + alias + ".");

	        if (lastVideo != null && !lastVideo.trim().isEmpty()) {
	            for (YTEntry entry : ytentries) {
	                if (entry.getURL().equals(lastVideo)) {
	                    String recoveredTimestamp = entry.getTimestamp();

	                    if (recoveredTimestamp == null || recoveredTimestamp.trim().isEmpty()) {
	                        guild.error("Could not recover timestamp for saved YouTube video for " + alias + ".");
	                        return;
	                    }

	                    lastVideoTimestamp = recoveredTimestamp;
	                    config.setLastPosted(lastVideo, recoveredTimestamp);
	                    lastInstant = parseTimestamp(recoveredTimestamp);

	                    guild.log("Recovered YouTube feed last timestamp for " + alias + ".");
	                    break;
	                }
	            }
	        }

	        if (lastInstant == null) {
	            guild.log("Could not recover saved YouTube video timestamp for " + alias
	                    + ". Initializing to newest entry without posting backlog.");

	            lastVideo = newestEntry.getURL();
	            lastVideoTimestamp = newestEntry.getTimestamp();

	            config.setLastPosted(lastVideo, lastVideoTimestamp);

	            return;
	        }
	    }

	    int index = 0;
	    int lastVidIndex = -1;

	    for (YTEntry entry : ytentries) {
	        String link = entry.getURL();

	        if (link.equals(lastVideo)) {
	            lastVidIndex = index;
	            break;
	        }

	        index++;
	    }

	    index = 0;

	    for (YTEntry entry : ytentries) {
	        String timestamp = entry.getTimestamp();
	        Instant entryTimestamp = parseTimestamp(timestamp);

	        if (entryTimestamp == null) {
	            guild.error("Could not retrieve YouTube feed entry timestamp for " + alias + "!");
	            return;
	        }

	        if (index > lastVidIndex && entryTimestamp.isAfter(lastInstant)) {
	            String link = entry.getURL();

	            MessageText.send(channel, message);
	            MessageText.send(channel, entry.getEmbedMessage());

	            lastVideo = link;
	            lastVideoTimestamp = timestamp;
	            config.setLastPosted(link, timestamp);

	            lastInstant = entryTimestamp;
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
		}

		Element rss = doc.getRootElement();
		Elements entries = rss.getChildElements();
		for(Element entry : entries){
			if(entry.getLocalName().equals("entry")) {
				
				String id = "";
				String title = "";
				String author = "";
				String timestamp = "";
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
					if(value.getLocalName().equals("published")) {
						timestamp = value.getValue();
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
				
				ytentries.add(new YTEntry(id, title, author, timestamp, description, thumbnail));
			}
		}
		
		return ytentries.reversed();

	}
	
	private Instant parseTimestamp(String timestamp) {
	    if (timestamp == null || timestamp.trim().isEmpty()) {
	        return null;
	    }

	    try {
	        return OffsetDateTime.parse(timestamp.trim()).toInstant();
	    } catch (DateTimeParseException e) {
	        throw new IllegalArgumentException("Invalid YouTube timestamp: " + timestamp, e);
	    }
	}

}
