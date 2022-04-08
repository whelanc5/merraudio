package com.merr.audio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

 
@Configuration
public class AudioConfig {
	
	@Bean
	public String audioDirectory() {
		return "C:\\audio";
		
	}
	

}
