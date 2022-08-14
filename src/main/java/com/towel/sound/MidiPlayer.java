package com.towel.sound;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class MidiPlayer {
    private boolean paused;
    private Sequencer sequencer;

    public MidiPlayer() {
        try {
            this.sequencer = MidiSystem.getSequencer();
            this.sequencer.open();
            this.paused = true;
        } catch (MidiUnavailableException e) {
            this.sequencer = null;
        }
    }

    public void play(Sequence midi) {
        play(midi, true);
    }

    public void play(Sequence midi, boolean loop) {
        if (this.sequencer != null && midi != null) {
            try {
                this.sequencer.setSequence(midi);
                this.sequencer.setMicrosecondPosition(0);
                if (loop) {
                    this.sequencer.setLoopCount(-1);
                }
                setPaused(false);
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (this.sequencer != null && this.sequencer.isOpen()) {
            this.sequencer.stop();
            this.sequencer.setMicrosecondPosition(0);
        }
    }

    public void close() {
        if (this.sequencer != null && this.sequencer.isOpen()) {
            this.sequencer.close();
        }
    }

    public void setPaused(boolean paused2) {
        if (this.paused != paused2 && this.sequencer != null) {
            this.paused = paused2;
            if (paused2) {
                this.sequencer.stop();
            } else {
                this.sequencer.start();
            }
        }
    }

    public boolean isPaused() {
        return this.paused;
    }

    public double getTempoFactor() {
        if (this.sequencer == null) {
            return 0.0d;
        }
        return (double) this.sequencer.getTempoFactor();
    }

    public boolean getTrackMute(int track) {
        if (this.sequencer == null) {
            return true;
        }
        return this.sequencer.getTrackMute(track);
    }

    public boolean getTrackSolo(int track) {
        if (this.sequencer == null) {
            return false;
        }
        return this.sequencer.getTrackSolo(track);
    }

    public void setTempoFactor(double factor) {
        if (this.sequencer != null) {
            this.sequencer.setTempoFactor((float) factor);
        }
    }

    public void setTrackMute(int track, boolean mute) {
        if (this.sequencer != null) {
            this.sequencer.setTrackMute(track, mute);
        }
    }

    public void setTrackSolo(int track, boolean solo) {
        if (this.sequencer != null) {
            this.sequencer.setTrackSolo(track, solo);
        }
    }

    public boolean isSequencerAvailable() {
        return this.sequencer != null;
    }
}
