package com.fpghoti.biscuit.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.PropertiesConfigurationLayout;
import org.apache.commons.configuration2.ex.ConfigurationException;

import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.rest.MessageText;
import com.jcabi.aspects.Async;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.TextChannel;

public class BiscuitConfig {

	private Biscuit biscuit;

	public BiscuitConfig(Biscuit b) {
		this.biscuit = b;
	}

	public void generateConfig() {
		Guild guild = biscuit.getGuild();
		boolean isMain = guild ==  null;

		String name = "config.properties";

		if(!isMain) {
			name = guild.getId() + ".properties";
		}
		File config = new File(biscuit.getConfigDir(), name);

		if (!config.exists()) {
			try {
				if(isMain) {
					Path path = config.toPath();
					InputStream is = BiscuitConfig.class.getClassLoader().getResourceAsStream(name);
					Files.copy( is, path);
					is.close();
				}else {
					config.createNewFile();
					updateConfig(config);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			updateConfig(config);
		}
	}

	@Async
	public void replaceConfig(Attachment a, TextChannel c) {
		Guild guild = biscuit.getGuild();
		String code = getFromConfig("Guild-Code", biscuit);
		if(guild == null) {
			biscuit.error("Main config replacement is currently not allowed.");
			return;
		}
		String name = guild.getId() + ".properties";
		if(a.getSize() > 51200) {
			MessageText.send(c, "**The file is too big!**");
			return;
		}
		File config = new File(biscuit.getConfigDir(), name);
		a.downloadToFile(config).thenAccept(file -> {
			updateConfig(file, true, code);
			MessageText.send(c, "**The config was successfully updated.**");
		}).exceptionally(t -> { 
			biscuit.error("Could not accept config file.");
			MessageText.send(c, "**An Exception occurred while trying to read the file.**");
			return null;
		});

		return;
	}

	public void updateConfig(File config) {
		updateConfig(config, false);
	}

	public void updateConfig(File config, boolean silent) {
		updateConfig(config, false, null);
	}

	public void updateConfig(File config, boolean silent, String code) {
		boolean added = false;
		PropertiesConfiguration prop = new PropertiesConfiguration();
		PropertiesConfigurationLayout layout = new PropertiesConfigurationLayout();
		prop.setLayout(layout);
		try {
			layout.load(prop, new FileReader(config));
			FileWriter fw = new FileWriter(config);
			added = addNewOptions(prop, silent, code);			
			layout.save(prop, fw);
			if(biscuit.getGuild() == null && added && !silent) {
				biscuit.log("Options have been added to config.properties! For information on what each "
						+ "option does, check out the Biscuit wiki.");
			}
		} catch (ConfigurationException e) {
			e.printStackTrace();
			biscuit.error("There was an issue preparing the config for updates.");
			return;
		} catch (IOException e) {
			e.printStackTrace();
			biscuit.error("There was an issue preparing the config for updates.");
			return;
		}
	}

	private boolean addNewOptions(PropertiesConfiguration prop, boolean silent, String code) {
		Guild guild = biscuit.getGuild();
		boolean isMain = guild == null;
		boolean added = false;
		String name = "Main Config";

		if(!isMain) {
			name = guild.getName();
		}

		//Add properties to only be used in main config here
		if(isMain) {
			added = addProperty("Command-Signifier", "-", prop, added, silent);
			added = addProperty("Bot-Token", "", prop,  added, silent);
			added = addProperty("Enable-Music-Bot", "true", prop,  added, silent);
			added = addProperty("Log-Music-Player", "true", prop,  added, silent);
		}

		added = addProperty("Guild-Identifier", name, prop, added, silent);
		if(prop.getProperty("Guild-Identifier") != null) {
			prop.setProperty("Guild-Identifier", name);
		}

		//Add properties to only be used in guild configs here
		if(!isMain) {
			//Each key value will be set to [global] by default
			added = addProperty("Guild-Code", "", prop, added, silent);
		}

		//Add properties to appear in both types of configs here
		added = addProperty("ChatLog", "true", prop, added, silent);
		added = addProperty("AllowSpamPunish", "true", prop,  added, silent);
		added = addProperty("Channels-To-Not-Chatlog", "ignore_me,ignore_me2,ignore_me3", prop,  added, silent);
		added = addProperty("NaughtyList", "piff,word123,another1", prop,  added, silent);
		added = addProperty("Restrict-Cmd-Channels", "false", prop,  added, silent);
		added = addProperty("CmdChannels", "bot,bot2,bot3", prop,  added, silent);
		added = addProperty("ToggleRoles", "role1,role2,role3", prop,  added, silent);
		added = addProperty("UseCustomDefaultRole", "true", prop,  added, silent);
		added = addProperty("DefaultRoleName", "Standard", prop,  added, silent);
		added = addProperty("Done-Emote", "ActionComplete", prop,  added, silent);
		added = addProperty("Captcha", "false", prop,  added, silent);
		added = addProperty("Captcha-Reward-Role", "cleared", prop,  added, silent);
		added = addProperty("No-Captcha-Kick", "false", prop,  added, silent);
		added = addProperty("No-Captcha-Kick-Time", "10", prop,  added, silent);
		added = addProperty("Block-Unicode-Emotes", "baby,snake,squid", prop,  added, silent);
		added = addProperty("Block-Custom-Emotes", "badfish,fix,bigleaf", prop,  added, silent);
		added = addProperty("DisabledCommands", "cmd1,cmd2,cmd3", prop,  added, silent);
		added = addProperty("DisabledUserCommands", "cmd4,cmd5,cmd6", prop,  added, silent);
		added = addProperty("ModRole", "biscuit-key", prop,  added, silent);
		added = addProperty("AdminRole", "biscuit-admin", prop,  added, silent);
		added = addProperty("Toggle-Role-React-Channels", "toggleroles1,toggleroles2,toggleroles3", prop,  added, silent);
		added = addProperty("Boost-Exclusive-Roles", "role2,role3", prop,  added, silent);
		added = addProperty("Treat-Like-Booster", "Nitro Booster,silver,gold", prop,  added, silent);
		added = addProperty("LogCaptcha", "false", prop,  added, silent);
		added = addProperty("Captcha-Log-Channel", "verify-log", prop,  added, silent);
		added = addProperty("Check-Join-Invite", "false", prop,  added, silent);
		added = addProperty("Custom-Command-Names", "", prop,  added, silent);
		added = addProperty("DM-Before-Kick", "true", prop,  added, silent);
		added = addProperty("Kick-DM-Invite", "", prop,  added, silent);
		added = addProperty("Allow-Music-Bot", "true", prop,  added, silent);
		added = addProperty("Music-Channels", "", prop,  added, silent);
		added = addProperty("Music-Controller-Role", "music-key", prop,  added, silent);

		if(!isMain) {
			if(code != null) {
				prop.setProperty("Guild-Code", code);
			}
		}

		return added;
	}

	private boolean addProperty(String key, String value, PropertiesConfiguration prop, boolean added, boolean silent) {
		Guild guild = biscuit.getGuild();
		if(guild != null) {
			value = "[global]";
		}
		if(prop.getProperty(key) == null) {
			if(silent) {
				biscuit.log("IMPORTANT: A new option has been added to the configuration file: " + key);
			}
			prop.addProperty(key, value);
			return true;
		}
		return added;
	}

	public String getFromConfig(String property, Biscuit biscuit){
		boolean isMain = biscuit.getGuild() == null;

		String setting = "";

		Properties prop = new Properties();
		InputStream input = null;

		String name = "config.properties";

		if(!isMain) {
			name = biscuit.getGuild().getId() + ".properties";
		}
		File config = new File(biscuit.getConfigDir(), name);

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

	public File getFile() {
		if(biscuit.getGuild() == null) {
			return null;
		}
		String name = biscuit.getGuild().getId() + ".properties";
		//Most likely will not happen
		//but here to prevent bot token leakage
		if(name.equalsIgnoreCase("config")) {
			return null;
		}
		File config = new File(biscuit.getConfigDir(), name);
		return config;
	}

	public String makeCode(String name) {
		String code = "";
		name = name.replace(" ", "");
		if(name.length() > 6) {
			code = name.substring(0, 6);
		}else{
			code = name;
		}
		return code.toUpperCase();
	}


}
