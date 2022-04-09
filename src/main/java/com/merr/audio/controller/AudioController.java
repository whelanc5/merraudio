package com.merr.audio.controller;

import java.io.IOException;
import java.util.ArrayList;

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

import com.merr.audio.bean.AudioPlayer;
import com.merr.audio.service.AudioService;

@Controller
public class AudioController {

	@Autowired
	private AudioService audioService;

	@RequestMapping("/")
	public String home(Model model) throws IOException, LineUnavailableException {
		model.addAttribute("storageMap", new ArrayList<AudioPlayer>(audioService.getAudioOutputs().values()));
		model.addAttribute("audioFiles", audioService.getAudioFiles());
		model.addAttribute("audioDirectory", audioService.getAudioDirectory());
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
	public ResponseEntity<?> play(@RequestParam("outputName") String outputName) {
		return audioService.play(outputName);
	}

	@GetMapping("/pause")
	@ResponseBody
	public ResponseEntity<?> pause(@RequestParam("outputName") String outputName) {
		return audioService.pause(outputName);
	}

	@GetMapping("/stop")
	@ResponseBody
	public ResponseEntity<?> stop(@RequestParam("outputName") String outputName) throws IOException, Exception {
		return audioService.stop(outputName);
	}

	@GetMapping("/save")
	public String save(HttpServletRequest request, Model model) throws IOException, LineUnavailableException {

		String audioDirectoryTemp = request.getParameter("audioDirectory");
		System.out.print(audioDirectoryTemp);

		ResponseEntity<?> response = audioService.setAudioDirectory(audioDirectoryTemp);
		if (response.getStatusCode() != HttpStatus.OK) {
			System.out.println( response.getBody().toString());
			throw new ResponseStatusException(response.getStatusCode(), response.getBody().toString());
		}

		response = audioService.saveSongs(request);
		if (response.getStatusCode() != HttpStatus.OK)
			throw new ResponseStatusException(response.getStatusCode(), response.getBody().toString());

		return home(model);
	}

}