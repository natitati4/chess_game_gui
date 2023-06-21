package chess_game_gui.app.GameGUI;

import javax.swing.*;

public class Game 
{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() 
            {
                createAndShowGUI();
            }

            private void createAndShowGUI() 
            {
                JFrame frame = new JFrame("Chess game");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new ChessBoardPanel());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}