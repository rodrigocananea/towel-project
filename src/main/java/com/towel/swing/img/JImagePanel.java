package com.towel.swing.img;

import com.towel.graphics.LoopImage;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class JImagePanel extends JPanel {

    private FillType fillType;
    private LoopImage images;
    private volatile boolean loop;

    public JImagePanel(BufferedImage img) {
        this.fillType = FillType.RESIZE;
        this.loop = true;
        this.images = new LoopImage(0, img);
    }

    public JImagePanel(long tick, BufferedImage... imgs) {
        this.fillType = FillType.RESIZE;
        this.loop = true;
        this.images = new LoopImage(tick, imgs);
        new Looper().start();
    }

    public JImagePanel(File imgSrc) throws IOException {
        this(ImageIO.read(imgSrc));
    }

    public JImagePanel() {
        this.fillType = FillType.RESIZE;
        this.loop = true;
    }

    public JImagePanel(String fileName) throws IOException {
        this(new File(fileName));
    }

    public final void setImage(BufferedImage img) {
        LoopImage loopImage;
        if (img == null) {
            loopImage = null;
        } else {
            loopImage = new LoopImage(0, img);
        }
        this.images = loopImage;
        invalidate();
    }

    public void setImage(File img) throws IOException {
        if (img == null) {
            this.images = null;
        } else {
            setImage(ImageIO.read(img));
        }
    }

    public void setImage(String fileName) throws IOException {
        if (fileName == null) {
            this.images = null;
        } else {
            setImage(new File(fileName));
        }
    }

    public BufferedImage getImage() {
        if (this.images == null) {
            return null;
        }
        return this.images.getCurrent();
    }

    /* access modifiers changed from: protected */
    @Override
    public void paintComponent(Graphics g) {
        JImagePanel.super.paintComponent(g);
        if (this.images != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            this.fillType.drawImage(this, g2d, this.images.getCurrent());
            g2d.dispose();
        }
    }

    public FillType getFillType() {
        return this.fillType;
    }

    public void setFillType(FillType fillType2) {
        if (fillType2 == null) {
            throw new IllegalArgumentException("Invalid fill type!");
        }
        this.fillType = fillType2;
        invalidate();
    }

    public static enum FillType {
        /**
         * Make the image size equal to the panel size, by resizing it.
         */
        RESIZE {
            @Override
            public void drawImage(JPanel panel, Graphics2D g2d,
                    BufferedImage image) {
                g2d.drawImage(image, 0, 0, panel.getWidth(), panel.getHeight(),
                        null);
            }
        },
        /**
         * Centers the image on the panel.
         */
        CENTER {
            @Override
            public void drawImage(JPanel panel, Graphics2D g2d,
                    BufferedImage image) {
                int left = (panel.getWidth() - image.getWidth()) / 2;
                int top = (panel.getHeight() - image.getHeight()) / 2;
                g2d.drawImage(image, left, top, null);
            }

        },
        /**
         * Makes several copies of the image in the panel, putting them side by
         * side.
         */
        SIDE_BY_SIDE {
            @Override
            public void drawImage(JPanel panel, Graphics2D g2d,
                    BufferedImage image) {
                Paint p = new TexturePaint(image, new Rectangle2D.Float(0, 0,
                        image.getWidth(), image.getHeight()));
                g2d.setPaint(p);
                g2d.fillRect(0, 0, panel.getWidth(), panel.getHeight());
            }
        };

        public abstract void drawImage(JPanel panel, Graphics2D g2d,
                BufferedImage image);
    }

    @Override
    public Dimension getPreferredSize() {
        return this.images == null ? new Dimension() : this.images.getSize();
    }

    /* access modifiers changed from: protected */
    @Override
    public void finalize() throws Throwable {
        this.loop = false;
        super.finalize();
    }

    private class Looper extends Thread {

        public Looper() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while (JImagePanel.this.loop) {
                JImagePanel.this.repaint();
                try {
                    sleep(JImagePanel.this.images.tick);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
