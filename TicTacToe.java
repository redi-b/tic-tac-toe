import javax.swing.*;
import javax.swing.border.CompoundBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class TicTacToe extends JFrame {

    enum WinType {
        ROW, COLUMN, DIAGONAL, ANTI_DIAGONAL
    }

    static ImageIcon xIcon = new ImageIcon("icons/x.png");
    static ImageIcon oIcon = new ImageIcon("icons/o.png");

    JPanel gamePanel;
    JPanel labelPanel;
    JLabel label;
    JButton reset;

    int gridSize = 3;

    int turn = 0;
    Character[][] game = new Character[gridSize][gridSize];
    JButton[] buttons = new JButton[gridSize * gridSize];

    WinType winType;
    int winRowColIdx;

    Color winColor = Color.decode("#26de81");
    Color btnColor = Color.decode("#1e272e");
    Color bgColor = Color.decode("#40407a");

    ButtonListener btnListener = new ButtonListener(this);

    TicTacToe() {
        setSize((gridSize + 1) * 100, (gridSize + 1) * 100);
        if (gridSize >= 7) setSize(700, 700);
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        gamePanel = new JPanel(new GridLayout(gridSize, gridSize, 4, 4));
        gamePanel.setBackground(bgColor);
        gamePanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        for (int i = 0; i < (gridSize * gridSize); i++) {
            buttons[i] = new JButton();
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(btnColor);
            buttons[i].setFont(buttons[i].getFont().deriveFont(Font.BOLD).deriveFont(42f));
            buttons[i].addActionListener(btnListener);
            buttons[i].setActionCommand(String.valueOf(i / gridSize) + " " + String.valueOf(i % gridSize));
            gamePanel.add(buttons[i]);
        }

        labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBackground(bgColor);
        labelPanel.setBorder(BorderFactory.createEmptyBorder(8, 6, 4, 8));
        label = new JLabel("Player 1's (X) turn");
        label.setFont(new Font("Poppins", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        labelPanel.add(label, BorderLayout.WEST);
        reset = new JButton("Reset Game");
        reset.setFont(new Font("Poppins", Font.PLAIN, 13));
        reset.setBackground(Color.decode("#d63031"));
        reset.setForeground(Color.WHITE);
        reset.setFocusPainted(false);
        reset.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        reset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }

        });
        labelPanel.add(reset, BorderLayout.EAST);

        add(labelPanel, BorderLayout.NORTH);
        add(gamePanel);
        setVisible(true);
    }

    public int checkWin() {

        int winner = -1;

        for (int i = 0; i < gridSize; i++) {
            if (count(game[i], 'X') == gridSize) {
                winner = 1;
                winType = WinType.ROW;
                winRowColIdx = i;
                break;
            }
            if (count(game[i], 'O') == gridSize) {
                winner = 2;
                winType = WinType.ROW;
                winRowColIdx = i;
                break;
            }
        }

        for (int i = 0; i < gridSize; i++) {
            if (count(getColumn(i), 'X') == gridSize) {
                winner = 1;
                winType = WinType.COLUMN;
                winRowColIdx = i;
                break;
            }
            if (count(getColumn(i), 'O') == gridSize) {
                winner = 2;
                winType = WinType.COLUMN;
                winRowColIdx = i;
                break;
            }
        }

        if (count(getDiagonal(), 'X') == gridSize) {
            winner = 1;
            winType = WinType.DIAGONAL;
        }
        if (count(getDiagonal(), 'O') == gridSize) {
            winner = 2;
            winType = WinType.DIAGONAL;
        }

        if (count(getDiagonal(0), 'X') == gridSize) {
            winner = 1;
            winType = WinType.ANTI_DIAGONAL;
        }
        if (count(getDiagonal(0), 'O') == gridSize) {
            winner = 2;
            winType = WinType.ANTI_DIAGONAL;
        }

        return winner;
    }

    private int count(Character[] array, Character item) {

        int count = 0;

        for (int i = 0; i < gridSize; i++) {
            if (array[i] == item)
                count++;
        }

        return count;
    }

    private Character[] getColumn(int idx) {
        Character[] col = new Character[gridSize];

        for (int i = 0; i < gridSize; i++) {
            col[i] = game[i][idx];
        }

        return col;
    }

    private Character[] getDiagonal() {
        Character[] col = new Character[gridSize];

        for (int i = 0; i < gridSize; i++) {
            col[i] = game[i][i];
        }

        return col;
    }

    private Character[] getDiagonal(int inverse) {
        Character[] col = new Character[gridSize];

        for (int i = (gridSize - 1); i >= 0; i--) {
            col[i] = game[inverse][i];
            inverse++;
        }

        return col;
    }

    public void endGame() {

        for (JButton btn : buttons) {
            btn.setBorderPainted(false);
            btn.setEnabled(false);
            btn.setDisabledIcon(btn.getIcon());
        }

        switch (winType) {
            case ROW:
                for (int i = 0; i < gridSize; i++) {
                    // buttons[(winRowColIdx * gridSize) + i].setBackground(winColor);
                    buttons[(winRowColIdx * gridSize) + i].setBorderPainted(true);
                    buttons[(winRowColIdx * gridSize) + i].setBorder(BorderFactory.createLineBorder(winColor, 2));
                }

                break;

            case COLUMN:
                for (int i = 0; i < gridSize; i++) {
                    // buttons[(i * gridSize) + winRowColIdx].setBackground(winColor);
                    buttons[(i * gridSize) + winRowColIdx].setBorderPainted(true);
                    buttons[(i * gridSize) + winRowColIdx].setBorder(BorderFactory.createLineBorder(winColor, 2));
                }
                break;

            case DIAGONAL:
                for (int i = 0; i < gridSize; i++) {
                    // buttons[(i * gridSize) + i].setBackground(winColor);
                    buttons[(i * gridSize) + i].setBorderPainted(true);
                    buttons[(i * gridSize) + i].setBorder(BorderFactory.createLineBorder(winColor, 2));
                    ;
                }
                break;

            case ANTI_DIAGONAL:
                for (int i = 0; i < gridSize; i++) {
                    // buttons[(i * gridSize) + (gridSize - 1 - i)].setBackground(winColor);
                    buttons[(i * gridSize) + (gridSize - 1 - i)].setBorderPainted(true);
                    buttons[(i * gridSize) + (gridSize - 1 - i)].setBorder(BorderFactory.createLineBorder(winColor, 2));
                    ;
                }
                break;

            default:
                break;
        }

    }

    public void resetGame() {
        turn = 0;
        label.setText("Player 1's (X) turn");
        for (JButton btn : buttons) {
            // btn.setText("");
            btn.setIcon(null);
            btn.setEnabled(true);
            btn.setBorder(new JButton().getBorder());
            btn.setBorderPainted(true);
        }
        Arrays.stream(game).forEach(x -> Arrays.fill(x, null));
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}

