package com.merr.audio.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sound.sampled.LineUnavailableException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.merr.audio.service.AudioService;

@Controller
public class AudioController {

	@Autowired
	private AudioService audioService;

	@RequestMapping("/")
	public String home(Model model) throws IOException, LineUnavailableException {
		model.addAttribute("mixerList", audioService.getAudioOutputs());
		model.addAttribute("audioFiles", audioService.getAudioFiles());
		model.addAttribute("audioDirectory", audioService.getAudioDirectory());
		model.addAttribute("audioPlayers", audioService.getAudioPlayers());
		System.out.println(audioService.getAudioPlayers().size());
		return "home";
	}

	@PostMapping("/setDirectory")
	@ResponseBody
	public ResponseEntity<?> setDirectory(@RequestParam("directory") String directory) {
		return audioService.setAudioDirectory(directory);
	}

	@PostMapping("/saveFile")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("outputName") String outputName, Model model) throws IOException, Exception {
		audioService.saveFile(file, outputName);
		return home(model);
	}

	@GetMapping("/play")
	@ResponseBody
	public ResponseEntity<?> play(@RequestParam("audioId") Integer audioId) {
		return audioService.play(audioId);
	}

	@GetMapping("/pause")
	@ResponseBody
	public ResponseEntity<?> pause(@RequestParam("audioId") Integer audioId) {
		return audioService.pause(audioId);
	}

	@GetMapping("/stop")
	@ResponseBody
	public ResponseEntity<?> stop(@RequestParam("audioId") Integer audioId) throws IOException, Exception {
		return audioService.stop(audioId);
	}

	@GetMapping("/save")
	public String save(@RequestParam("audioIds") List<Integer> ids, @RequestParam("files") List<String> fileNames, @RequestParam("mixers") List<String> mixers, @RequestParam("audioDirectory") String audioDirectory, Model model) throws IOException, LineUnavailableException {

	

		ResponseEntity<?> response = audioService.setAudioDirectory(audioDirectory);
		if (response.getStatusCode() != HttpStatus.OK) {
			System.out.println( response.getBody().toString());
			throw new ResponseStatusException(response.getStatusCode(), response.getBody().toString());
		}
		
		
		
		response = audioService.saveAudio(ids, fileNames, mixers);
		if (response.getStatusCode() != HttpStatus.OK)
			throw new ResponseStatusException(response.getStatusCode(), response.getBody().toString());

		return home(model);
	}

}