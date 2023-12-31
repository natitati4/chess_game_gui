package chess_game_gui.app.MainComponents;

public class Cell 
{
    private int row;
    private int col;
    private Piece piece;

    public Cell(int row, int col, Piece piece)
    {
        this.row = row;
        this.col = col;
        this.piece = piece;
    }

    /**
     * @return int return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @return int return the col
     */
    public int getCol() {
        return col;
    }

    /**
     * @param col the col to set
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * @return Piece return the piece
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * @param piece the piece to set
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

}
