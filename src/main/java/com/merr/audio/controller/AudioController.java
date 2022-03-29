package com.merr.audio.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.merr.audio.bean.AudioPlayer;
import com.merr.audio.bean.StorageMap;

@Controller
public class AudioController {
	@Autowired
	StorageMap<String, AudioPlayer> storageMap;
	
	@RequestMapping("/")
	public String home(Model model) throws IOException {
		
		model.addAttribute("storageMap",new ArrayList<AudioPlayer>(storageMap.values()));
		
		return "home";
	}
	
	@PostMapping("/saveFile")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("outputName") String outputName, Model model) throws IOException, Exception {
		AudioPlayer ap = storageMap.get(outputName);	
		//InputStream bufferedIn = new BufferedInputStream(file.getInputStream());
		
		//AudioInputStream stream = AudioSystem.getAudioInputStream(bufferedIn);
		AudioInputStream stream= AudioPlayer.createReusableAudioInputStream(file);
		ap.setInputStream(stream);
		System.out.println(file.getOriginalFilename());
		ap.setFileName(file.getOriginalFilename());

		return home(model);
	}
	
	
	
	@GetMapping("/play")
	public String play( @RequestParam("outputName") String outputName,  Model model) throws IOException, Exception {
		AudioPlayer ap = storageMap.get(outputName);	
		ap.play();
		return home(model);
	}
	
	@GetMapping("/pause")
	public void pause(@RequestParam("outputName") String outputName) throws IOException, Exception {
		
	}
	
	@GetMapping("/stop")
	public String stop(@RequestParam("outputName") String outputName ,  Model model) throws IOException, Exception {
		AudioPlayer ap = storageMap.get(outputName);	
		ap.stop();
		return home(model);
	}
	
	
}