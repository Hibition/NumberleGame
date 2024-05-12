package component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RoundButton extends JButton {
    private Color buttonGray = new Color(220, 225, 237);
    private Color buttonClick = new Color(144, 154, 176);
    private Color butMouseOn = new Color(180, 190, 200);

    private Color buttonColor;

    public RoundButton(String text) {
        super(text);
        this.setSize(80, 80);

        buttonColor = buttonGray;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    public void resetColor() {
        buttonColor = buttonGray;
        this.changeColor(buttonGray);
    }

    public void changeColor(Color color) {
        buttonColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (buttonColor != buttonGray) {
            g.setColor(buttonColor);
        } else if (getModel().isArmed()) {
            g.setColor(buttonClick);
        } else if (getModel().isRollover()) {

            g.setColor(butMouseOn);
        } else {
            g.setColor(buttonColor);
        }

        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
        super.paintComponent(g);
    }


    @Override
    protected void paintBorder(Graphics g) {
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(60, 80);
    }
}
