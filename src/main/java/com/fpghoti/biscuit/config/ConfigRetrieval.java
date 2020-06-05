package com.fpghoti.biscuit.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.PropertiesConfigurationLayout;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.PluginCore;

public class ConfigRetrieval {

	static Logger log = Main.log;

	public static void generateConfig() {
		File config;
		if(Main.isPlugin) {
			config = new File(PluginCore.plugin.getDataFolder(), "config.properties");
			if(!config.exists()) {
				config.getParentFile().mkdir();
			}
		}else {
			config = new File("config.properties");
		}
		if (!config.exists()) {
			try {
				Path path = config.toPath();
				InputStream is = ConfigRetrieval.class.getClassLoader().getResourceAsStream("config.properties");
				Files.copy( is, path);
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			boolean added = false;
			PropertiesConfiguration prop = new PropertiesConfiguration();
			PropertiesConfigurationLayout layout = new PropertiesConfigurationLayout();
			prop.setLayout(layout);
			try {
				layout.load(prop, new FileReader(config));
				FileWriter fw = new FileWriter(config);
				added = updateConfig(prop);			
				layout.save(prop, fw);
				if(added) {
					Main.log.info("The above options have been added to config.properties! For information on what each "
							+ "option does, check out the Biscuit wiki.");
				}
			} catch (ConfigurationException e) {
				e.printStackTrace();
				Main.log.error("There was an issue preparing the config for updates.");
				return;
			} catch (IOException e) {
				e.printStackTrace();
				Main.log.error("There was an issue preparing the config for updates.");
				return;
			}
		}
	}

	private static boolean updateConfig(PropertiesConfiguration prop) {
		boolean added = false;
		added = addProperty("Command-Signifier", "-", prop, added);
		added = addProperty("ChatLog", "true", prop, added);
		added = addProperty("AllowSpamPunish", "true", prop, added);
		added = addProperty("Channels-To-Not-Chatlog", "ignore_me,ignore_me2,ignore_me3", prop, added);
		added = addProperty("Bot-Token", "", prop, added);
		added = addProperty("NaughtyList", "piff,word123,another1", prop, added);
		added = addProperty("Restrict-Cmd-Channels", "false", prop, added);
		added = addProperty("CmdChannels", "bot,bot2,bot3", prop, added);
		added = addProperty("ToggleRoles", "role1,role2,role3", prop, added);
		added = addProperty("UseCustomDefaultRole", "true", prop, added);
		added = addProperty("DefaultRoleName", "Standard", prop, added);
		added = addProperty("Done-Emote", "ActionComplete", prop, added);
		added = addProperty("Captcha", "false", prop, added);
		added = addProperty("Captcha-Reward-Role", "cleared", prop, added);
		added = addProperty("No-Captcha-Kick", "false", prop, added);
		added = addProperty("No-Captcha-Kick-Time", "10", prop, added);
		added = addProperty("Block-Unicode-Emotes", "baby,snake,squid", prop, added);
		added = addProperty("Block-Custom-Emotes", "badfish,fix,bigleaf", prop, added);
		added = addProperty("Custom-Command-Names", "", prop, added);
		added = addProperty("DisabledCommands", "cmd1,cmd2,cmd3", prop, added);
		added = addProperty("DisabledUserCommands", "cmd4,cmd5,cmd6", prop, added);

		return added;
	}

	private static boolean addProperty(String key, String value, PropertiesConfiguration prop, boolean added) {
		if(prop.getProperty(key) == null) {
			Main.log.info("IMPORTANT: A new option has been added to the configuration file: " + key);
			prop.addProperty(key, value);
			return true;
		}
		return added;
	}

	public static String getFromConfig(String property){
		String setting = "";

		Properties prop = new Properties();
		InputStream input = null;

		File config;

		if(Main.isPlugin) {
			config = new File(PluginCore.plugin.getDataFolder(), "config.properties");
		}else {
			config = new File("config.properties");
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
