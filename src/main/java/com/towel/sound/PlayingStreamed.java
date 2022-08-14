package com.towel.sound;

import java.io.InputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class PlayingStreamed implements Runnable {
    private volatile boolean isFinished;
    private volatile boolean isPaused;
    private Streamed stream;

    PlayingStreamed(Streamed stream2) {
        this.isPaused = false;
        this.isFinished = false;
        this.isPaused = false;
        this.isFinished = false;
        this.stream = stream2;
    }

    public void setPaused(boolean pause) {
        this.isPaused = pause;
    }

    public void stop() {
        this.isFinished = true;
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    private SourceDataLine createDataLine(int bufferSize) throws LineUnavailableException {
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, this.stream.getFormat()));
        line.open(this.stream.getFormat(), bufferSize);
        return line;
    }

    private byte[] createStreamedBuffer() {
        return new byte[(this.stream.getFormat().getFrameSize() * Math.round(this.stream.getFormat().getSampleRate() / 10.0f))];
    }

    public void run() {
        if (this.isFinished) {
            throw new IllegalStateException("Sound already played.");
        }
        SourceDataLine line = null;
        try {
            byte[] buffer = createStreamedBuffer();
            SourceDataLine line2 = createDataLine(buffer.length);
            InputStream input = this.stream.newInputStream();
            line2.start();
            int numBytesRead = 0;
            while (numBytesRead != -1 && !isFinished()) {
                if (isPaused()) {
                    Thread.yield();
                } else {
                    numBytesRead = input.read(buffer, 0, buffer.length);
                    if (numBytesRead != -1) {
                        line2.write(buffer, 0, numBytesRead);
                    }
                }
            }
            stop();
            if (line2 != null) {
                line2.drain();
                line2.close();
            }
        } catch (Exception e) {
            stop();
            if (0 != 0) {
                line.drain();
                line.close();
            }
        } catch (Throwable th) {
            stop();
            if (0 != 0) {
                line.drain();
                line.close();
            }
            throw th;
        }
    }

    public Streamed getStream() {
        return this.stream;
    }
}
