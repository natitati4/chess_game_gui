package chess_game_gui.app.GameGUI;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

class PieceLabel extends JLabel
{
    private int color;
    public static final int PIECE_SIZE = (int)(ChessCellPanel.CELL_SIZE * 0.8);
    
    public PieceLabel(ImageIcon icon, int color)
    {
        Image image = icon.getImage(); // transform it 
        Image newImg = image.getScaledInstance(PIECE_SIZE, PIECE_SIZE, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        icon = new ImageIcon(newImg);  // transform it back
        setIcon(icon);
        this.color = color;
    }

    /**
     * @return int return the color
     */
    public int getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(int color) {
        this.color = color;
    }

}