package chess_game_gui.app.MainComponents;

public abstract class Piece 
{
    public static final int WHITE = 0;
    public static final int BLACK = 1;

    private int color;
    
    public Piece(int color)
    {
        this.color = color;
    }
    
    public abstract boolean validateMove(Board board, Cell start, Cell end);

    public abstract boolean hasMoves(Board board, Cell start);

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