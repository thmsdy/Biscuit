package com.fpghoti.biscuit.audio.util;

import java.io.File;
import java.util.ArrayList;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.util.Util;

public class AudioUtil {
	
	public static ArrayList<File> getAudioFiles(){
		return getAudioFiles(Main.audioDir);
	}

	public static ArrayList<File> getAudioFiles(File a){
		ArrayList<File> audios = new ArrayList<File>();
		File[] files = a.listFiles();
		for(int i = 0; i < files.length; i++) {
			File f = files[i];
			if(!f.isDirectory()) {
				String name = f.getName();
				String filetype = Util.getLast(name, "\\.");
				String[] types = {"mp3", "wav", "flac", "mp4", "m4a", "ogg", "webm"};
				for(String s : types) {
					if(s.equalsIgnoreCase(filetype)) {
						audios.add(f);
					}
				}
			}
		}
		return audios;
	}
	
	public static File getAudioFile(String name) {
		return getAudioFile(getAudioFiles(), name);
	}
	
	public static File getAudioFile(ArrayList<File> playlist, String name) {
		for(File f : playlist) {
			if(f.getName().equalsIgnoreCase(name)) {
				return f;
			}
		}
		return null;
	}
	
}
