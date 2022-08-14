package com.towel.sound;

import com.towel.sound.filter.LoopFilter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

public class SoundManager {
    private AudioFormat format;
    private ExecutorService soundPool;

    public SoundManager(AudioFormat format2) {
        if (format2 == null) {
            throw new IllegalArgumentException("Invalid audio format!");
        }
        this.format = format2;
        this.soundPool = createThreadPool(format2, new SoundManagerThreadFactory(null));
    }

    public AudioFormat getFormat() {
        return this.format;
    }

    private ExecutorService createThreadPool(AudioFormat format2, ThreadFactory threadFactory) {
        int max = getMaxSimultaneousSounds(format2);
        if (max == -1 || max == 0) {
            return Executors.newCachedThreadPool(threadFactory);
        }
        if (max == 1) {
            return Executors.newSingleThreadExecutor(threadFactory);
        }
        return Executors.newFixedThreadPool(max, threadFactory);
    }

    private int getMaxSimultaneousSounds(AudioFormat format2) {
        return AudioSystem.getMixer((Mixer.Info) null).getMaxLines(new DataLine.Info(SourceDataLine.class, format2));
    }

    public PlayingStreamed play(Streamed streamed) {
        return play(streamed, false);
    }

    public PlayingStreamed play(Streamed streamed, boolean loop) {
        if (this.soundPool.isShutdown()) {
            throw new IllegalStateException("Manager already closed!");
        } else if (!this.format.matches(streamed.getFormat())) {
            throw new IllegalArgumentException("The streamed noise is a \n" + streamed.getFormat() + " but it should be a \n" + this.format);
        } else {
            if (loop) {
                streamed = new LoopFilter(streamed);
            }
            PlayingStreamed playing = new PlayingStreamed(streamed);
            this.soundPool.execute(playing);
            return playing;
        }
    }

    public void close() {
        this.soundPool.shutdownNow();
    }

    private static class SoundManagerThreadFactory implements ThreadFactory {
        private SoundManagerThreadFactory() {
        }

        /* synthetic */ SoundManagerThreadFactory(SoundManagerThreadFactory soundManagerThreadFactory) {
            this();
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("Sound Thread - " + Long.toString(t.getId()));
            t.setDaemon(true);
            return t;
        }
    }
}
