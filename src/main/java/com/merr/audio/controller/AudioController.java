package com.merr.audio.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.merr.audio.bean.AudioPlayerImpl;
import com.merr.audio.bean.AudioOutput;

@Controller
public class AudioController {
	@Autowired
	AudioOutput<String, AudioPlayerImpl> storageMap;
	
	@RequestMapping("/")
	public String home(Model model) throws IOException {
		model.addAttribute("storageMap",new ArrayList<AudioPlayerImpl>(storageMap.values()));
		return "home";
	}
	
	@PostMapping("/saveFile")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("outputName") String outputName, Model model) throws IOException, Exception {
		AudioPlayerImpl ap = storageMap.get(outputName);	
		//InputStream bufferedIn = new BufferedInputStream(file.getInputStream());
		//AudioInputStream stream = AudioSystem.getAudioInputStream(bufferedIn);
		AudioInputStream stream= AudioPlayerImpl.createReusableAudioInputStream(file);
		ap.setInputStream(stream);
		ap.setFileName(file.getOriginalFilename());
		return home(model);
	}
	
	
	
	@GetMapping("/play")
	public String play( @RequestParam("outputName") String outputName,  Model model) throws IOException, Exception {
		AudioPlayerImpl ap = storageMap.get(outputName);	
		ap.play();
		return home(model);
	}
	
	@GetMapping("/pause")
	public String pause(@RequestParam("outputName") String outputName,  Model model) throws IOException, Exception {
		AudioPlayerImpl ap = storageMap.get(outputName);	
		ap.pause();
		return home(model);
	}
	
	@GetMapping("/stop")
	public String stop(@RequestParam("outputName") String outputName ,  Model model) throws IOException, Exception {
		AudioPlayerImpl ap = storageMap.get(outputName);	
		ap.stop();
		return home(model);
	}
	
	
}