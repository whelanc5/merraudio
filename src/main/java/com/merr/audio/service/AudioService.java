package com.merr.audio.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer.Info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.merr.audio.DAO.AudioDAO;
import com.merr.audio.bean.AudioPlayer;

@Service
public class AudioService {

	@Autowired
	private AudioDAO audioDAO;

	public List<AudioPlayer> getAudioPlayers() {
		return audioDAO.getAudioPlayers();
	}

	public List<File> getAudioFiles() {
		return audioDAO.getAudioFiles();

	}

	public String getAudioDirectory() {
		return audioDAO.getAudioDirectory();
	}

	public String saveFile(MultipartFile file, String fileName) throws IOException, Exception {
		return "";
	}

	public ResponseEntity<?> setAudioDirectory(String directory) {
		try {
			File file = new File(directory);
			if (file == null || !file.exists() || !file.isDirectory()) {
				return new ResponseEntity<>("error saving directory", HttpStatus.BAD_REQUEST);
			}
			audioDAO.setAudioDirectory(directory);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public ResponseEntity<?> play(Integer audioId) {
		try {
			AudioPlayer ap = audioDAO.getAudioPlayerById(audioId);
			if (ap.getFileName() == null || ap.getFileName().isEmpty()) {
				return new ResponseEntity<>("no file declared", HttpStatus.BAD_REQUEST);
			}
			ap.play();
		} catch (Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public ResponseEntity<?> pause(Integer audioId) {
		try {
			AudioPlayer ap = audioDAO.getAudioPlayerById(audioId);
			ap.pause();
		} catch (Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public ResponseEntity<?> stop(Integer audioId) {

		try {
			AudioPlayer ap = audioDAO.getAudioPlayerById(audioId);
			ap.stop();
		} catch (Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public ResponseEntity<?> delete(Integer audioId) {

		try {
			AudioPlayer ap = audioDAO.getAudioPlayerById(audioId);
			ap.prepareForDelete();
			audioDAO.deleteAudioPlayer(audioId);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public ResponseEntity<?> saveAudio(List<Integer> ids, List<String> fileNames, List<String> mixers) {

		try {
			for (Integer i = 0; i < ids.size(); i++) {
				Integer audioId = ids.get(i);
				AudioPlayer ap;

				ap = audioDAO.getAudioPlayerById(audioId);
				if (ap == null) {
					ap = new AudioPlayer();
					ap.setId(audioDAO.createAudioPlayer(fileNames.get(i), mixers.get(i)));
				}
				
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	public List<String> getAudioOutputs() throws LineUnavailableException {

		List<String> audioOutputs = new ArrayList<String>();
		for (Info info : AudioSystem.getMixerInfo()) {
			try {
				Clip clip = AudioSystem.getClip(info);
				audioOutputs.add(info.getName());
			} catch (Exception e) {
				System.out.println("not an audio output");
			}
		}
		return audioOutputs;
	}

}
