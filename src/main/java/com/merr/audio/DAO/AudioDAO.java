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

	private static Integer AUDIO_ID_SEQ =0;
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

	public Integer createAudioPlayer(String fileName, String mixerName) {
		AudioPlayer ap = new AudioPlayer();
		Integer id = AUDIO_ID_SEQ++;
		ap.setId(id);
		ap.setFileName(fileName);
		for (Info info : AudioSystem.getMixerInfo()) {
			try {
				if (info.getName().equals(mixerName)) {
					ap.setMixerInfo(info);
				}
			} catch (Exception e) {
				System.out.println("not an audio output");
			}
		}

		audioPlayers.add(ap);

		return id;

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

}
