package com.fpghoti.biscuit.rss;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.PropertiesConfigurationLayout;
import org.apache.commons.configuration2.ex.ConfigurationException;

import com.fpghoti.biscuit.guild.BiscuitGuild;

public class YTFeedConfig {

	private BiscuitGuild biscuit;
	private YTFeed feed;
	private File config;
	
	public YTFeedConfig(BiscuitGuild b, YTFeed feed) {
		this.biscuit = b;
		this.feed = feed;
		generateConfig();
	}
	
	public BiscuitGuild getGuild() {
		return biscuit;
	}
	
	public YTFeed getFeed() {
		return feed;
	}

	public void generateConfig() {

		String name = feed.getAlias();

		File config = new File(biscuit.getYTFeedDir(), name);

		if (!config.exists()) {
			try {
				config.createNewFile();
				updateConfig(config);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			updateConfig(config);
		}
		this.config = config;
	}
	

	public void setLastPosted(String link) {
		PropertiesConfiguration prop = new PropertiesConfiguration();
		PropertiesConfigurationLayout layout = new PropertiesConfigurationLayout();
		prop.setLayout(layout);
		try {
			layout.load(prop, new FileReader(config));
			FileWriter fw = new FileWriter(config);
			prop.setProperty("LastVideo", link);	
			feed.setLastVideo(link);
			layout.save(prop, fw);
		} catch (ConfigurationException e) {
			e.printStackTrace();
			biscuit.error("There was an issue updating a YouTube feed file.");
			return;
		} catch (IOException e) {
			e.printStackTrace();
			biscuit.error("There was an issue updating a YouTube feed file.");
			return;
		}
	}
	
	private void updateConfig(File config) {
		PropertiesConfiguration prop = new PropertiesConfiguration();
		PropertiesConfigurationLayout layout = new PropertiesConfigurationLayout();
		prop.setLayout(layout);
		try {
			layout.load(prop, new FileReader(config));
			FileWriter fw = new FileWriter(config);
			addNewOptions(prop);			
			layout.save(prop, fw);
		} catch (ConfigurationException e) {
			e.printStackTrace();
			biscuit.error("There was an issue preparing a YouTube feed config for updates.");
			return;
		} catch (IOException e) {
			e.printStackTrace();
			biscuit.error("There was an issue preparing a YouTube feed config for updates.");
			return;
		}
	}

	private void addNewOptions(PropertiesConfiguration prop) {
		if(feed == null) {
			return;
		}

		addProperty("Alias", feed.getAlias(), prop);
		addProperty("TextChannelID", feed.getTextChannel().getId(), prop);
		addProperty("YouTubeChannelURL", feed.getChannelURL(), prop);
		addProperty("Message", feed.getMesage(), prop);
		addProperty("LastVideo", "", prop);
	}

	private void addProperty(String key, String value, PropertiesConfiguration prop) {
		if(prop.getProperty(key) == null) {
			prop.addProperty(key, value);
		}
	}

	public String getFromConfig(String property){

        String setting = "";

        Properties prop = new Properties();
        InputStream input = null;

        File config = new File(biscuit.getYTFeedDir(), feed.getAlias());

        if(!config.exists()) {
            return "";
        }

        try {
            input = new FileInputStream(config);
            prop.load(input);
            setting = prop.getProperty(property);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return setting;

    }

}
