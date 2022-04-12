package com.merr.audio.bean;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.springframework.web.multipart.MultipartFile;

public class AudioPlayer {

	private AudioInputStream inputStream;
	private Mixer.Info mixerInfo;
	private Clip clip;
	private boolean playing;
	private String fileName;
	private Integer id;
	
	
	
	public AudioPlayer() {
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		setPlaying(false);
	}

	public void play() throws Exception {

		try {
			File file = new File(fileName);
			if (inputStream == null)
				inputStream = AudioSystem.getAudioInputStream(file);
			if (!clip.isOpen())
				clip.open(inputStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			setPlaying(true);
		} catch (Exception e) {
			setPlaying(false);
			System.out.println(e.getMessage());
		}
	}

	public void pause() {
		clip.stop();
		setPlaying(false);
	}

	public void stop() {
		clip.stop();
		setPlaying(false);
		clip.setFramePosition(0);
		clip.setMicrosecondPosition(0);
	}
	
	public void prepareForDelete() {
		if(clip.isOpen())
			clip.close();
	}
	
	public void setInputStream(AudioInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public AudioInputStream getInputStream() {
		return inputStream;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public Mixer.Info getMixerInfo() {
		return mixerInfo;
	}

	public void setMixerInfo(Mixer.Info mixerInfo) {
		this.mixerInfo = mixerInfo;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	// creates a reusable audio inputstream from a multipartfile
	public static AudioInputStream createReusableAudioInputStream(MultipartFile file)
			throws IOException, UnsupportedAudioFileException {
		AudioInputStream ais = null;
		try {
			InputStream bufferedIn = new BufferedInputStream(file.getInputStream());
			ais = AudioSystem.getAudioInputStream(bufferedIn);
			byte[] buffer = new byte[1024 * 32];
			int read = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream(buffer.length);
			while ((read = ais.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, read);
			}
			AudioInputStream reusableAis = new AudioInputStream(new ByteArrayInputStream(baos.toByteArray()),
					ais.getFormat(), AudioSystem.NOT_SPECIFIED);
			return reusableAis;
		} finally {
			if (ais != null) {
				ais.close();
			}
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
