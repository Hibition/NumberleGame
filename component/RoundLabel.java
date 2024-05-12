package component;

import javax.swing.*;
import java.awt.*;

public class RoundLabel extends JLabel {

    public RoundLabel() {
        super();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Dimension size = getSize();
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, size.width - 1, size.height - 1, 25, 25);

        super.paintComponent(g2d);
        g2d.dispose();
    }
}
