package com.merr.audio.config;

import javax.sound.sampled.LineUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.merr.audio.bean.AudioPlayerImpl;
import com.merr.audio.bean.AudioOutput;

 
@Configuration
public class AudioConfig {
	@Autowired 
	com.merr.audio.service.AudioService audioService;
	@Bean
	public AudioOutput<String, AudioPlayerImpl> storageMap() throws LineUnavailableException {

		return audioService.getAudioOutputs();
	}

}
