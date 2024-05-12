package component;

import javax.swing.border.Border;
import java.awt.*;

public class RoundBorder implements Border {
    private Color color;
    private int thickness;

    public void setBorderColor(Color color) {
        this.color = color;
    }

    public RoundBorder(Color color, int thickness) {
        this.color = color;
        this.thickness = thickness;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (thickness <= 0) return; // Don't paint if thickness is non-positive

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawRoundRect(x, y, width - 1, height - 1, 25, 25);
        g2d.dispose();
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, 0, 0);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
