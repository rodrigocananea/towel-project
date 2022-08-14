package com.towel.sound.filter;

import com.towel.sound.Streamed;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;

public class LoopFilter implements Streamed {
    private Streamed stream;

    public LoopFilter(Streamed stream2) {
        if (stream2 == null) {
            throw new IllegalArgumentException("Source cannot be null!");
        } else if (stream2 instanceof LoopFilter) {
            throw new IllegalArgumentException("Cannot loop a loop!");
        } else {
            this.stream = stream2;
        }
    }

    @Override // com.towel.sound.Streamed
    public InputStream newInputStream() {
        return new LoopInputStream(this.stream.newInputStream());
    }

    @Override // com.towel.sound.Formatted
    public AudioFormat getFormat() {
        return this.stream.getFormat();
    }

    private class LoopInputStream extends InputStream {
        private InputStream source;

        protected LoopInputStream(InputStream in) {
            this.source = in;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            int read = this.source.read();
            if (read != -1) {
                return read;
            }
            this.source.reset();
            return this.source.read();
        }
    }
}
