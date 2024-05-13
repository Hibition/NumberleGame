import java.awt.*;

import java.io.FileNotFoundException;
import java.util.Observer;
import java.util.Observable;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import component.RoundBorder;
import component.RoundButton;
import component.RoundLabel;


public class NumberleView extends JFrame implements Observer {

    private int maxRow;
    private int maxCol;
    private RoundLabel[][] guessGrid;

    private JPanel gridPanel;
    private JPanel gridBorderPanel;
    private JPanel buttonPanel;

    private StringBuilder currentGuess = new StringBuilder("");

    private NumberleController controller;

    private boolean canReStart = false;


    private Color noExist = new Color(164, 174, 196);
    private Color rightPlace = new Color(47, 193, 165);
    private Color wrongPlace = new Color(247, 154, 111);
    private Color borderGray = new Color(220, 220, 220);
    private Color transparent = new Color(0, 0, 0, 0);


    private Border emptyBorder = new EmptyBorder(10, 5, 10, 5);

    private Font customFont = new Font("Arial", Font.BOLD, 20);


    public NumberleView(NumberleController controller, int maxGuess, int equationLength) {
        /**
         @pre none
         @post The Controller can't be empty.
         */
        super("Numberle Game");
        maxRow = maxGuess;
        maxCol = equationLength;
        this.controller = controller;
        assert this.controller != null : "Controller must not be null!";

        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.red);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(contentPane);

