package sandbox.installer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StepCfgLoader {
    private int currentStep;
    private List<String> lines;

    public StepCfgLoader(File file) throws FileNotFoundException {
        this(new FileInputStream(file));
    }

    public StepCfgLoader(InputStream is) {
        this.currentStep = 0;
        Scanner sc = new Scanner(is);
        this.lines = new ArrayList();
        while (sc.hasNext()) {
            this.lines.add(sc.nextLine());
        }
    }

    public String getNextStep() {
        List<String> list = this.lines;
        int i = this.currentStep;
        this.currentStep = i + 1;
        return list.get(i);
    }
}
