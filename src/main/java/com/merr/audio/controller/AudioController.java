package com.merr.audio.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.sound.sampled.AudioInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.merr.audio.bean.AudioPlayerImpl;
import com.merr.audio.service.AudioService;
import com.merr.audio.bean.AudioOutput;

@Controller
public class AudioController {
	@Autowired
	AudioOutput<String, AudioPlayerImpl> storageMap;

	@Autowired
	AudioService audioService;

	@RequestMapping("/")
	public String home(Model model) throws IOException {
		model.addAttribute("storageMap", new ArrayList<AudioPlayerImpl>(storageMap.values()));
		model.addAttribute("audioFiles", audioService.getAudioFiles());
		return "home";
	}

	@PostMapping("/saveFile")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("outputName") String outputName, Model model) throws IOException, Exception {
		AudioPlayerImpl ap = storageMap.get(outputName);
		AudioInputStream stream = AudioPlayerImpl.createReusableAudioInputStream(file);
		ap.setInputStream(stream);
		ap.setFileName(file.getOriginalFilename());
		return home(model);
	}

	@GetMapping("/play")
	@ResponseBody
	public String play(@RequestParam("outputName") String outputName, Model model) throws IOException, Exception {
		AudioPlayerImpl ap = storageMap.get(outputName);
		ap.play();
		return "playing";
	}

	@GetMapping("/pause")
	@ResponseBody
	public String pause(@RequestParam("outputName") String outputName, Model model) throws IOException, Exception {
		AudioPlayerImpl ap = storageMap.get(outputName);
		ap.pause();
		return "paused";
	}

	@GetMapping("/stop")
	@ResponseBody
	public String stop(@RequestParam("outputName") String outputName, Model model) throws IOException, Exception {
		AudioPlayerImpl ap = storageMap.get(outputName);
		ap.stop();
		return "stopped";
	}

	@GetMapping("/saveSongs")
	@ResponseBody
	public String saveSongs(HttpServletRequest request, Model model) throws IOException, Exception {

		for (int i = 0; i < storageMap.size(); i++) {
			AudioPlayerImpl ap = storageMap.get(request.getParameter("outputName" + i));
			ap.setFileName(request.getParameter("fileName" + i));
		}

		return "saved";
	}

}