        initializeFrame();
        this.controller.setView(this);

    }


    private void initializeFrame() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeGridPanel();
        initializeButtonPanel();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        this.setResizable(false);
    }


    private void initializeGridPanel() {
        guessGrid = new RoundLabel[maxRow][maxCol];

        gridPanel = new JPanel(new GridLayout(maxRow, maxCol));
        gridPanel.setPreferredSize(new Dimension(620, 540));
        gridPanel.setMaximumSize(new Dimension(620, 540));
        gridPanel.setMinimumSize(new Dimension(620, 540));


        gridPanel.setLayout(new GridLayout(6, 7));
        Border panelBorder = new EmptyBorder(10, 10, 10, 10);
        gridPanel.setBorder(panelBorder);

        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                RoundLabel label = new RoundLabel();
                Border grayBorder = new RoundBorder(borderGray, 3);

                JPanel borderPanel = new JPanel();
                label.setBorder(grayBorder);
                borderPanel.add(label);

                label.setFont(this.customFont);

                label.setHorizontalAlignment(JLabel.CENTER);

                Dimension labelDim = new Dimension(80, 80);
                label.setPreferredSize(labelDim);
                label.setMinimumSize(labelDim);
                label.setMaximumSize(labelDim);
                guessGrid[i][j] = label;
                gridPanel.add(borderPanel);
            }
        }

        this.gridBorderPanel = new JPanel();

        JButton b = new JButton();
        ImageIcon icon = new ImageIcon("logo.png");
        JLabel j = new JLabel(icon);
        j.setAlignmentX(Component.CENTER_ALIGNMENT);
        Border border = new EmptyBorder(20, 10, 10, 10);
        j.setBorder(border);
        gridBorderPanel.add(j);
        gridBorderPanel.setLayout(new BoxLayout(gridBorderPanel, BoxLayout.Y_AXIS));

        gridBorderPanel.setPreferredSize(new Dimension(620, 640));
        gridBorderPanel.setMaximumSize(new Dimension(620, 640));
        gridBorderPanel.setMinimumSize(new Dimension(620, 640));
        gridBorderPanel.add(gridPanel);

        add(gridBorderPanel, BorderLayout.CENTER);

    }

    private void initializeButtonPanel() {
        buttonPanel = new JPanel(new GridLayout(2, 8, 7, 7));
        buttonPanel.setPreferredSize(new Dimension(1000, 170));


        String[] buttons = {
                "1", "2", "3", "4", "5", "6", "7",
                "8", "9", "0", "+", "-", "*", "/", "="
        };

        for (String buttonText : buttons) {

            RoundButton button = new RoundButton(buttonText);
            button.setFont(this.customFont);
            button.setBorder(emptyBorder);
            button.setBackground(Color.red);

            button.addActionListener(e -> {
                if (currentGuess.length() < maxCol) {
                    this.currentGuess.append(buttonText);
                    assert buttonText.length() <= maxCol;
                    guessGrid[maxRow - controller.getRemainingAttempts()][currentGuess.length() - 1].setText(buttonText);
                }
            });
            button.resetColor();
            buttonPanel.add(button);
        }

        JButton deleteButton = new RoundButton("Delete");
        deleteButton.setFont(this.customFont);
        deleteButton.setSize(100, 80);

        deleteButton.addActionListener(e -> {
            if (currentGuess.length() != 0) {
                guessGrid[maxRow - controller.getRemainingAttempts()][currentGuess.length() - 1].setText(" ");
                currentGuess.deleteCharAt(currentGuess.length() - 1);
            }
        });

        JButton submitButton = new RoundButton("Enter");
        submitButton.setFont(this.customFont);
        submitButton.addActionListener(e -> {
            if (controller.processInput(currentGuess.toString())) {
                currentGuess.setLength(0);
            }
        });

        JButton rePlayButton = new RoundButton("Replay");
        rePlayButton.setFont(this.customFont);
        rePlayButton.addActionListener(e -> {
            if (!canReStart) {
                JOptionPane.showMessageDialog(null, "<html><font size='5'>You haven't finish first try!</font></html>", "Can't replay", JOptionPane.ERROR_MESSAGE);
                return;
            }
            canReStart = false;
            currentGuess.setLength(0);
            try {
                controller.startNewGame();

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

            resetGrid();
            resetButton();
        });

        buttonPanel.add(deleteButton);
        buttonPanel.add(submitButton);
        buttonPanel.add(rePlayButton);

        Border border = BorderFactory.createEmptyBorder(20, 30, 30, 30);

        buttonPanel.setBorder(border);

        this.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        /**
         * @pre. o must exit.
         * @pre. o must be instant of NumberleModel
         * @post. none.
         */
        assert o != null;
        assert o instanceof NumberleModel : "The Observable object should be NumberleModel";
        NumberleModel model = (NumberleModel) o;
        if (model.getRemainingAttempts() < maxRow)
            canReStart = true;   // the restart button only work after the first try.
        String error = model.getErrorMessage();
        if (error != null) {
            error = "<html><font size='5'>" + error + "</font></html>";
            JOptionPane.showMessageDialog(null, error, "Invalid Input", JOptionPane.ERROR_MESSAGE);
            model.clearErrorMessage();
            return;
        }


        for (int i = 0; i < maxCol; i++) {
            if (model.getGussSiduation()[i] == 0) {
                guessGrid[maxRow - model.getRemainingAttempts() - 1][i].setBackground(rightPlace);
                changeRoundBorderColor(guessGrid[maxRow - model.getRemainingAttempts() - 1][i].getBorder(), rightPlace);
                searchButton(guessGrid[maxRow - model.getRemainingAttempts() - 1][i].getText()).changeColor(rightPlace);
            } else if (model.getGussSiduation()[i] == 1) {
                guessGrid[maxRow - model.getRemainingAttempts() - 1][i].setBackground(wrongPlace);
                changeRoundBorderColor(guessGrid[maxRow - model.getRemainingAttempts() - 1][i].getBorder(), wrongPlace);
                if (searchButton(guessGrid[maxRow - model.getRemainingAttempts() - 1][i].getText()).getBackground() != rightPlace) {
                    searchButton(guessGrid[maxRow - model.getRemainingAttempts() - 1][i].getText()).changeColor(wrongPlace);
                }
            } else if (model.getGussSiduation()[0] == -1) {  // the invalid input, jump over
                break;
            } else {
                guessGrid[maxRow - model.getRemainingAttempts() - 1][i].setBackground(noExist);
                changeRoundBorderColor(guessGrid[maxRow - model.getRemainingAttempts() - 1][i].getBorder(), noExist);
                searchButton(guessGrid[maxRow - model.getRemainingAttempts() - 1][i].getText()).changeColor(noExist);
            }
        }

        System.out.println("Remaining attempts: " + model.getRemainingAttempts());
        if (controller.isGameOver()) {
            if (controller.isGameWon()) {
                JOptionPane.showMessageDialog(null, "<html><font size='5'>Congratulations, you won the game!</font></html>", "Game Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "<html><font size='5'>Sorry! You run out of chances!</font></html>", "Game Result", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    /**
     * Reset the button color after restart the game.
     */
    private void resetButton() {
        for (Component component : buttonPanel.getComponents()) {
            if (component instanceof RoundButton) {
                RoundButton button = (RoundButton) component;
                button.resetColor();
            }
        }
    }

    /**
     * Reset the Grid label color and border color after restart the game.
     */
    private void resetGrid() {
        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                guessGrid[row][col].setText(" ");
                changeRoundBorderColor(guessGrid[row][col].getBorder(), borderGray);
                guessGrid[row][col].setBackground(transparent);
            }
        }
    }

    private void changeRoundBorderColor(Border border, Color color) {
        assert border instanceof RoundBorder : "The border of Grid should be Round Border";
        RoundBorder roundBorderObject = (RoundBorder) border;
        roundBorderObject.setBorderColor(color);
    }

    private RoundButton searchButton(String buttonName) {
        for (Component button : buttonPanel.getComponents()) {
            if (((RoundButton) button).getText().contains(buttonName)) {
                return (RoundButton) button;
            }
        }
        assert true : "Can't find the button which need change color";
        return null;
    }
}
