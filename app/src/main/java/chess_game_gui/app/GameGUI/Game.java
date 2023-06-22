package chess_game_gui.app.GameGUI;

import java.awt.GridLayout;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;

public class Game 
{
    public static final int BOTTOM_WHITE = -1;
    public static final int BOTTOM_BLACK = 1;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                createAndShowGUI();
            }

            private void createAndShowGUI() 
            {
                JFrame frame = new JFrame("Chess game");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                boolean isWhiteOnBottom = chooseColorOnBottom();
                frame.add(new ChessBoardPanel(isWhiteOnBottom));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    private static boolean chooseColorOnBottom()
    {
        JDialog colorDialog = new JDialog();
        colorDialog.setTitle("Choose Color on bottom");
        colorDialog.setModal(true);
        colorDialog.setSize(300, 200);
        colorDialog.setLayout(new GridLayout(2, 1));
        colorDialog.setLocationRelativeTo(null); // Center the dialog on the screen

        // Create the color buttons
        JButton whiteButton = new JButton("White");
        JButton blackButton = new JButton("Black");

        AtomicBoolean isWhiteOnBottom = new AtomicBoolean(false);

        // Add action listeners to the buttons
        whiteButton.addActionListener(e -> 
        {
            isWhiteOnBottom.set(true);
            colorDialog.dispose(); // Close the dialog
        });

        blackButton.addActionListener(e -> 
        {
            colorDialog.dispose(); // Close the dialog
        });

        // Add the buttons to the dialog
        colorDialog.add(whiteButton);
        colorDialog.add(blackButton);

        // Display the dialog
        colorDialog.setVisible(true);

        return isWhiteOnBottom.get();
    }
}