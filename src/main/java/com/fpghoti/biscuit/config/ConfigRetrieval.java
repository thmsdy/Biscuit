package com.fpghoti.biscuit.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

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
		}
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