class ButtonListener implements ActionListener {

    TicTacToe xo;
    int winner;

    ButtonListener(TicTacToe xo) {
        this.xo = xo;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JButton btn = (JButton) event.getSource();
        String[] idxs = event.getActionCommand().split(" ");
        int i = Integer.parseInt(idxs[0]), j = Integer.parseInt(idxs[1]);
        if (xo.turn % 2 == 0) {
            btn.setIcon(TicTacToe.xIcon);
            btn.setDisabledIcon(TicTacToe.xIcon);
            btn.setEnabled(false);
            btn.setBorderPainted(false);
            xo.game[i][j] = 'X';
            xo.label.setText("Player 2's (O) turn");
        } else {
            btn.setIcon(TicTacToe.oIcon);
            btn.setDisabledIcon(TicTacToe.oIcon);
            btn.setEnabled(false);
            btn.setBorderPainted(false);
            xo.game[i][j] = 'O';
            xo.label.setText("Player 1's (X) turn");
        }

        if (xo.turn >= (xo.gridSize + 1)) {
            winner = xo.checkWin();

            switch (winner) {
                case 1:
                    xo.label.setText("Game Over - Player 1 Wins!");
                    xo.gamePanel.setEnabled(false);
                    xo.endGame();
                    break;

                case 2:
                    xo.label.setText("Game Over - Player 2 Wins!");
                    xo.gamePanel.setEnabled(false);
                    xo.endGame();
                    break;

                default:
                    break;
            }

            if (xo.turn == (xo.gridSize * xo.gridSize - 1) && winner == -1)
                xo.label.setText("Game Over - No Win!");
        }

        xo.turn++;
    }

}