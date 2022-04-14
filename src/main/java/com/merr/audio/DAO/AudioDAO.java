package com.merr.audio.DAO;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer.Info;

import org.springframework.beans.factory.annotation.Autowired;

import com.merr.audio.bean.AudioPlayer;

public class AudioDAO {

	private static Integer AUDIO_ID_SEQ = 0;
	@Autowired
	private String audioDirectory;

	@Autowired
	private List<AudioPlayer> audioPlayers;

	public List<AudioPlayer> getAudioPlayers() {
		return audioPlayers;
	}

	public AudioPlayer getAudioPlayerById(Integer audioId) {
		return audioPlayers.stream().filter(a -> a.getId().equals(audioId)).findFirst().orElse(null);
	}

	public boolean createAudioPlayer(String fileName, String mixerName) {
		AudioPlayer ap = new AudioPlayer(AUDIO_ID_SEQ++,fileName, mixerName);
		
		
		audioPlayers.add(ap);

		return true;

	}

	public List<File> getAudioFiles() {
		System.out.println(audioDirectory);
		try {
			File file = new File(audioDirectory);
			// Stream<File> stream = Arrays.stream(file.listFiles());
			// return stream.filter(f ->
			// f.getAbsolutePath().matches(".*[mp3|wav]")).toList();

			return Arrays.asList(file.listFiles());
		} catch (Exception e) {
			return new ArrayList<File>();
		}

	}

	public String getAudioDirectory() {
		return audioDirectory;
	}

	public void setAudioDirectory(String audioDirectory) {
		this.audioDirectory = audioDirectory;
	}

	public void deleteAudioPlayer(Integer audioId) {
		audioPlayers.removeIf(a -> a.getId().equals(audioId));

	}

}
