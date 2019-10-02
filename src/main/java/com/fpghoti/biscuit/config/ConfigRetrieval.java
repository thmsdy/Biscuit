package com.fpghoti.biscuit.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.slf4j.Logger;

import com.fpghoti.biscuit.Main;

public class ConfigRetrieval {
	
	static Logger log = Main.log;
	
	public static void generateConfig(){
		Properties prop = new Properties();
		OutputStream output = null;

		try {
			FileInputStream inputStream = 
					new FileInputStream("config.properties");
			inputStream.close();        

		}
		catch(FileNotFoundException ex) {
			log.info("config.ini missing...\nCreating file...");  

			try {
				output = new FileOutputStream("config.properties");
				prop.setProperty("Bot-Token", "");
				prop.setProperty("AllowSpamPunish", "true");
				prop.store(output, null);

			} catch (IOException io) {
				io.printStackTrace();
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		catch(IOException ex) {
			log.info("CANNOT READ CONFIG!");                  
		}
	}
	
	public static String getFromConfig(String property){
				String setting = "";

				Properties prop = new Properties();
				InputStream input = null;

				try {
					input = new FileInputStream("config.properties");
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
