package chess_game_gui.app.GameGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

class ChessCellPanel extends JPanel 
{
    public static final int CELL_SIZE = 100;
    private static final Color LIGHT_COLOR = new Color(240, 217, 181);
    private static final Color DARK_COLOR = new Color(181, 136, 99);
    private int row;
    private int col;
    private Color cellColor;

    public ChessCellPanel(int row, int col) 
    {
        this.row = row;
        this.col = col;
        this.cellColor = (row + col) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR;
        setBackground(cellColor);
        setLayout(new GridBagLayout());
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Color getCellColor() {
        return cellColor;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(CELL_SIZE, CELL_SIZE);
    }
}