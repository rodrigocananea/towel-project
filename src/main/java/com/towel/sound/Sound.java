package com.towel.sound;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound implements Sampled {
    private AudioFormat format;
    private byte[] samples;

    public Sound(URL resource) throws UnsupportedAudioFileException, IOException {
        this(AudioSystem.getAudioInputStream(resource));
    }

    public Sound(File file) throws UnsupportedAudioFileException, IOException {
        this(AudioSystem.getAudioInputStream(file));
    }

    public Sound(String fileName) throws UnsupportedAudioFileException, IOException {
        this(new File(fileName));
    }

    public Sound(AudioInputStream stream) throws IOException {
        this.samples = null;
        setSamples(getSamplesFromAudio(stream));
        this.format = stream.getFormat();
    }

    /* access modifiers changed from: protected */
    public byte[] getSamplesFromAudio(AudioInputStream stream) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException("You must provide an AudioInputStream!");
        }
        byte[] samples2 = new byte[((int) (stream.getFrameLength() * ((long) stream.getFormat().getFrameSize())))];
        new DataInputStream(stream).readFully(samples2);
        return samples2;
    }

    /* access modifiers changed from: protected */
    public void setSamples(byte[] samples2) {
        if (samples2 == null) {
            throw new IllegalArgumentException("You must provide some samples!");
        }
        this.samples = samples2;
    }

    @Override // com.towel.sound.Sampled
    public byte[] getSamples() {
        return this.samples;
    }

    @Override // com.towel.sound.Streamed
    public InputStream newInputStream() {
        return new ByteArrayInputStream(this.samples);
    }

    @Override // com.towel.sound.Formatted
    public AudioFormat getFormat() {
        return this.format;
    }
}
