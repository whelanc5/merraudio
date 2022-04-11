package com.merr.audio.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.merr.audio.DAO.AudioDAO;
import com.merr.audio.bean.AudioPlayer;

 
@Configuration
public class AudioConfig {
	
	@Bean
	public String audioDirectory() {
		return "C:\\audio";
		
	}
	
	@Bean
	List<AudioPlayer> audioPlayers(){
		return new ArrayList<AudioPlayer>();
	}
	
	
	@Bean
	AudioDAO audioDAO() {
		return new AudioDAO();
	}
	
}
