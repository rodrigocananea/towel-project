package com.towel.swing.img;

import com.towel.graphics.LoopImage;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

@Deprecated
public class ImageLoopPanel extends JPanel implements Runnable {
    private LoopImage lImg;
    private long tick;

    public ImageLoopPanel(long tick2, BufferedImage... imgs) {
        this.tick = tick2;
        this.lImg = new LoopImage(tick2, imgs);
        setMinimumSize(new Dimension(imgs[0].getWidth(), imgs[0].getHeight()));
        new Thread(this).start();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        this.lImg.draw(g2d);
        g2d.dispose();
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(this.tick);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
