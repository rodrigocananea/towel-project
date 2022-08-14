package sandbox.installer.steps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import sandbox.installer.Step;

public class ExtractZipStep implements Step {
    private String dest;
    private String zipFile;

    @Override // sandbox.installer.Step
    public void close() {
    }

    @Override // sandbox.installer.Step
    public void doStep() {
        try {
            ZipInputStream in = new ZipInputStream(new FileInputStream(new File(this.zipFile)));
            ZipEntry entry = null;
            while (in.available() == 1) {
                entry.getMethod();
                byte[] b = new byte[((int) entry.getCompressedSize())];
                in.read(b);
                Inflater infl = new Inflater();
                infl.setInput(b);
                infl.inflate(new byte[100]);
                infl.end();
                in.closeEntry();
                entry = in.getNextEntry();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (DataFormatException e3) {
            e3.printStackTrace();
        }
    }

    @Override // sandbox.installer.Step
    public void init(String... args) {
        this.zipFile = args[0];
        this.dest = args[1];
    }
}
