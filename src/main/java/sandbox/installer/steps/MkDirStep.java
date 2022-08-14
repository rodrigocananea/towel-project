package sandbox.installer.steps;

import java.io.File;
import java.io.IOException;
import sandbox.installer.Step;

public class MkDirStep implements Step {
    private String mkPath;

    @Override // sandbox.installer.Step
    public void doStep() {
        try {
            new File(this.mkPath).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override // sandbox.installer.Step
    public void close() {
    }

    @Override // sandbox.installer.Step
    public void init(String... args) {
        this.mkPath = args[0];
    }
}
