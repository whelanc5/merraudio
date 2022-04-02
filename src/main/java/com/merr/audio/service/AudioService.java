package com.merr.audio.service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer.Info;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.merr.audio.bean.AudioOutput;
import com.merr.audio.bean.AudioPlayerImpl;
@Service
public class AudioService {

	@Value("${audio_directory}")
	private String audioDir;

	List<File> getAudioFiles() {
		File file = new File(audioDir);
		Stream<File> stream = Arrays.stream(file.listFiles());
		return stream.filter(f -> f.getName().matches(".*[mp3|wav]")).toList();
	}
	
	
	public AudioOutput<String, AudioPlayerImpl> getAudioOutputs() throws LineUnavailableException {
		AudioOutput<String, AudioPlayerImpl> storageMap = new AudioOutput<String, AudioPlayerImpl>();

		for (Info info : AudioSystem.getMixerInfo()) {
			try {
				AudioPlayerImpl ap = new AudioPlayerImpl(info);
				storageMap.put(info.getName(), ap);
				//TODO determine what is an output
			} catch (Exception e) {
				System.out.println("not an audio output");
			}
		}
		return storageMap;
	}
	
	
	
	
}
