package sandbox;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;
import javax.swing.Icon;

/* compiled from: PagingTable */
class ArrowIcon implements Icon {
    public static final int DOWN = 1;
    public static final int UP = 0;
    private Polygon arrowDownPolygon = new Polygon(this.arrowX, new int[]{6, 6, 11}, 3);
    private Polygon arrowUpPolygon = new Polygon(this.arrowX, new int[]{10, 10, 4}, 3);
    private int[] arrowX = {4, 9, 6};
    private int direction;
    private Polygon pagePolygon = new Polygon(new int[]{2, 4, 4, 10, 10, 2}, new int[]{4, 4, 2, 2, 12, 12}, 6);

    public ArrowIcon(int which) {
        this.direction = which;
    }

    public int getIconWidth() {
        return 14;
    }

    public int getIconHeight() {
        return 14;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(Color.black);
        this.pagePolygon.translate(x, y);
        g.drawPolygon(this.pagePolygon);
        this.pagePolygon.translate(-x, -y);
        if (this.direction == 0) {
            this.arrowUpPolygon.translate(x, y);
            g.fillPolygon(this.arrowUpPolygon);
            this.arrowUpPolygon.translate(-x, -y);
            return;
        }
        this.arrowDownPolygon.translate(x, y);
        g.fillPolygon(this.arrowDownPolygon);
        this.arrowDownPolygon.translate(-x, -y);
    }
}
