package img;

import com.towel.swing.img.JImagePanel;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class JImagePanelLoopTest {

    final static String pathImages = "C:\\Users\\rodri\\OneDrive\\Área de Trabalho\\images\\";

    public static void main(String[] args) throws Exception {

        JImagePanel panel = new JImagePanel(20, new BufferedImage[]{
            loadImage("1.jpg"),
            loadImage("2.jpg"),
            loadImage("3.jpg"),
            loadImage("4.jpg"),
            loadImage("5.jpg"),
            loadImage("6.jpg"),
            loadImage("7.jpg"),
            loadImage("8.jpg")
        });

        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(100, 100));
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static BufferedImage loadImage(String file) throws IOException {
        return ImageIO.read(new File(pathImages + file));
    }
}
