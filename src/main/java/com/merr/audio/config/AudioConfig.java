package com.merr.audio.config;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.merr.audio.bean.AudioPlayer;
import com.merr.audio.bean.StorageMap;

@Configuration
public class AudioConfig {

	@Bean
	public StorageMap<String, AudioPlayer> storageMap() throws LineUnavailableException {
		StorageMap<String, AudioPlayer> storageMap = new StorageMap<String, AudioPlayer>();
		
		for (Info info : AudioSystem.getMixerInfo()) {
			try {
				
			
			AudioPlayer ap = new AudioPlayer(info);
			storageMap.put(info.getName(), ap);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return storageMap;
	}

}
