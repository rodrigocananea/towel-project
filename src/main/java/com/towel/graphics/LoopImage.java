package com.towel.graphics;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class LoopImage {
    private int currentIndex = 0;
    private BufferedImage[] imgs;
    private long lastTick;
    public long tick;

    public LoopImage(long tick2, BufferedImage... imgs2) {
        this.imgs = (BufferedImage[]) imgs2.clone();
        this.tick = tick2;
    }

    public void draw(Graphics2D g) {
        updateTick();
        Graphics2D g2d = (Graphics2D) g.create();
        BufferedImage current = this.imgs[this.currentIndex];
        g2d.drawImage(current, 0, 0, current.getWidth(), current.getHeight(), (ImageObserver) null);
        g2d.dispose();
    }

    private void updateTick() {
        long currentTick = System.currentTimeMillis();
        if (currentTick - this.lastTick > this.tick) {
            this.currentIndex++;
        }
        this.lastTick = currentTick;
        if (this.currentIndex == this.imgs.length) {
            this.currentIndex = 0;
        }
    }

    public BufferedImage getCurrent() {
        updateTick();
        return this.imgs[this.currentIndex];
    }

    public Dimension getSize() {
        return new Dimension(this.imgs[this.currentIndex].getWidth(), this.imgs[this.currentIndex].getHeight());
    }
}
