import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class TicTacToe {
    private JButton[][] buttons;
    private char CP;
    private JFrame frame;
    private String Xname;
    private String Oname;
    private int XStreak;
    private int OStreak;
    private boolean isAI;
    private boolean isPlayerTurn;

    public TicTacToe(String Xname, String Oname, boolean isAI) {
        this.Xname = Xname;
        this.Oname = Oname;
        this.isAI = isAI;

        frame = new JFrame("Tic Tac Toe");
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 3));

        buttons = new JButton[3][3];
        CP = 'X';

        // Initialize buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
                buttons[i][j].setFocusPainted(false);
                int fi = i;
                int fj = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        makeMove(fi, fj);
                        checkForGameEnd();
                    }
                });
                frame.add(buttons[i][j]);
            }
        }

        frame.setVisible(true);
        isPlayerTurn = true;

        if (!isPlayerTurn && isAI) {
            aiMove();
            checkForGameEnd();
        }
    }

    public void makeMove(int r, int c) {
        if (buttons[r][c].getText().equals("") && isPlayerTurn) {
            buttons[r][c].setText(String.valueOf(CP));
            CP = (CP == 'X') ? 'O' : 'X'; // Switch player
        }
    }

    public void aiMove() {
        if (isBoardFull()) {
            return; // Board is full
        }

        Random rand = new Random();
        int r, c;
        do {
            r = rand.nextInt(3);
            c = rand.nextInt(3);
        } while (!buttons[r][c].getText().equals(""));

        buttons[r][c].setText(String.valueOf(CP));
        CP = (CP == 'X') ? 'O' : 'X'; // Switch player
    }

    public boolean checkForGameEnd() {
        if (checkForWinner()) {
            JOptionPane.showMessageDialog(frame, (isPlayerTurn ? Xname : (isAI ? "AI" : Oname)) + " wins!");
            updateStreaks(isPlayerTurn);
            if (playAgain()) {
                resetGame();
                return true;
            } else {
                frame.dispose(); // Close the window if the user chooses not to play again
                return true;
            }
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(frame, "It's a draw!");
            if (playAgain()) {
                resetGame();
                return true;
            } else {
                frame.dispose(); // Close the window if the user chooses not to play again
                return true;
            }
        }

        isPlayerTurn = !isPlayerTurn;
        if (!isPlayerTurn && isAI) {
            aiMove();
            checkForGameEnd();
        }
        return false; // Game not ended
    }

    private void updateStreaks(boolean isPlayerX) {
        String playerName = isPlayerX ? Xname : (isAI ? "AI" : Oname);
        if (isPlayerX) {
            XStreak++;
        } else {
            OStreak++;
        }
        JOptionPane.showMessageDialog(frame, playerName + " Streak: " + (isPlayerX ? XStreak : OStreak));
    }

    private boolean playAgain() {
        int result = JOptionPane.showConfirmDialog(frame, "Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    public boolean checkForWinner() {
        // Check rs and cumns
        for (int i = 0; i < 3; i++) {
            if (!buttons[i][0].getText().equals("") &&
                    buttons[i][0].getText().equals(buttons[i][1].getText()) &&
                    buttons[i][1].getText().equals(buttons[i][2].getText())) {
                return true; // r win
            }
            if (!buttons[0][i].getText().equals("") &&
                    buttons[0][i].getText().equals(buttons[1][i].getText()) &&
                    buttons[1][i].getText().equals(buttons[2][i].getText())) {
                return true; // cumn win
            }
        }

        // Check diagonals
        if (!buttons[0][0].getText().equals("") &&
                buttons[0][0].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][2].getText())) {
            return true; // Diagonal win
        }
        if (!buttons[0][2].getText().equals("") &&
                buttons[0][2].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][0].getText())) {
            return true; // Diagonal win
        }

        return false; // No winner yet
    }

    public boolean isBoardFull() {
        // Check if the board is full
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false; // Board is not full
                }
            }
        }
        return true; // Board is full
    }

    public void resetGame() {
        // Clear the board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        CP = 'X';
        isPlayerTurn = true;
        if (!isPlayerTurn && isAI) {
            aiMove();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Get player name
                String Xname = JOptionPane.showInputDialog("Enter Player X's name:");
                String Oname = JOptionPane.showInputDialog("Enter Player O's name:");

                // Choose game mode
                String[] options = {"AI", "Multiplayer"};
                int mode = JOptionPane.showOptionDialog(null, "Choose game mode", "Game Mode", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                boolean isAI = mode == 0;

                // Create the Tic Tac Toe game with selected game mode
                new TicTacToe(Xname, Oname, isAI);
            }
        });
    }
}
