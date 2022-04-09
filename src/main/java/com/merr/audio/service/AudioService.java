package com.merr.audio.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer.Info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.merr.audio.bean.AudioOutput;
import com.merr.audio.bean.AudioPlayer;

@Service
public class AudioService {

	@Autowired
	private String audioDirectory;

	@Autowired
	private AudioOutput<String, AudioPlayer> storageMap;
	
	
	public String getAudioDirectory() {
		return audioDirectory;
	}
	
	public List<File> getAudioFiles() {
		System.out.println(audioDirectory);
		File file = new File(audioDirectory);
		Stream<File> stream = Arrays.stream(file.listFiles());
		return stream.filter(f -> f.getAbsolutePath().matches(".*[mp3|wav]")).toList();
	}

	public String saveFile(MultipartFile file, String outputName) throws IOException, Exception {
		AudioPlayer ap = storageMap.get(outputName);
		AudioInputStream stream = AudioPlayer.createReusableAudioInputStream(file);
		ap.setInputStream(stream);
		ap.setFileName(file.getOriginalFilename());
		return outputName;
	}
	
	public ResponseEntity<?> setAudioDirectory(String directory) {
		try {
			File file = new File(directory);
			if (file == null || !file.exists() || !file.isDirectory()) {
				return new ResponseEntity<>("error saving directory", HttpStatus.BAD_REQUEST);
			}
			audioDirectory = directory;
		} catch (Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public ResponseEntity<?> play(String outputName) {
		try {
			AudioPlayer ap = storageMap.get(outputName);
			if (ap.getFileName() == null || ap.getFileName().isEmpty()) {
				return new ResponseEntity<>("no file declared", HttpStatus.BAD_REQUEST);
			}
			ap.play();
		} catch (Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public ResponseEntity<?> pause(String outputName) {
		try {
			AudioPlayer ap = storageMap.get(outputName);
			ap.pause();
		} catch (Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public ResponseEntity<?> stop(String outputName) {

		try {
			AudioPlayer ap = storageMap.get(outputName);
			ap.stop();
		} catch (Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public ResponseEntity<?> saveSongs(HttpServletRequest request) {
		
		
		try {
			for (int i = 0; i < storageMap.size(); i++) {
				AudioPlayer ap = storageMap.get(request.getParameter("outputName" + i));
				ap.setFileName(request.getParameter("fileName" + i));
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	public AudioOutput<String, AudioPlayer> getAudioOutputs() throws LineUnavailableException {
		if (storageMap == null) {
			this.storageMap = new AudioOutput<String, AudioPlayer>();
		}

		for (Info info : AudioSystem.getMixerInfo()) {
			try {
				if (storageMap.get(info.getName()) == null) {
					AudioPlayer ap = new AudioPlayer(info);
					storageMap.put(info.getName(), ap);
				}

			} catch (Exception e) {
				System.out.println("not an audio output");
			}
		}
		return storageMap;
	}

}
